package abstraction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

public class Catalogue extends Observable {
	private ArrayList<Article> articles;
    private int indexCourant;
    
	public Catalogue(String nomFichier) {
	      this.articles = new ArrayList<Article>();
	      try {
	         BufferedReader aLire= new BufferedReader(new FileReader(nomFichier));
             String ligne;
             String fichier;
        	 double largeur, profondeur, hauteur;
        	 String descriptif; 
        	 int reference;
        	 int quantite;
        	 double prix;
	         do {
	        			
	        	 ligne = aLire.readLine();
	            if (ligne!=null) { 
	            	fichier = ligne;
	            	largeur = Double.parseDouble(aLire.readLine());
	            	profondeur = Double.parseDouble(aLire.readLine()); 
	            	hauteur = Double.parseDouble(aLire.readLine()); 
	            	descriptif = aLire.readLine();
	            	reference = Integer.parseInt(aLire.readLine()); 
	            	prix = Double.parseDouble(aLire.readLine()); 
	            	quantite = Integer.parseInt(aLire.readLine()); 
                    this.articles.add(new Article(fichier, largeur, profondeur, hauteur, descriptif, reference, prix, quantite));
	            }
	         } while (ligne!=null); // tant qu'on n'a pas atteint la fin du fichier aLire


	         // On ferme les fichiers
	         aLire.close( );
	      }
	      catch (IOException e) { 
	         System.out.println("Une operation sur les fichiers a leve l'exception "+e);
	      }
	      this.indexCourant = 0;
	}
	public int getSize() {
		return this.articles.size();
	}
	public Article get(int i) {
		if (i>=0 && i<this.getSize()) {
			return this.articles.get(i);
		}
		else {
			return null;
		}
	}
	public Article getArticleCourant() {
		return this.get(this.getIndexCourant());
	}
	public int getIndexCourant() {
		return this.indexCourant;
	}
	public int getIndex(Article a) {
		return this.articles.indexOf(a);
	}
	public void setIndexCourant(int i) {
		if (i>=0 && i<this.getSize()) {
			this.indexCourant = i;
			this.setChanged();
			this.notifyObservers();		}
	}
}
