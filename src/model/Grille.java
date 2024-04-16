package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import model.Case.TypeCase;
import view.GamePanel;
import view.GameWindow;
import view.TextZone;
import model.Fichier;

public class Grille {
    private TypeTerritoire[][] territoires;
    private Case[][] cases;
    private ArrayList<Robot> robots;
    private ArrayList<Fichier> fichiers;
    private Map<Integer, Integer[]> destinationsPortes;
    
    public Grille(ArrayList<Robot> robots, ArrayList<Fichier> fichiers) {
        // Initialiser la grille avec les types de territoire plutôt que des int
        territoires = new TypeTerritoire[][] {
            {TypeTerritoire.LIBRE, TypeTerritoire.PORTE2, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.PORTE3, TypeTerritoire.PORTE5},
            {TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE},
            {TypeTerritoire.LIBRE, TypeTerritoire.PORTE4, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
        };
        cases = new Case[5][5];
        this.robots = new ArrayList<>(robots);
        this.fichiers = new ArrayList<>(fichiers);

        initTerritoires();
        initDestinationsPortes();
    }

    private void initTerritoires() {
        for (int i = 0; i < territoires.length; i++) {
            for (int j = 0; j < territoires[i].length; j++) {
                TypeTerritoire typeTerritoire = territoires[i][j];
                TypeCase typeCase = switch (typeTerritoire) {
                    case LIBRE -> TypeCase.LIBRE;
                    case OCCUPE -> TypeCase.OCCUPEE;
                    default -> TypeCase.PORTE;
                };
                cases[i][j] = new Case(i, j, typeCase);
            }
        }
    }
    private void initDestinationsPortes() {
        destinationsPortes = new HashMap<>();
        destinationsPortes.put(2, new Integer[]{3, 0});
        destinationsPortes.put(3, new Integer[]{0, 3});
        destinationsPortes.put(4, new Integer[]{4, 4});
        destinationsPortes.put(5, new Integer[]{1, 1});
    }

    ////////////ajout d'un fichier dans la grille/////////////////
    public void ajouterFichier(Fichier fichier, int posX, int posY) {
        fichier.setPosition(posX, posY); 
        fichiers.add(fichier);
        cases[posX][posY].setFichier(fichier); 
    }

    /////// liste des robots /////////
    public ArrayList<Robot> getListeRobots() {
        ArrayList<Robot> result = new ArrayList<>(robots);
        return result;
    }

    ////liste des fichiers/////
    public ArrayList<Fichier> getListFichiers() {
        ArrayList<Fichier> result = new ArrayList<>(fichiers);
        return result;
    }

    // Méthode pour récupérer un fichier par son ID
    public Fichier getFichierParId(int id) {
    for (Fichier fichier : fichiers) {
        if (fichier.getId() == id) {
            return fichier;
        }
    }
    return null;
}
    // Méthode pour récupérer un robot par son ID
    public Robot getRobotId(int id) {
    for(Robot r : getListeRobots()) {
        if (r.getId() == id) {
            return r;
        }
    }
    return null;
    }

    ////////verifier si la case contient un fichier/////////
    public boolean contientFichier(int x, int y) {
        return cases[x][y].getFichier() != null;
    }

    /////////recuperer un  fichier depuis une case////////
    public Fichier getFichier(int x, int y) {
        return cases[x][y].getFichier();
    }

    /////////supprimer un fichier+///////////
    public void retirerFichier(Fichier fichier) {
        for (int i = 0; i < cases.length; i++) {
            for (int j = 0; j < cases[i].length; j++) {
                if (cases[i][j].getFichier() == fichier) {
                    cases[i][j].setFichier(null);
                    return; 
                }
            }
        }
    }
    //////////supprimer un fichier par son id///////////
    public void retirerFichier(int fichierId) {
        Fichier fichier = getFichierParId(fichierId);
        if (fichier != null) {
            retirerFichier(fichier);
        } else {
            System.out.println("Le fichier avec l'ID " + fichierId + " n'existe pas dans la grille.");
        }
    }
    
    // Méthode pour associer un fichier à un robot
    public void associerFichierRobot(Fichier fichier, Robot robot) {
        if (!fichierEstTransporte(fichier) && !robot.aFichier()) {
            robot.setFichierEnMain(fichier);
            retirerFichier(fichier);
        } else {
            System.out.println("Impossible d'associer le fichier au robot.");
        }
    }
   

    ///////le type de la case/////
    public TypeTerritoire getTerritoire(int x, int y) {
        if (x >= 0 && x < 5 && y >= 0 && y < 5) {
            return territoires[y][x];
        }
        return TypeTerritoire.LIBRE; // Retourne un type par défaut si hors limites
    }
    


    public boolean estZoneAccessible(int x, int y) {
        return getTerritoire(x, y) == TypeTerritoire.LIBRE;
    }
    
    public boolean estZoneOccupee(int x, int y) {
        return getTerritoire(x, y) == TypeTerritoire.OCCUPE;
    }

    public boolean estPorteNumerotee(int x, int y) {
        TypeTerritoire territoire = getTerritoire(x, y);
        return territoire != TypeTerritoire.LIBRE && territoire != TypeTerritoire.OCCUPE;
    }

    
    public ArrayList<Robot> getRobotsActifs() {
        ArrayList<Robot> actifs = new ArrayList<>();
        for (Robot r : this.robots) {
            if (r.estActif()) {
                actifs.add(r);
            }
        }
        return actifs;
    }
   //Méthode pour déplacer un fichier d'une position à une autre dans la grille
    public void deplacerFichier(int origX, int origY, int destX, int destY) {
        if (contientFichier(origX, origY) && estZoneAccessible(destX, destY)) {
            Fichier fichier = getFichier(origX, origY);
            cases[origX][origY].setFichier(null);
            cases[destX][destY].setFichier(fichier);
            fichier.setPosition(destX, destY);
        } else {
            System.out.println("Déplacement de fichier impossible.");
        }
    }

   

    // Méthode pour déposer un fichier sur une case de la grille
    public void deposerFichierSurCase(Fichier fichier, int posX, int posY) {
        if (!contientFichier(posX, posY) && estZoneAccessible(posX, posY)) {
            cases[posX][posY].setFichier(fichier);
            fichier.setPosition(posX, posY);
        } else {
            System.out.println("Impossible de déposer le fichier sur cette case.");
        }
    }

    // Méthode pour vérifier si un fichier est en cours de transport par un robot
    public boolean fichierEstTransporte(Fichier fichier) {
        for (Robot robot : robots) {
            if (robot.aFichier() && robot.getFichierEnMain() == fichier) {
                return true;
            }
        }
        return false;
    }

    public void ajouterRobot(Robot r) {
        robots.add(r);
    }

    public void ajouterFichier(Fichier f) {
        fichiers.add(f);
    }

    /************************************************************************************************* */

    // Méthode pour déplacer le robot vers une porte numérotée
    public void deplacerRobotVersPorte(int id, int porteNumero) {
        Robot r = getRobotId(id);
        if (r != null) {
            Integer[] destination = destinationsPortes.get(porteNumero);
            if (destination != null) {
                r.moveTo(destination[0], destination[1]);
            } else {
                System.out.println("Destination de la porte non trouvée.");
            }
        } else {
            System.out.println("Robot non trouvé.");
        }
    }

     
    private int territoireToPorteNumero(TypeTerritoire territoire) {
        switch (territoire) {
            case PORTE2: return 2;
            case PORTE3: return 3;
            case PORTE4: return 4;
            case PORTE5: return 5;
            default: return -1; 
        }
    }
    

    ////////////////////LINK //////////////////////////
    public void traiterCommandeLink(int id, String direction) {
        Robot r = getRobotId(id);
        if (r == null || !r.estActif()) {
            System.out.println("Robot non trouvé.");
            return;
        }
        System.out.println("Le robot est dans la case(" + r.getPositionX() + "," + r.getPositionY() + ") avant de bouger ");
        int newX = r.getPositionX();
        int newY = r.getPositionY();
        // Calculer les nouvelles coordonnées en fonction de la direction
        switch (direction.toLowerCase()) {
            case "left":
                newX -= 1;
                break;
            case "right":
                newX += 1;
                break;
            case "up":
                newY -= 1;
                break;
            case "down":
                newY += 1;
                break;
            default:
                System.out.println("Direction invalide.");
                return;
        }
        // Vérifier si les nouvelles coordonnées sont dans les limites de la grille
        if (newX < 0 || newX >= 5 || newY < 0 || newY >= 5) {
            System.out.println("Le robot ne peut pas passer dans cette direction car il sortirait de la grille.");
            return;
        }
        System.out.println("Le robot est sensé passer vers la case(" + newX + "," + newY + ") ");
        TypeTerritoire territoire = getTerritoire(newX, newY);
    
        if (estZoneAccessible(newX, newY)) {
            r.moveTo(newX, newY);
            System.out.println("Le robot a passé vers la case (" + newX + "," + newY + ").");
        } else if (estPorteNumerotee(newX, newY)) {
            int porteNumero = territoireToPorteNumero(territoire);
            System.out.println("LA PORTE NUMERO" + porteNumero);
            if (porteNumero >= 0) {
                // Ajustez l'affichage du numéro de porte ici si nécessaire
                System.out.println("Le robot a passé par la porte numéro " + (porteNumero - 2) + ".");
                deplacerRobotVersPorte(id, porteNumero);
            } else {
                System.out.println("Le robot est à une porte, mais le numéro de porte est invalide.");
            }
        } else {
            System.out.println("Le robot ne peut pas passer dans cette direction à cause d'un obstacle.");
        }
    }
    

    
    ///////////////////////GRAB///////////////
    public void GRAB(int id, int fichierId) {
        Robot r = getRobotId(id);
        if (r == null || !r.estActif()) {
            System.out.println("Robot non trouvé.");
            return;
        }
        
        Fichier fichier = getFichierParId(fichierId);
        if (fichier == null) {
            System.out.println("Le fichier avec l'identifiant " + fichierId + " n'existe pas.");
            return;
        }
    
        if (r.getPositionX() != fichier.getPosX() || r.getPositionY() != fichier.getPosY()) {
            System.out.println("Le robot doit être sur la même case que le fichier pour le saisir.");
            return;
        }
    
        if (r.getFichierEnMain() != null) {
            System.out.println("Le robot détient déjà un fichier.");
            return;
        }
        r.setFichierEnMain(fichier);
        fichier.setEstTenuParRobot(true); 
        retirerFichier(fichier); 
        System.out.println("Le fichier avec l'identifiant " + fichierId + " a été saisi par le robot.");
    }
    
    ///////////////la commande DROP/////////
    public void DROP(int id) {
        Robot r = getRobotId(id);
        if (r == null || !r.estActif()) {
            System.out.println("Robot non trouvé.");
            return;
        }
        
        Fichier fichier = r.getFichierEnMain();
        if (fichier == null) {
            System.out.println("Le robot ne détient aucun fichier à déposer.");
            return;
        }
    
        int x = r.getPositionX();
        int y = r.getPositionY();
    
        if (!estZoneAccessible(x, y) || estPorteNumerotee(x, y)) {
            System.out.println("Le robot ne peut pas déposer de fichier sur cette case.");
            return;
        }

        ajouterFichier(fichier, x, y);
        r.setFichierEnMain(null); 
        fichier.setEstTenuParRobot(false);
        System.out.println("Le fichier a été déposé sur la case (" + x + ", " + y + ").");
    }
    

  
    
    //////////// add /////////////////// 
    private int obtenirValeur(Robot r, String input) throws NumberFormatException {
        // Essayez de convertir directement input en int
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // Si ce n'est pas un nombre, vérifiez s'il s'agit d'une case mémoire ou d'un fichier
            switch (input) {
                case "X":
                    return (Integer) r.getCaseMemoire(0);
                case "T":
                    return (Integer) r.getCaseMemoire(1);
                case "M":
                    return (Integer) r.getRegistreM();
                case "F":
                    if (r.getFichierEnMain() != null) {
                        String contenu = r.getFichierEnMain().retirerContenu();
                        if (contenu != null) {
                            try {
                                return Integer.parseInt(contenu);
                            } catch (NumberFormatException ex) {
                                System.err.println("Erreur lors de la conversion du contenu du fichier en nombre.");
                                throw ex;
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Input invalide.");
                    throw new NumberFormatException("Input invalide");
            }
        }
        return 0;
    }
    
    private void appliquerResultat(Robot r, int resultat, String destination, GameWindow gameWindow) {
        switch (destination) {
            case "X":
                r.setCaseMemoire(0, resultat);
                gameWindow.getTextZone().setMemoryArea1Text(String.valueOf(resultat));
                break;
            case "T":
                r.setCaseMemoire(1, resultat);
                gameWindow.getTextZone().setMemoryArea2Text(String.valueOf(resultat));
                break;
            case "M":
                r.setRegistreM(resultat);
                gameWindow.getTextZone().setMemoryArea4Text(String.valueOf(resultat));
                break;
            case "F":
                if (r.getFichierEnMain() != null) {
                    Fichier fichierDestination = r.getFichierEnMain();
                    fichierDestination.ajouterContenu(String.valueOf(resultat));
                    fichierDestination.afficherContenu();
                } else {
                    System.out.println("Aucun fichier en main pour copier le résultat.");
                }
                break;
            default:
                System.out.println("Destination invalide.");
        }
        System.out.println("Résultat de l'opération: " + resultat);
    }
    
    public void add(int id, String valeur1, String valeur2, String destination, GameWindow gameWindow) {
        Robot r = getRobotId(id);
        if (r == null || !r.estActif()) {
            System.out.println("Robot non trouvé.");
            return;
        }
    
        try {
            int nombre1 = obtenirValeur(r, valeur1);
            int nombre2 = obtenirValeur(r, valeur2);
            int resultat = nombre1 + nombre2;
    
            appliquerResultat(r, resultat, destination, gameWindow);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'addition : " + e.getMessage());
        }
    }
    
    
    
    
    // Méthode pour convertir une lettre en indice numérique correspondant
    private int convertirLettreEnIndice(String lettre) {
        lettre = lettre.toUpperCase();
        switch (lettre) {
            case "X":
                return 0;
            case "T":
                return 1;
            default:
                return -1; 
        }
    }

  ////////////////////////////SUBI/////////////////////////////
  public void sub(int id, String valeur1, String valeur2, String destination, GameWindow gameWindow) {
    Robot r = getRobotId(id);
    if (r == null || !r.estActif()) {
        System.out.println("Robot non trouvé.");
        return;
    }

    try {
        int nombre1 = obtenirValeur(r, valeur1);
        int nombre2 = obtenirValeur(r, valeur2);
        int resultat = nombre1 - nombre2;

        appliquerResultat(r, resultat, destination, gameWindow);
    } catch (Exception e) {
        System.err.println("Erreur lors de la soustraction : " + e.getMessage());
    }
}


    ////////////////////////MULTI///////////////////
    public void mult(int id, String valeur1, String valeur2, String destination, GameWindow gameWindow) {
    Robot r = getRobotId(id);
    if (r == null || !r.estActif()) {
        System.out.println("Robot non trouvé.");
        return;
    }

    try {
        int nombre1 = obtenirValeur(r, valeur1);
        int nombre2 = obtenirValeur(r, valeur2);
        int resultat = nombre1 * nombre2;

        appliquerResultat(r, resultat, destination, gameWindow);
    } catch (Exception e) {
        System.err.println("Erreur lors de la multiplication : " + e.getMessage());
    }
}





    //////////////////la commande copy///////////////////////
    public void copy(int id, String source, String destination,GameWindow gameWindow) {
    Robot r = getRobotId(id);
    if (r == null || !r.estActif()) {
        System.out.println("Robot non trouvé.");
        return;
    }

    try {
        int valeur = 0;
        boolean sourceValid = false;

        switch (source) {
            case "X":
                valeur = (Integer) r.getCaseMemoire(0);
                sourceValid = true;
                break;
            case "T":
                valeur = (Integer) r.getCaseMemoire(1);
                sourceValid = true;
                break;
            case "M":
                valeur = (Integer) r.getRegistreM();
                sourceValid = true;
                break;
            case "F":
                if (r.getFichierEnMain() != null) {
                    Fichier fichierSource = r.getFichierEnMain();
                    String Val = fichierSource.retirerContenu();
                    if (Val != null) {
                        try {
                            valeur = Integer.parseInt(Val);
                            sourceValid = true;
                            fichierSource.afficherContenu();
                        } catch (NumberFormatException e) {
                            System.err.println("Erreur lors de la conversion du contenu du fichier en nombre.");
                            sourceValid = false;
                        }
                    }
                }
                break;
            default:
                System.out.println("Source invalide.");
                return;
        }

        switch (destination) {
            case "X":
            final int valeurFinale = valeur;
                r.setCaseMemoire(0, valeur);
                gameWindow.getTextZone().setMemoryArea1Text(String.valueOf(valeur));
                System.out.println("Valeur copiée dans la case mémoire X : " + valeur);
                break;
            case "T":
                r.setCaseMemoire(1, valeur);
                System.out.println("Valeur copiée dans la case mémoire T : " + valeur);
                gameWindow.getTextZone().setMemoryArea2Text(String.valueOf(valeur));
                break;
            case "M":
            gameWindow.getTextZone().setMemoryArea4Text(String.valueOf(valeur));
                r.setRegistreM(valeur);
                System.out.println("Valeur copiée dans le registre M: " + valeur);
                break;
            case "F":
                if (r.getFichierEnMain() != null) {
                    Fichier fichierDestination = r.getFichierEnMain();
                    fichierDestination.ajouterContenu(String.valueOf(valeur));
                    System.out.println("Valeur copiée dans le fichier ID: " + fichierDestination.getId() + ": " + valeur);
                    fichierDestination.afficherContenu();
                } else {
                    System.out.println("Aucun fichier en main pour la copie.");
                }
                break;
            default:
                System.out.println("Destination invalide.");
        }
    } catch (Exception e) {
        System.err.println("Erreur lors de la copie : " + e.getMessage());
    }
}
     /////////////commande DEVI////////////////
    public void devi(int id, String valeur1, String valeur2, String destination, GameWindow gameWindow) {
    Robot r = getRobotId(id);
    if (r == null || !r.estActif()) {
        System.out.println("Robot non trouvé ou inactif.");
        return;
    }

    try {
        int nombre1 = obtenirValeur(r, valeur1);
        int nombre2 = obtenirValeur(r, valeur2);

        if (nombre2 == 0) {
            throw new ArithmeticException("Tentative de division par zéro.");
        }

        int resultat = nombre1 / nombre2;

        appliquerResultat(r, resultat, destination, gameWindow);
    } catch (ArithmeticException e) {
        System.err.println("Erreur lors de la division : " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erreur inattendue lors de la division : " + e.getMessage());
    }
}
 /////////////traitemant de lacommande MODI////////////////
    public void modi(int id, String valeur1, String valeur2, String destination, GameWindow gameWindow) {
    Robot r = getRobotId(id);
    if (r == null || !r.estActif()) {
        System.out.println("Robot non trouvé ou inactif.");
        return;
    }

    try {
        int nombre1 = obtenirValeur(r, valeur1);
        int nombre2 = obtenirValeur(r, valeur2);

        if (nombre2 == 0) {
            throw new ArithmeticException("Tentative de modulo par zéro.");
        }

        int resultat = nombre1 % nombre2;

        appliquerResultat(r, resultat, destination, gameWindow);
    } catch (ArithmeticException e) {
        System.err.println("Erreur lors du modulo : " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erreur inattendue lors du modulo : " + e.getMessage());
    }
}


   // Méthode pour traiter la commande TEST EOF
    public void traiterCommandeTestEOF(int id) {
    Robot r = getRobotId(id);
    if (r.getFichierEnMain() != null) {
        if (r.getFichierEnMain().estVide()) {
            System.out.println("Fin de fichier atteinte.");
        } else {
            System.out.println("Encore des données à lire.");
        }
    } else {
        System.out.println("Aucun fichier en possession.");
    }
}
   /////////////commande NOOP////////////////
    public void NOOP() {
    try {
        // Attend pendant une durée définie, par exemple 1000 millisecondes (1 seconde)
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Restaure l'état d'interruption
        System.err.println("Le thread a été interrompu pendant l'attente de NOOP");
    }
}
    //////////////////Commande HALT/////////////////////
    public void traiterCommandeHalt(int id,GameWindow window) {
    Robot r = getRobotId(id);
    if (r != null  && r.estActif()) {
        r.halt();
        r.setPositionX(-1);
        r.setPositionY(-1);

        window.getGamePanel().rafraichirAffichage(); // Met à jour l'affichage
    } else {
        System.out.println("Robot avec l'ID " + id + " non trouvé.");
    }
    }

    public void traiterCommandeMake(int idRobot, int id) {
        Robot robot = getRobotId(idRobot);
        if (robot != null && robot.estActif()) {
            Fichier nouveauFichier = new Fichier(id,robot.getPositionX(),robot.getPositionY());
            
            
            System.out.println("Le robot avec l'ID " + idRobot + " a créé un fichier nommé " + id + ".");
        } else {
            System.out.println("Robot avec l'ID " + idRobot + " non trouvé ou inactif.");
        }
    }
}

