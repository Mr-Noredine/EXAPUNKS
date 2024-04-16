package model;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.LinkedList;


public class ArrayListFile {
    private File file;
    private int pointer = 0;      // Le pointeur  au début de mon fichier 


public ArrayListFile(String filePath) {
        this.file = new File(filePath);
    }

 // Ajoute un élément à la fin du fichier(la fin de mon tableau dynamique 
    public void add(String element) {
        try (FileWriter fw = new FileWriter(this.file, true);
             PrintWriter out = new PrintWriter(fw)) {
            out.println(element);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// Supprime l'élément à la position du pointeur et décale les éléments suivants
    public void remove() {
    
        LinkedList<String> lines = new LinkedList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
        
            String line;
            // je vais ajoutée tout les lignes dans ma liste 
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            
            if (pointer >= 0 && pointer < lines.size()) {
                lines.remove(pointer);
            }

            // Réécrit le fichier sans l'élément supprimé
            try (PrintWriter writer = new PrintWriter(this.file)) {
                for (String l : lines) {
                    writer.println(l);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 
    
    
    // Déplace le pointeur
    public void seek(int position) {
        this.pointer = position;
    }
    
    
    
    // Supprime le dernier élément du fichier
    public void voidF() {
        LinkedList<String> lines = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            // Supprime le dernier élément si la liste n'est pas vide
            if (!lines.isEmpty()) {
                lines.removeLast();
            }

            // Réécrit le fichier sans le dernier élément
            try (PrintWriter writer = new PrintWriter(this.file)) {
                for (String l : lines) {
                    writer.println(l);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

    


 
