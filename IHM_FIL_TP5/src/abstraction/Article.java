package abstraction;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;

public class Article {
	private ImageIcon photo; 
	private double largeur, profondeur, hauteur;
	private String descriptif;
	private int reference;
	private double prix;
	private int enStock;
	
	public Article(String nomFichier, double largeur, double profondeur, double hauteur, String descriptif, int reference, double prix, int quantite) {
		this.photo = new ImageIcon("images"+File.separator+nomFichier);
		this.largeur = largeur; 
		this.profondeur = profondeur; 
		this.hauteur = hauteur; 
		this.descriptif = descriptif; 
		this.reference = reference; 
		this.prix = prix; 
		this.enStock = quantite;
	}
	public ImageIcon getPhoto() {
		return this.photo;
	}
	public ImageIcon getIcone() {
		Image image = this.getPhoto().getImage();
		int largeur = 200;//image.getIconWidth();
		int hauteur = 200;//image.getIconHeight();
		BufferedImage buf = new BufferedImage(largeur, hauteur,	BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buf.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, largeur, hauteur, null);
		g.dispose();

		return (new ImageIcon(buf));

		
	}
	public int getEnStock() {
		return this.enStock;
	}
	public double getPrix() {
		return this.prix;
	}
    public String getDescription() {
    	return "<html><body><p><font face=\"Arial\" color=\"blue\"><b>Reference :</b></font>"+this.reference+"<br>"
    			+"<font face=\"Arial\" color=\"blue\"><b>Description :</b></font>"+this.descriptif+"<br>"
                +"<font face=\"Arial\" color=\"blue\"><b>Dimensions :</b></font>"+this.largeur+"x"+this.profondeur+"x"+this.hauteur+" cm<br>"
    			+"<font face=\"Arial\" color=\"blue\"><b>Prix :</b></font>"+this.prix+"</p></body></html>";
    }
}
