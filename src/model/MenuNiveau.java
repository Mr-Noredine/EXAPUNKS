package model;
import java.util.Scanner;

public class MenuNiveau {
    private Scanner scanner;

    public MenuNiveau() {
        this.scanner = new Scanner(System.in);
    }

    public Niveau choisirNiveau() {
        System.out.println("Choisissez un niveau :");
        System.out.println("1. Niveau 1");
        System.out.println("2. Niveau 2");
        System.out.println("3. Niveau 3");
        System.out.print("Entrez le numéro du niveau : ");

        int choix = scanner.nextInt();

        // Assurez-vous de valider le choix de l'utilisateur
        while (choix < 1 || choix > 3) {
            System.out.println("Choix invalide. Veuillez réessayer.");
            choix = scanner.nextInt();
        }

        // Créer une instance du niveau choisi
        switch (choix) {
            case 1:
                return new Niveau1();
            case 2:
                return new Niveau2();
            case 3:
                return new Niveau3();
            default:
                return null; // Ce cas ne devrait normalement jamais se produire grâce à la validation
        }
    }


}
