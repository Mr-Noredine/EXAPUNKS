package model;

import java.util.ArrayList;

public class Instruction {
	private String motCommande;
	private String secondMot;
    private String thirdMot;
    private String fourthMot;


	
	public Instruction(String motCommande, String secondMot, String thirdMot, String fourthMot) {
		this.motCommande = motCommande;
		this.secondMot = secondMot;
        this.thirdMot = thirdMot;
        this.fourthMot = fourthMot;
	}

	public String[] getArguments() {

        ArrayList<String> args = new ArrayList<>();
        
        if (this.secondMot != null) args.add(this.secondMot);
        if (this.thirdMot != null) args.add(this.thirdMot);
        if (this.fourthMot != null) args.add(this.fourthMot);
  
        return args.toArray(new String[0]);
    }
    /**
	 *  Renvoie le mot commande (le premier mot) de cette Commande. Si cette
	 *  commande n'est pas une commande valide, la valeur renvoyée est null.
	 */
	public String getMotCommande() {
		return motCommande;
	}

    /**
	 *  Renvoie le second mot de cette Commande ou null si cette commande ne
	 *  possède pas de second mot.
     */
	public String getSecondMot() {
		return secondMot;
	}
    	
    /**
	 *  Renvoie le 3éme mot de cette Commande ou null si cette commande ne
	 *  possède pas de 3éme mot.
     */
	public String getThirdMot() {
		return thirdMot;
	}

     /**
	 *  Renvoie le 4éme mot de cette Commande ou null si cette commande ne
	 *  possède pas de 4éme mot.
     */
	public String getFourthMot() {
		return fourthMot;
	}

	/**
	 *  Teste si cette commande est une commande reconnue par le jeu.
	 *
	 * @return    true si cette commande est valide ; false sinon
	 */
	public boolean estInconnue() {
		return (motCommande == null);
	}


	/**
	 *  Teste si cette commande possède un second mot.
	 */
	public boolean aSecondMot() {
		return (secondMot != null);
	}

    /**
	 *  Teste si cette commande possède un 3éme mot.
	 */
    public boolean aThirdMot() {
		return (thirdMot != null);
	}

    /**
	 *  Teste si cette commande possède un 4éme mot.
	 */
    public boolean aFourthMot() {
		return (fourthMot != null);
	}
   
	public String toString() {
		if (estInconnue()) {
			return " Commande inconnue !";
		}

		String result = "Commande: " + getMotCommande();
		if (aSecondMot()) {
			result += " ,arg1: " + getSecondMot();
		}

		if (aThirdMot()) {
			result += " ,arg2: " + getThirdMot();
		}
	
		if (aFourthMot()) {
			result += " ,arg3: " + getFourthMot();
		}

		return result;
	}

}
