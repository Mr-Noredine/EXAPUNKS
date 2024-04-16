package model;


public class Niveau1 extends Niveau {

    private int nbNiveau;
    private int  nbRobot;

    public Niveau1() {
        super();
        this.nbRobot = 1;
        this.nbNiveau = 1;
        
        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();
        
        grille = new Grille(robotsLancement, fichiersLancement);
        for (Fichier fichier : fichiersLancement) {
            if (fichier.getId() == 200) {
                fichier.afficherContenu();
                break; 
            }
        }
    }

    public String getDescription() {
        String description = " Move file 200 to the position (3, 4). \n";
        description += "This task, requires you to leave no trace," +
        "and not make any changes to the network other than those specified !";

        return description;
    }

    public String getMission() {
        String mission = " -> Move file 200 to the position (3, 4) \n";
        mission += "Leave no trace";

        return mission;
    }

    public boolean testVectoire() {
        if ((grille.getFichierParId(200).getPosX() != 3 ) && (grille.getFichierParId(200).getPosY() != 4)) {
            return false;
        }
        
        if (grille.getListeRobots().size() != 0) {
            return false;
        }

        return true;
    }

    public void ajouterSolution() {
        

    }


    public void InitialiserFichierLancement() {

        Fichier fichier200 = new Fichier(200, 4, 0);
        
    
        String[] StringFichier200 = {"MOVE", "THIS", "FILE", "TO", "THE ", "OUTBOX"};
        for (String e : StringFichier200) {
            fichier200.ajouterContenu(e);
        }

        fichiersLancement.add(fichier200);
    }


    public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 0, 0));  
    }


    @Override
    public int getNbSolution() {
        return nbSolutions;
    }

    @Override
    public int getNbNiveau() {
        return nbNiveau;
    }

    @Override
    public int getNbRobot() {
        return nbRobot;
    }

}