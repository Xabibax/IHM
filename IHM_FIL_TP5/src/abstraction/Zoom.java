package abstraction;

import java.awt.Container;

import javax.swing.*;

public class Zoom extends JFrame {
	public Zoom(ImageIcon pPhoto) {
		super("Zoom");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		Container conteneur = this.getContentPane();
		
		JLabel photoArticleCourant = new JLabel();
		conteneur.add(photoArticleCourant);
		photoArticleCourant.setIcon(pPhoto);
		
		this.pack();
	}
	
}


