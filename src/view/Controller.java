package view;

import controller.GameController;
import model.Niveau;


public class Controller implements Runnable {
    
    private GameWindow gameWindow;
    private GameController gameController;
    private GamePanel gamePanel;
    private Thread gameThread;

    public Controller(GameWindow gameWindow, Niveau niveau) {
        this.gameWindow = gameWindow;
        this.gamePanel = gameWindow.getGamePanel();
        gameController = new GameController(gameWindow, niveau.getGrille());
    
    }


    public void startGame() {
        gameWindow.setVisible(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            try {
                SharedSemaphore.acquire();
                // Execute one step for each active robot
                for (model.Robot robot : gameWindow.getNiveau().getGrille().getListeRobots()) {
                    if (robot.estActif()) {
                        update(robot.getId());
                    }
                }
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            gameWindow.repaint();
        }
    }

    public void update(int id) {     
        String code = "";
        if (id == 1) {
            code = gameWindow.getTextZone().getTextArea().getText();
        } else if (id == 2 && gameWindow.getTextZone2() != null) {
            code = gameWindow.getTextZone2().getTextArea().getText();
        }
        
        if (!code.isEmpty()) {
            gameController.executeNextStep(id, code);  
        }
    }

    public void paintComponent() {
        gameWindow.repaint();
    }

    public GameController getGameController() {
        return gameController;
    }

    
}
