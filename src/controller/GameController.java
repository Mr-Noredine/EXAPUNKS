package controller;


import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import model.*;
import view.GamePanel;
import view.GameWindow;
import view.TextZone;
public class GameController {
    
    private Grille grille;
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique();
    // private Random random = new Random();
    public GameController(GameWindow gameWindow, Grille grille) {
        this.grille = grille;
        this.gamePanel = gameWindow.getGamePanel();
        this.gameWindow=gameWindow;
    
    }
    public void executeNextCommand(String code, int id) {

        new Thread(() -> {
            String[] lines = code.split("\n");
            int currentIndex = 0; // Commence à la première ligne
            int nbJump = 0;
            boolean dernierTestResultat = false; // Stocke le résultat du dernier TEST
    
            while (currentIndex < lines.length) {
                String line = lines[currentIndex].trim();
                if (!line.isEmpty()) { // Ignore les lignes vides
                    Instruction instruction = analyseurSyntaxique.getCommande(line);
                    if (instruction != null) {
                        // Ajout de la gestion pour TEST
                        if ("TEST".equals(instruction.getMotCommande().toUpperCase())) {
                            JTextArea memoryArea1 = gameWindow.getTextZone().getMemoryArea1(); // Obtient l'objet JTextArea
                            String valeur = memoryArea1.getText(); // Convertit le contenu du JTextArea en String
                            int valeurX =(Integer.parseInt(valeur)); // Vous devez obtenir la valeur actuelle de X
                            dernierTestResultat = valeurX != 0; // TEST X != 0
                        } else {
                            executeInstruction(id, instruction);
                        }
    
                        // Si la commande est JUMP, et que c'est le premier saut, ou FJUMP basé sur le résultat de TEST
                        if ("JUMP".equals(instruction.getMotCommande().toUpperCase()) && nbJump == 0) {
                            nbJump++;
                            currentIndex = adjustJumpIndex(instruction, currentIndex, lines);
                        } else if ("FJUMP".equals(instruction.getMotCommande().toUpperCase()) && !dernierTestResultat) {
                            nbJump++;
                            currentIndex = adjustJumpIndex(instruction, currentIndex, lines);
                        }
    
                        try {
                            Thread.sleep(2000); // Attend 2 secondes
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Restaure l'état d'interruption
                            return; // Sort du thread si une interruption est capturée
                        }
                    }
                }
                currentIndex++; // Passe à la ligne suivante, sauf si JUMP/FJUMP a modifié l'index
                final int finalIndex = currentIndex; // Pour l'utilisation dans SwingUtilities.invokeLater
                SwingUtilities.invokeLater(() -> {
                    gamePanel.repaint();
                });
            }
        }).start();
    }
    
    private int adjustJumpIndex(Instruction instruction, int currentIndex, String[] lines) {
        try {
            int jumpIndex = Integer.parseInt(instruction.getArguments()[0]);
            if (jumpIndex >= 0 && jumpIndex < lines.length) {
                currentIndex = jumpIndex - 1; // -1 car il sera incrémenté après
            } else {
                System.out.println("Index de JUMP invalide");
            }
        } catch (NumberFormatException e) {
            System.out.println("Argument de JUMP non valide");
        }
        return currentIndex;
    }
private void executeInstruction(int id, Instruction instruction) {
        String commande = instruction.getMotCommande();
        String[] arguments = instruction.getArguments(); 
        
        switch (commande.toUpperCase()) {
            case "LINK":
                if (arguments.length > 0) {
                    grille.traiterCommandeLink(id, arguments[0]); 
                }
                break;
            case "GRAB":
            int idfichier=Integer.parseInt(arguments[0]);
                grille.GRAB(id,idfichier ); 
                break;
            case "DROP":
                grille.DROP(id);
                break;
            case "ADDI":
                if (arguments.length == 3) {
                    String VALEUR1 = arguments[0];
                    String VALEUR2 = arguments[1];
                    String destinationIndex = arguments[2];
                    grille.add(id, VALEUR1,VALEUR2, destinationIndex,gameWindow);
                }
                break;
            case "SUBI":
            if (arguments.length == 3) {
                String VALEUR1 = arguments[0];
                String VALEUR2 = arguments[1];
                String destinationIndex = arguments[2];
                grille.sub(id, VALEUR1,VALEUR2, destinationIndex,gameWindow);
            }
                break;
            case "MULI":
            if (arguments.length == 3) {
                String VALEUR1 = arguments[0];
                String VALEUR2 = arguments[1];
                String destinationIndex = arguments[2];
                grille.mult(id, VALEUR1,VALEUR2, destinationIndex,gameWindow);
            }
                break;
            
            case "TESTEOF":
                grille.traiterCommandeTestEOF(id);
                break;
            case "COPY":
                if (arguments.length >= 2) {
                    String source=arguments[0];
                    String destination = arguments[1];
                    grille.copy(id,source, destination,gameWindow);
                   // memoryArea1.setText(String.valueOf(numero));
                   
                }
                break;
                case "DIVI":
                if (arguments.length == 3) {
                    String VALEUR1 = arguments[0];
                    String VALEUR2 = arguments[1];
                    String destinationIndex = arguments[2];
                    grille.devi(id, VALEUR1,VALEUR2, destinationIndex,gameWindow);
                }
            break;
            case "MODI":
                if (arguments.length == 3) {
                    String VALEUR1 = arguments[0];
                    String VALEUR2 = arguments[1];
                    String destinationIndex = arguments[2];
                    grille.modi(id, VALEUR1,VALEUR2, destinationIndex,gameWindow);
                }
                    break;    
            case "NOOP":
                grille.NOOP();
                break;
            case "HALT":
            grille.traiterCommandeHalt(id, gameWindow);
            break;
            default:
                System.out.println("Commande non reconnue : " + commande);
                break;
        }
    }

}