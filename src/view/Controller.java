package view;

import controller.GameController;
import model.Niveau;


public class Controller implements Runnable {

    private GameWindow gameWindow;
    private GameController gameController;
    private GamePanel gamePanel;
    private Thread gameThread;
    /** Set to true to signal the run loop to exit cleanly. */
    private volatile boolean running = false;

    public Controller(GameWindow gameWindow, Niveau niveau) {
        this.gameWindow = gameWindow;
        this.gamePanel = gameWindow.getGamePanel();
        gameController = new GameController(gameWindow, niveau.getGrille());
    }


    public void startGame() {
        gameWindow.setVisible(true);
    }

    public void startGameThread() {
        running = true;
        gameThread = new Thread(this);
        gameThread.setDaemon(true);  // Dies automatically when the JVM exits
        gameThread.start();
    }

    /** Stop the game loop and unblock the thread if it is waiting on the semaphore. */
    public void stopGameThread() {
        running = false;
        gameController.stopAutoRun();
        // Release the semaphore so the blocked thread wakes up and can check `running`
        SharedSemaphore.release();
        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                SharedSemaphore.acquire();
                if (!running) break;  // Check again after waking up
                // Execute one step for each active robot
                for (model.Robot robot : gameWindow.getNiveau().getGrille().getListeRobots()) {
                    if (robot.estActif()) {
                        update(robot.getId());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
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
        javax.swing.SwingUtilities.invokeLater(() -> gameWindow.repaint());
    }

    public GameController getGameController() {
        return gameController;
    }


}
