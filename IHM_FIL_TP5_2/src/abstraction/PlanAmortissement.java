package abstraction;

import java.util.Observable;

public class PlanAmortissement extends Observable {
	private double revenus;    // le revenu mensuel
	private double capital;    // le capital emprunte
	private int nbMensualites; // le nombre de mensualites
	private double montantMensualites; // le montant des mensualites

	private double[] taux = {0.0348, 0.038, 0.0412, 0.0452}; 

	public PlanAmortissement(double revenus) {
		this.revenus = revenus;
		this.setNbMensualites(360);
		this.setCapital(10000); // par defaut le capital emprunte est 10000
	}
	public double getCapital() {
		return this.capital;
	}
	public double getNbMensualites() {
		return this.nbMensualites;
	}
	public double getMontantMensualites() {
		return this.montantMensualites;
	}
	public double getRevenus() {
		return this.revenus;
	}
	public void setNbMensualites(int nb) {
		this.nbMensualites = nb;
		if (this.getCapital()>this.getMontantEmpruntable()) {
			this.setCapital(  this.getMontantEmpruntable() );
		}
		this.setChanged();
		this.notifyObservers();
	}
	public void setCapital( double c) {
		if (c>this.getMontantEmpruntable()) {
			c=this.getMontantEmpruntable();
		}
		this.capital = c;
		double m = this.getMensualite();
		if (m>this.getMensualiteMax() && this.nbMensualites<360) {
			this.nbMensualites+=(5*12);
			m = this.getMensualite();
			if (m>this.getMensualiteMax() && this.nbMensualites<360) {
				this.nbMensualites+=(5*12);
				m = this.getMensualite();
				if (m>this.getMensualiteMax() && this.nbMensualites<360) {
					this.nbMensualites+=(5*12);
					m = this.getMensualite();
				}
			}
		}
		this.setChanged();
		this.notifyObservers();
	}

	public void setRevenus(double revenus) {
	//	System.out.println("set "+revenus);
		this.revenus = revenus;
	//	System.out.println("empruntable : "+this.getMontantEmpruntable());
		if (this.getCapital()>this.getMontantEmpruntable()) {
			this.setCapital(  this.getMontantEmpruntable() );
	//		System.out.println("set capital "+ this.getMontantEmpruntable());
		}
		
		this.setChanged();
		this.notifyObservers();
	}

	public double getMensualiteMax() {
		return this.getRevenus()/3.0;
	}
	public double getMensualite() {
		return ((this.getCapital()*this.getTauxPeriodique()*Math.pow(1+this.getTauxPeriodique(), this.getNbMensualites()))) / (Math.pow(1+this.getTauxPeriodique(), this.getNbMensualites())-1);
	}
	/**
	 * Retourne true si il est possible d'etaler l'emprunt sur nbMensualites
	 * @param nbMensualites
	 * @return  true si il est possible d'etaler l'emprunt sur nbMensualites
	 */
	public boolean possible(int nbMensualites) {
		double mensualite = ((this.getCapital()*this.getTauxPeriodique()*Math.pow(1+this.getTauxPeriodique(), nbMensualites))) / (Math.pow(1+this.getTauxPeriodique(), nbMensualites)-1);
		return mensualite<=this.getMensualiteMax();
	}
	/**
	 * Retourne le montant maximal empruntable en fonction du nombre de mensualites fixe.
	 * @return
	 */
	public double getMontantEmpruntable() {
		double montant = 10000;
		double mMax = this.getMensualiteMax();
	//	System.out.println("mens max : "+mMax);
		double mensualite;
		do {
			mensualite = ((montant*this.getTauxPeriodique()*Math.pow(1+this.getTauxPeriodique(), this.getNbMensualites()))) / (Math.pow(1+this.getTauxPeriodique(), this.getNbMensualites())-1);
			if (mensualite>mMax) {
				montant = montant-1;
			}
			else {
				montant = montant+1;
			}
		} while (Math.abs(mensualite-mMax)>1.0);
		return montant; 
	}
	/**
	 * Retourne le taux d'interets mensuel
	 * @return  le taux d'interets mensuel
	 */
	public double getTauxPeriodique() {
		double tx;
		if (this.nbMensualites<=(12*15)) {
			tx = taux[0];
		}
		else {
			if (this.nbMensualites<=(12*20)) {
				tx = taux[1];
			}
			else {
				if (this.nbMensualites<=(12*25)) {
					tx = taux[2];
				}
				else {
					tx = taux[3];
				}
			}
		}
		return  tx/12;
	}
	/**
	 * Methode utilisee par getPlan pour obtenir un affichage convenable des nombres
	 * @param val
	 * @param nbCaracteres
	 * @return
	 */
	public String ajuste(double val, int nbCaracteres) {
		String s = ""+   ((int)(val*100))/100.0;
		while (s.length()<nbCaracteres) {
			s = " "+s;
		}
		return s;
	}

/**
 * Retourne  le plan d'amortissement  du pret sous format html
 * @return  le plan d'amortissement  du pret sous format html
 */
	public String getPlan() {
	//	System.out.println("getPlan : nb mens = "+ this.nbMensualites);
		String res="<html><table border=\"1\">";
		double capitalRestantDu = this.getCapital();
		int nbM = this.nbMensualites;
		double m = this.getMensualite();
		double interets;
		res=res+"<tr><td> mois </td><td>restant du</td><td>interets</td><td>principal</td><td>mensualite</td></tr>";
		for (int i=0; i<nbM; i++) {
			interets = capitalRestantDu * this.getTauxPeriodique();
			res = res +"<tr><td>"+(i+1)+"</td><td>"
			                      +ajuste(capitalRestantDu,10)+"</td><td>"
					              +ajuste(interets,7)+"</td><td> "
			                      +ajuste(this.getMensualite()-interets,7)+"</td><td> "
					              +ajuste(m,7)+"</td></tr>";
			capitalRestantDu = capitalRestantDu + interets - m;
		}
		res=res+"</table> cout total du pret : "+((int)(100*nbM*this.getMensualite())/100.0);
		return res+"</html>";
	}
}
