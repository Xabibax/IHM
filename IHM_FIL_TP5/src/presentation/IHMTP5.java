package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import abstraction.*;

public class IHMTP5 extends JFrame {
	private Catalogue catalogue;
	private Panier panier;

	public IHMTP5(Catalogue catalogue, Panier panier) {
		// 1.2. L’icône et la description
		super("IHM TP5");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.catalogue = catalogue;
		this.panier = panier;
		Container conteneur = this.getContentPane();
		conteneur.setLayout(new BoxLayout(conteneur, BoxLayout.X_AXIS));
		

		JPanel partieGauche = new JPanel();
		conteneur.add(partieGauche);	
		JPanel partieDroite = new JPanel();
		conteneur.add(partieDroite);
		
		JLabel iconeArticleCourant = new JLabel();
		partieGauche.add(iconeArticleCourant);
		iconeArticleCourant.setIcon(catalogue.getArticleCourant().getIcone());
		
		JLabel descriptionArticleCourant = new JLabel();
		partieDroite.add(descriptionArticleCourant);
		descriptionArticleCourant.setText(catalogue.getArticleCourant().getDescription());
		
		// 1.3. Le ZOOM
		JButton boutonZoom = new JButton("Zoom");
		partieGauche.add(boutonZoom);
		partieGauche.setLayout(new BoxLayout(partieGauche, BoxLayout.Y_AXIS));
		
		boutonZoom.addActionListener(new CliqueZoom(catalogue));
		
		this.pack();
	}


	public static void main(String[] args) {
		Catalogue c = new Catalogue("catalogue.txt");
		Panier p = new Panier( c );
		IHMTP5 fen = new IHMTP5(c, p);
		fen.setVisible(true);
	}
}
