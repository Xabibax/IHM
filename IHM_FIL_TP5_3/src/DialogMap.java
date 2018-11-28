import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JDialog;
import javax.swing.JFrame;


public class DialogMap extends JDialog {

	private static final long serialVersionUID = 1L; // juste pour calmer java

	private ArrayList<String> result; // la liste des stations selectionnees


	public DialogMap(  String title) {
		this( title, null);
	}
	public DialogMap( String title, LinkedList<String> itineraire) {
		this( (JFrame)null,title, itineraire);
	}
	/**
	 * 
	 * (cf. main pour exemples d'utilisation) 
	 * @param parent, la JFrame parente (null si par exemple la boite de dialogue n'est pas evoquee depuis une fenetre graphique
	 * @param title, le titre affiche dans la barre de titre de la fenetre
	 * @param itineraire, Si itineraire ne vaut pas null, la boite de dialogue affiche le plan du reseau et des points rouges pour marquer les stations de l'itineraire.
	 * Si par contre itineraire vaut null, la boite de dialogue va permettre de selectionner des stations.
	 */
	public DialogMap(  JFrame parent, String title, LinkedList<String> itineraire) {
		super(parent, title, true);
		// la boite de dialogue n'est composee que d'une instance de map
		setLayout(new BorderLayout());
		Map m = new Map(this, itineraire); //, res
		getContentPane().add(m, BorderLayout.CENTER);

		// On va dimensionner la fenetre pour qu'elle occupe initialement l'integralite de l'espace disponible		
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize(); // dimensions de l'ecran
		GraphicsConfiguration gconf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Insets insets = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(gconf); // marges occupees par le systeme (barre d'outil, barre des taches, ...

		int hauteurMax = (int)tailleEcran.getHeight() - insets.top - insets.bottom; // l'espace disponible c'est la taille de l'ecran moins les marges
		int largeurMax = (int)tailleEcran.getWidth() - insets.left - insets.right;
		this.setSize(new Dimension(largeurMax,hauteurMax));
	}

	/** Accesseur permettant a l'instance de map de mettre a jour le resultat (la liste des stations selectionnees).
	 *  Notez que le resultat etant a present connu, on masque la boite de dialogue.
	 */
	public void setResult( ArrayList<String> t) {
		this.result = t;
		this.setVisible(false);
	}

	/**
	 * Accesseur permettant de recuperer le resultat, c'est a dire la liste des stations selectionnees
	 * @return
	 */
	public ArrayList<String> getResult() {
		return this.result;
	}

	public static void main(String[] args) {
		// TEST AFFICHAGE D'UN ITINERAIRE
		String[] t = { "Gare de Pont Rousseau", "Pont Rousseau", "Pirmil"};
		LinkedList<String> itineraire = new LinkedList<String>();
		for (int i=0; i<t.length; i++) {
			itineraire.add(t[i]);
		}
		DialogMap dm = new DialogMap( "Affichage itineraire", itineraire);
		dm.setVisible(true); // on rend visible : la boite etant modale le reste du programme demeure bloque jusqu'a ce qu'on ferme la boite de dialogue.
		dm.dispose(); // on libere l'espace occupe par la boite de dialogue.

		// TEST DE LA SAISIE DE STATIONS POUR UNE DEMANDE D'ITINERAIRES
		DialogMap dm2 = new DialogMap( "Saisie des stations");
		dm2.setVisible(true);
		// On recupere la liste des noms de stations selectionnees
		System.out.println(dm2.getResult());
		dm2.dispose(); // on libere l'espace occupe par la boite de dialogue.
	}

}

