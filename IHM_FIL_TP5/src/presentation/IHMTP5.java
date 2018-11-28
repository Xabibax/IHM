package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import abstraction.Catalogue;
import abstraction.Panier;


public class IHMTP5 extends JFrame {
	private Catalogue catalogue;
	private Panier panier;

	public IHMTP5(Catalogue catalogue, Panier panier) {

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.catalogue = catalogue;
		this.panier = panier;

		
		this.pack();
	}


	public static void main(String[] args) {
		Catalogue c = new Catalogue("catalogue.txt");
		Panier p = new Panier( c );
		IHMTP5 fen = new IHMTP5(c, p);
		fen.setVisible(true);
	}
}
