package controller;

import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import model.*;
import view.GamePanel;
import view.GameWindow;

public class GameController {
    
    private Grille grille;
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique();
    private Map<String, Integer> labels = new HashMap<>();
    private String currentCode = "";

    public GameController(GameWindow gameWindow, Grille grille) {
        this.grille = grille;
        this.gamePanel = gameWindow.getGamePanel();
        this.gameWindow = gameWindow;
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
        Robot robot = grille.getRobotId(id);
        if (robot == null || !robot.estActif()) return;

        String[] lines = code.split("\n");
        int currentIndex = robot.getCurrentIndex();

        if (currentIndex >= lines.length) {
            System.out.println("Fin du programme pour le robot " + id);
            return;
        }

        String line = lines[currentIndex].trim();
        // Skip labels and empty lines
        while (line.isEmpty() || line.endsWith(":") || line.startsWith("MARK ")) {
            currentIndex++;
            if (currentIndex >= lines.length) {
                robot.setCurrentIndex(currentIndex);
                return;
            }
            line = lines[currentIndex].trim();
        }

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
        SwingUtilities.invokeLater(() -> {
            gamePanel.repaint();
            updateUI(robot);
        });
    }

    private void handleTest(Robot robot, String[] args) {
        if (args.length < 3) return;
        try {
            int v1 = obtenirValeur(robot, args[0]);
            String op = args[1];
            int v2 = obtenirValeur(robot, args[2]);
            boolean result = false;
            switch (op) {
                case "=": result = (v1 == v2); break;
                case ">": result = (v1 > v2); break;
                case "<": result = (v1 < v2); break;
                case "!=": result = (v1 != v2); break;
            }
            robot.setCaseMemoire(1, result ? 1 : 0);
        } catch (Exception e) {
            System.err.println("Erreur TEST: " + e.getMessage());
        }
    }

    private int obtenirValeur(Robot r, String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            switch (input.toUpperCase()) {
                case "X": return (Integer) r.getCaseMemoire(0);
                case "T": return (Integer) r.getCaseMemoire(1);
                case "M": return (Integer) r.getRegistreM();
                case "F":
                    if (r.getFichierEnMain() != null) {
                        String val = r.getFichierEnMain().retirerContenu();
                        return val != null ? Integer.parseInt(val) : 0;
                    }
            }
        }
        return 0;
    }

    private void updateUI(Robot r) {
        gameWindow.getTextZone().setMemoryArea1Text(String.valueOf(r.getCaseMemoire(0)));
        gameWindow.getTextZone().setMemoryArea2Text(String.valueOf(r.getCaseMemoire(1)));
        gameWindow.getTextZone().setMemoryArea4Text(String.valueOf(r.getRegistreM()));
        if (r.getFichierEnMain() != null) {
            gameWindow.getTextZone().setMemoryArea3Text("Holding: " + r.getFichierEnMain().getId());
        } else {
            gameWindow.getTextZone().setMemoryArea3Text("None");
        }
    }

    private void executeInstruction(int id, Instruction instruction) {
        String commande = instruction.getMotCommande().toUpperCase();
        String[] arguments = instruction.getArguments(); 
        
        switch (commande) {
            case "LINK":
                if (arguments.length > 0) grille.traiterCommandeLink(id, arguments[0]); 
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
