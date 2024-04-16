package main;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;

import java.util.Scanner;

import model.*;

public class TestPartieTextuelle {
    public static void main(String[] args) throws java.io.IOException {
        
       
        String texte = ""; 
        String ligne = "";


        System.out.println("Entrer les instructions séparer avec des saut-ligne \"\\n\" (pour arreter entrer <stop>):\n");
		System.out.print("> ");

		Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\\n");

            while (scanner.hasNextLine()) {
                ligne = scanner.next();
                if ("stop".equals(ligne)) {
                    break;
                }
                texte += ligne + "\n";
            }
        scanner.close();
        

        TextSplitter splitter = new TextSplitter(texte);
        
         
        for(Instruction e : splitter.textProcessor()) {
            System.out.println(e.toString());
        };

        
    }
}
