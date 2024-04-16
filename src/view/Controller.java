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
            
            System.out.println("SALUT !");
            try {
                SharedSemaphore.acquire();
                update(1);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            gameWindow.repaint();
        }
    }


    public void update(int id) {     
        String code = gameWindow.getTextZone().getTextArea().getText();
        System.out.println(code);
        gameController.executeNextCommand(code, id);  
    }

    public void paintComponent() {
        gameWindow.repaint();
    }

    
}
