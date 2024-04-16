package model; 

public enum TypeTerritoire {
    LIBRE(0),
    OCCUPE(1),
    PORTE2(2),
    PORTE3(3),
    PORTE4(4),
    PORTE5(5);
    
    private final int valeur;

    TypeTerritoire(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return this.valeur;
    }

    public static TypeTerritoire fromInt(int valeur) {
        for (TypeTerritoire territoire : TypeTerritoire.values()) {
            if (territoire.getValeur() == valeur) {
                return territoire;
            }
        }
        return LIBRE; // Retour par défaut
    }
}