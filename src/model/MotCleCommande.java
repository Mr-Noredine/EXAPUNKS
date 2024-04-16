package model;
public class MotCleCommande {
	/**
	 *  Un tableau constant contenant tous les mots-clé valides comme commandes.
	 */
	private final static String commandesValides[] = {"LINK", "COPY", "GRAB", "DROP", "ADDI", "SUBI", "MULI", "HALT", "JUMP", "FJUMP", "TEST"};


	/**
	 *  Initialise la liste des mots-clé utilisables comme commande.
	 */
	public MotCleCommande() { }


	/**
	 *  Teste si la chaine de caractères spécifiée est un mot-clé de commande valide. 
	 */
	public boolean estCommande(String aString) {
		for (int i = 0; i < commandesValides.length; i++) {
			if (commandesValides[i].equals(aString)) {
				return true;
			}
		}
		return false;
	}


	/**
	 *  Affiche toutes les commandes (i.e. les mots-clé) valides.
	 */
	public void afficherToutesLesCommandes() {
		for (int i = 0; i < commandesValides.length; i++) {
			System.out.print(commandesValides[i] + "  ");
		}
		System.out.println();
	}
}

