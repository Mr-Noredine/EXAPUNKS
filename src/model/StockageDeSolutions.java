package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StockageDeSolutions {
    private String filePath;
    private String code;
    private Niveau niveau;

    public StockageDeSolutions(String code, Niveau niveau) {
        this.niveau = niveau;
        this.code = code;
        niveau.setNbSolution();
        filePathInit();
        CreationFichier();
        EcritureDeSolution();
    }


    public void filePathInit() {
        if (niveau.getNbNiveau() == 1) {
            filePath = "src/assets/solutions/niveau1/sol"+  String.valueOf(niveau.getNbSolution()) + ".txt";
        }
        if (niveau.getNbNiveau() == 2) {
            filePath = "src/assets/solutions/niveau2/sol"+  String.valueOf(niveau.getNbSolution()) + ".txt";
        }
        if (niveau.getNbNiveau() == 3) {
            filePath = "src/assets/solutions/niveau3/sol"+  String.valueOf(niveau.getNbSolution()) + ".txt";
        }
    }

    public void CreationFichier() {
        try {
            File monFichier = new File(filePath);
            
            if (monFichier.createNewFile()) {
                System.out.println("Fichier crée : "+ monFichier.getName());
            }
            else {
                System.out.println("Le fichier existe déjà.");
            }
        } catch (IOException e) {
            System.out.println("Une erreur est survenue.");
            e.printStackTrace();
        }
    }

    public void EcritureDeSolution() {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(code);
            writer.close();
            System.out.println("le fichier a été crée et écrit avec succès. ");
        } catch (IOException e) {
            System.out.println("Une erreur est survenue. ");
            e.printStackTrace();
        }
    }
}
