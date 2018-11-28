package abstraction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

public class CliqueZoom implements ActionListener {

	private Catalogue catalogue;

	public CliqueZoom(Catalogue c) {
		this.catalogue = c;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Zoom fenetre = new Zoom(this.catalogue.getArticleCourant().getPhoto());
		fenetre.setVisible(true);
	}

}
