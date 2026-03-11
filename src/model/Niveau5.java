package model;

import java.util.List;

public class Niveau5 extends Niveau {

    private int nbNiveau;
    private int nbRobot;

    public Niveau5() {
        super();
        this.nbRobot = 1;
        this.nbNiveau = 5;

        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();

        TypeTerritoire[][] layout = {
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE}
        };
        grille = new Grille(robotsLancement, fichiersLancement, layout);
    }

    @Override
    public String getDescription() {
        return "Sommer les 4 valeurs de F200 (3+7+12+8=30) et deposer le resultat en OUTBOX.";
    }

    @Override
    public String getMission() {
        return "-> Sommer les 4 valeurs de F200 (3+7+12+8) | Deposer 30 en OUTBOX (0,4)";
    }

    @Override
    public boolean testVictoire() {
        Fichier f200 = grille.getFichierParId(200);
        if (f200 == null || f200.getPosX() != 0 || f200.getPosY() != 4) {
            return false;
        }

        List<String> content = f200.getGestionFichier().getContenuCommeListe();
        boolean found = false;
        for (String s : content) {
            if (s.equals("30")) { found = true; break; }
        }
        if (!found) return false;

        for (Robot r : grille.getListeRobots()) {
            if (r.estActif()) return false;
        }
        return true;
    }

        public void ajouterSolution() {
        // no predefined solution
    }

        public void InitialiserFichierLancement() {
        Fichier fichier200 = new Fichier(200, 4, 0);
        fichier200.ajouterContenu("3");
        fichier200.ajouterContenu("7");
        fichier200.ajouterContenu("12");
        fichier200.ajouterContenu("8");
        fichiersLancement.add(fichier200);
    }

        public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 0, 0));
    }

    @Override
    public int getTargetX() { return 0; }

    @Override
    public int getTargetY() { return 4; }

    @Override
    public int getNbSolution() { return nbSolutions; }

    @Override
    public int getNbNiveau() { return nbNiveau; }

    @Override
    public int getNbRobot() { return nbRobot; }
}
