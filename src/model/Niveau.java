package model;
import java.util.ArrayList;

public abstract class Niveau{

    public Grille grille;
    public ArrayList<Instruction> solution;
    public ArrayList<Robot> robotsLancement;
    public ArrayList<Fichier> fichiersLancement;
    public int nbSolutions;

    public Niveau() {
        nbSolutions = 0;
        robotsLancement = new ArrayList<>();
        fichiersLancement = new ArrayList<>();
        solution = new ArrayList<>();

    }



    public boolean est_solution_valide(ArrayList<Instruction> userSolution){
        if (!(getSolution().size() == userSolution.size())) {
            return false;
        }
        for (int i = 0 ; i < getSolution().size() ; i++) {
            if (!getSolution().get(i).equals(userSolution.get(i))) {
                return false;
            }
        }

        return true;
    }


    public ArrayList<Instruction> getSolution() {
        ArrayList<Instruction> result = new ArrayList<>(solution);
        return result;
    }

    public ArrayList<Robot> getListRobotsLancemet() {
        ArrayList<Robot> result = new ArrayList<>(robotsLancement);
        return result;
    }

    public ArrayList<Fichier> getListFichiersLancement() {
        ArrayList<Fichier> result = new ArrayList<>(fichiersLancement);
        return result;
    }

    public Grille getGrille() {
        return grille;
    }


    public abstract int getNbSolution();
    public abstract int getNbNiveau();
    public abstract int getNbRobot();
    public abstract String getDescription();
    public abstract String getMission();
    public abstract boolean testVectoire();
    
    public void setNbSolution() {
        nbSolutions++;
    }
}