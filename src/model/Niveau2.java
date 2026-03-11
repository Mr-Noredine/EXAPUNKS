package model;

import java.util.List;

public class Niveau2 extends Niveau {

    private int nbNiveau;
    private int nbRobot;

    public Niveau2() {
        super();
        this.nbRobot = 1;
        this.nbNiveau = 2;

        ajouterSolution();
        InitialiserFichierLancement();
        InitialiserRobotLancement();

        TypeTerritoire[][] layout = {
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE},
            {TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE, TypeTerritoire.LIBRE, TypeTerritoire.OCCUPE},
            {TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE, TypeTerritoire.LIBRE}
        };
        grille = new Grille(robotsLancement, fichiersLancement, layout);
    }

    @Override
    public String getDescription() {
        return "Quadrupler la valeur de F200 et deposer le resultat en OUTBOX.";
    }

    @Override
    public String getMission() {
        return "-> Robot en (4,0) | Atteindre F200 (0,4) | Multiplier par 4 | OUTBOX (4,4)";
    }

    @Override
    public boolean testVictoire() {
        Fichier f200 = grille.getFichierParId(200);
        if (f200 == null || f200.getPosX() != 4 || f200.getPosY() != 4) {
            return false;
        }

        List<String> content = f200.getGestionFichier().getContenuCommeListe();
        boolean found = false;
        for (String s : content) {
            if (s.equals("24")) { found = true; break; }
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
        Fichier fichier200 = new Fichier(200, 0, 4);
        fichier200.ajouterContenu("6");
        fichiersLancement.add(fichier200);
    }

        public void InitialiserRobotLancement() {
        robotsLancement.add(new Robot(1, 4, 0));
    }

    @Override
    public int getTargetX() { return 4; }

    @Override
    public int getTargetY() { return 4; }

    @Override
    public int getNbSolution() { return nbSolutions; }

    @Override
    public int getNbNiveau() { return nbNiveau; }

    @Override
    public int getNbRobot() { return nbRobot; }
}
