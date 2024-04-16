package view;

import java.util.concurrent.Semaphore;

public class SharedSemaphore {
    public static final Semaphore semaphore = new Semaphore(0);

    // Constructeur privé pour éviter l'instanciation de la classe utilitaire
    private SharedSemaphore() { }

    // Méthode statique pour acquérir le sémaphore
    public static void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    // Méthode statique pour libérer le sémaphore
    public static void release() {
        semaphore.release();
    }

    public static int availablePermits() {
        return semaphore.availablePermits();
    }
}
