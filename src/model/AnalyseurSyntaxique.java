package model;
import java.util.StringTokenizer;


public class AnalyseurSyntaxique {

	// répertorie toutes les commandes reconnues
	private MotCleCommande commandes;


	/**
	 *  Initialise un nouvel analyseur syntaxique
	 */
	public AnalyseurSyntaxique() {
		commandes = new MotCleCommande();
	}


	
	public Instruction getCommande(String ligne) {
		
		String mot1;
		String mot2;
        String mot3;
        String mot4;


		StringTokenizer tokenizer = new StringTokenizer(ligne);

		if (tokenizer.hasMoreTokens()) {
			// récupération du permier mot (le mot commande)
			mot1 = tokenizer.nextToken();
		} else {
			mot1 = null;
		}
		if (tokenizer.hasMoreTokens()) {
			// récupération du second mot
			mot2 = tokenizer.nextToken();
		} else {
			mot2 = null;
		}
        if (tokenizer.hasMoreTokens()) {
			// récupération du 3éme mot
			mot3 = tokenizer.nextToken();
		} else {
			mot3 = null;
		}
        if (tokenizer.hasMoreTokens()) {
			// récupération du 4éme mot
			mot4 = tokenizer.nextToken();
		} else {
			mot4 = null;
		}

		// note: le reste de la ligne est ignoré.

		// Teste si le permier mot est une commande valide, si ce n'est pas
		// le cas l'objet renvoyé l'indique
		if (commandes.estCommande(mot1)) {
			return new Instruction(mot1, mot2, mot3, mot4);
		} else {
			return new Instruction(null, mot2, mot3, mot4);
		}
	}


	/**
	 *  Affiche la liste de toutes les commandes reconnues pour le jeu.
	 */
	public void afficherToutesLesCommandes() {
		commandes.afficherToutesLesCommandes();
	}
}
