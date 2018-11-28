package abstraction;

import java.util.Observable;

public class Panier extends Observable {
	private Catalogue catalogue;
	private int[] quantite; // pour chaque article, la quantite actuellement dans le panier

	public Panier(Catalogue catalogue) {
		this.catalogue = catalogue;
		this.quantite = new int[ this.catalogue.getSize()];
	}
	public void vider() {
		for (int i=0; i<this.quantite.length; i++) {
			this.quantite[i]=0;
		}
		this.setChanged();
		this.notifyObservers();
	}
	public double getTotal() {
		double total = 0.0;
		for (int i=0; i<this.quantite.length; i++) {
			total += this.quantite[i]*this.catalogue.get(i).getPrix();
		}
		return ((int)(total*100))/100.0;
	}
	public void setNbArticles(Article a, int nb) {
		if (this.catalogue.getIndex(a)>=0 && this.catalogue.getIndex(a)<this.catalogue.getSize()) {
			this.quantite[ this.catalogue.getIndex(a) ]=nb;
			this.setChanged();
			this.notifyObservers();
		}
	}
	public int getNbArticles(Article a) {
		return this.quantite[ this.catalogue.getIndex(a) ];
	}
	public int getNbTotalArticles() {
		int total = 0;
		for (int i=0; i<this.quantite.length; i++) {
			total += this.quantite[i];
		}
		return total;
	}
}
