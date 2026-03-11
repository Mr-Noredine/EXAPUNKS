package controller;

import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import model.*;
import view.GamePanel;
import view.GameWindow;
import view.TextZone;
import view.SharedSemaphore;

public class GameController {
    
    private Grille grille;
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique();
    private Map<String, Integer> labels = new HashMap<>();
    private String currentCode = "";
    private Timer autoTimer;
    private int speed = 300;
    private int networkActivity = 0;

    public GameController(GameWindow gameWindow, Grille grille) {
        this.grille = grille;
        this.gamePanel = gameWindow.getGamePanel();
        this.gameWindow = gameWindow;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        if (autoTimer != null && autoTimer.isRunning()) {
            autoTimer.setDelay(speed);
        }
    }

    private void preprocess(String code) {
        if (code.equals(currentCode)) return;
        currentCode = code;
        labels.clear();
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.endsWith(":")) {
                labels.put(line.substring(0, line.length() - 1), i);
            } else if (line.startsWith("MARK ")) {
                labels.put(line.substring(5).trim(), i);
            }
        }
    }

    public void executeNextStep(int id, String code) {
        preprocess(code);
        Niveau niveau = gameWindow.getNiveau();
        Robot robot = grille.getRobotId(id);
        if (robot == null || !robot.estActif()) return;

        TextZone tz = (id == 2 && gameWindow.getTextZone2() != null) ? gameWindow.getTextZone2() : gameWindow.getTextZone();

        String[] lines = code.split("\n");
        int currentIndex = robot.getCurrentIndex();

        if (currentIndex >= lines.length) {
            System.out.println("Fin du programme pour le robot " + id);
            tz.highlightLine(-1); // Remove highlight
            return;
        }

        String line = lines[currentIndex].trim();
        // Skip labels and empty lines
        while (line.isEmpty() || line.endsWith(":") || line.startsWith("MARK ")) {
            currentIndex++;
            if (currentIndex >= lines.length) {
                robot.setCurrentIndex(currentIndex);
                tz.highlightLine(-1);
                return;
            }
            line = lines[currentIndex].trim();
        }
        
        // Persist the index after skipping
        robot.setCurrentIndex(currentIndex);

        // Highlight the current line BEFORE executing
        tz.highlightLine(currentIndex);

        Instruction instruction = analyseurSyntaxique.getCommande(line);
        if (instruction != null && instruction.getMotCommande() != null) {
            boolean jumpTaken = false;
            String cmd = instruction.getMotCommande().toUpperCase();
            String[] args = instruction.getArguments();

            switch (cmd) {
                case "TEST":
                    handleTest(robot, args);
                    break;
                case "JUMP":
                    if (args.length > 0 && labels.containsKey(args[0])) {
                        currentIndex = labels.get(args[0]);
                        jumpTaken = true;
                    }
                    break;
                case "FJUMP":
                    Object tVal = robot.getCaseMemoire(1);
                    if (tVal instanceof Integer && (Integer)tVal == 0) {
                        if (args.length > 0 && labels.containsKey(args[0])) {
                            currentIndex = labels.get(args[0]);
                            jumpTaken = true;
                        }
                    }
                    break;
                case "TJUMP":
                    Object tValT = robot.getCaseMemoire(1);
                    if (tValT instanceof Integer && (Integer)tValT != 0) {
                        if (args.length > 0 && labels.containsKey(args[0])) {
                            currentIndex = labels.get(args[0]);
                            jumpTaken = true;
                        }
                    }
                    break;
                default:
                    executeInstruction(id, instruction);
                    break;
            }

            if (!jumpTaken) {
                currentIndex++;
            }
        } else {
            currentIndex++;
        }

        robot.setCurrentIndex(currentIndex);
        niveau.setNbSolution(); // Increment cycles
        SwingUtilities.invokeLater(() -> {
            gamePanel.repaint();
            updateUI(robot, tz);
            checkVictory();
        });
    }

    public void startAutoRun() {
        if (autoTimer != null && autoTimer.isRunning()) return;
        autoTimer = new Timer(speed, e -> {
            SharedSemaphore.release();
        });
        autoTimer.start();
    }

    public void stopAutoRun() {
        if (autoTimer != null) {
            autoTimer.stop();
        }
    }

    public void resetGame() {
        stopAutoRun();
        Niveau currentNiveau = gameWindow.getNiveau();
        Niveau freshNiveau;
        if (currentNiveau instanceof Niveau1) freshNiveau = new Niveau1();
        else if (currentNiveau instanceof Niveau2) freshNiveau = new Niveau2();
        else if (currentNiveau instanceof Niveau3) freshNiveau = new Niveau3();
        else return;

        gameWindow.afficherFenetreJeu(freshNiveau);
    }

    private void checkVictory() {
        Niveau niveau = gameWindow.getNiveau();
        if (niveau.testVictoire()) {
            stopAutoRun();
            int totalSize = calculateTotalSize();
            String stats = String.format("Cycles: %d\nCode Size: %d\nNetwork Activity: %d", 
                niveau.getNbSolution(), totalSize, networkActivity);
            JOptionPane.showMessageDialog(gameWindow, "MISSION ACCOMPLISHED!\n" + stats, "Success", JOptionPane.INFORMATION_MESSAGE);
            gameWindow.afficherOptionsNiveau();
            gameWindow.frame2.dispose();
        }
    }

    private int calculateTotalSize() {
        int size = countLines(gameWindow.getTextZone().getTextArea().getText());
        if (gameWindow.getTextZone2() != null) {
            size += countLines(gameWindow.getTextZone2().getTextArea().getText());
        }
        return size;
    }

    private int countLines(String code) {
        if (code == null || code.isEmpty()) return 0;
        String[] lines = code.split("\n");
        int count = 0;
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith(";") && !trimmed.startsWith("//") && !trimmed.endsWith(":")) {
                count++;
            }
        }
        return count;
    }

    private void handleTest(Robot robot, String[] args) {
        if (args.length < 3) return;
        try {
            int v1 = grille.obtenirValeur(robot, args[0]);
            String op = args[1];
            int v2 = grille.obtenirValeur(robot, args[2]);
            boolean result = false;
            switch (op) {
                case "=": result = (v1 == v2); break;
                case ">": result = (v1 > v2); break;
                case "<": result = (v1 < v2); break;
                case "!=": result = (v1 != v2); break;
                case ">=": result = (v1 >= v2); break;
                case "<=": result = (v1 <= v2); break;
            }
            robot.setCaseMemoire(1, result ? 1 : 0);
        } catch (Exception e) {
            System.err.println("Error in handleTest: " + e.getMessage());
        }
    }

    private void updateUI(Robot r, TextZone tz) {
        tz.setMemoryArea1Text(String.valueOf(r.getCaseMemoire(0)));
        tz.setMemoryArea2Text(String.valueOf(r.getCaseMemoire(1)));
        tz.setMemoryArea4Text(String.valueOf(r.getRegistreM()));
        if (r.getFichierEnMain() != null) {
            tz.setMemoryArea3Text("F: " + r.getFichierEnMain().getId());
        } else {
            tz.setMemoryArea3Text("None");
        }
    }

    private void executeInstruction(int id, Instruction instruction) {
        String commande = instruction.getMotCommande().toUpperCase();
        String[] arguments = instruction.getArguments(); 
        
        switch (commande) {
            case "LINK":
                if (arguments.length > 0) {
                    grille.traiterCommandeLink(id, arguments[0]);
                    networkActivity++;
                }
                break;
            case "GRAB":
                if (arguments.length > 0) grille.GRAB(id, Integer.parseInt(arguments[0])); 
                break;
            case "DROP":
                grille.DROP(id);
                break;
            case "ADDI":
                if (arguments.length == 3) grille.add(id, arguments[0], arguments[1], arguments[2], gameWindow);
                break;
            case "SUBI":
                if (arguments.length == 3) grille.sub(id, arguments[0], arguments[1], arguments[2], gameWindow);
                break;
            case "MULI":
                if (arguments.length == 3) grille.mult(id, arguments[0], arguments[1], arguments[2], gameWindow);
                break;
            case "COPY":
                if (arguments.length >= 2) grille.copy(id, arguments[0], arguments[1], gameWindow);
                break;
            case "HALT":
                grille.traiterCommandeHalt(id, gameWindow);
                break;
            case "NOOP":
                break;
        }
    }
}
