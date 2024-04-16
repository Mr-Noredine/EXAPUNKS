package model;


public class Case {
    private int x;
    private int y;
    private TypeCase type;
    private boolean hasRobot; 
    private boolean hasFichier;
    private Fichier fichier; 

    public Case(int x, int y, TypeCase type) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Les coordonnées ne peuvent pas être négatives.");
        }
        if (type == null) {
            throw new NullPointerException("Le type de case ne peut pas être null.");
        }
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public TypeCase getType() {
        return type;
    }

    public void setFichier(Fichier fichier) {
        this.fichier = fichier;
    }

    public Fichier getFichier() {
        return fichier;
    }
    
        public int getX() {
            return x;
        }
    
        public int getY() {
            return y;
        }

        public boolean hasRobot() {
            return hasRobot;
        }

        public boolean hasFichier(){
            return hasFichier;
        }

        public void setHasRobot(boolean hasRobot) {
            this.hasRobot = hasRobot;
        }
    
    public  enum TypeCase {
        LIBRE, OCCUPEE, PORTE
    }
}
    

