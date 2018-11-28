// ---------------------------------------------
// Nom    : DEBRUYNE
// Prenom : Romuald
// ---------------------------------------------
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.MediaTracker;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

// - completez mouseDragged afin de modifier this.x et this.y en consequence

// - Completez mouseWheelMoved afin de modifier le facteur de zoom en consequence. Vous pouvez appeler this.repaint(); afin de faire un appel indirecte a la methode paint

// - Completez componentResized afin de prendre en compte le redimensionnement de la fenetre.

// - Completer paintComponent afin d'afficher des cercles rouges sur les stations de l'itineraire si l'itineraire ne vaut pas null.

// - Completer mouseMoved afin que si on est en mode selection de stations on affiche dans la barre de tite de la boite de dialogue (methode setTitle) le nom de la station la plus proche de la souris

// - Completer mouseClicked afin qu'on ajoute la station la plus proche a la liste des stations selectionnes
//   et appeler la methode setResult de notre boite de dialogue si d'aventure on a deja selectionne deux stations et qu'on venait deja de cliquer sur cette station en dernier. 

// - les setToolTip afin d'afficher le nom de la station (dans la vraie application on affiche les horaires de la station).
//   --> clic droit == activer/desactiver les tooltips

public class Map extends JPanel implements   MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener{

	// CONSTANTES
	static final long serialVersionUID=1;
	
	public static final int MAX_ZOOM = 10; // Combien de niveaux de zoom souhaitons nous
	public static final String FICHIER_IMAGE = "map.jpg"; // fichier contenant l'image du reseau
	public static final String FICHIER_COORDONNEES = "coordonnees.txt"; // fichier contenant les coordonnees des stations
	
	// VARIABLES D'INSTANCE
	private MediaTracker tracker; // pour atteindre la fin du chargement des images
	private Image offscreen;  // pour le double buffering : on n'affiche pas directement sur l'ecran, mais sur une image et une fois l'image mis a jour c'est elle qu'on affiche sur l'ecran
	private Graphics bufferGraphics; // le Graphics du double buffering : c'est sur lui qu'on trace. 
	
	private boolean charge=false; // vrai si les images sont chargees, false sinon.

	private int colDepart, ligDepart; // dernieres positions connues de la souris depuis que le bouton de la souris a ete enfonce

	private Image carte; // la carte du reseau
	private int largeur, hauteur; // largeur et hauteur de l'image (carte) en prenant en compte le zoom
	private int x,y; // position du coin haut gauche de l'image
	private int minX, maxX, minY, maxY; // positions limites pour le coin haut gauche de l'image
	
	private int niveauZoom; // le niveau de zoom actuel
	private double zoom, zoomMin, zoomMax; // coefficients multiplicateurs du zoom actuel, du zoomMin et du zoomMax

	
	private ArrayList<String> stations;    // les noms des stations
	private ArrayList<Point> coordonnees ; // les coordonnees des stations

	private LinkedList<String> itineraire; // la liste des noms de station de l'itineraire a afficher
	private DialogMap dialog; // la dialogMap dont on fait partie
	private ArrayList<String> result; // la selection de noms de stations

	public Map(DialogMap d, LinkedList<String> itineraire) {
		this.dialog = d;
		this.itineraire = itineraire;
		this.coordonnees = new ArrayList<Point>();
		this.stations = new ArrayList<String>();
		this.charger(); // consulte le fichier de coordonnees afin de mettre a jour les coordonnees et les noms des stations
		this.result = new ArrayList<String>();

		niveauZoom=0;
		zoomMax=1.0;

		tracker = new MediaTracker( this );
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
	}

	

	/**
	 * Consulte le fichier de coordonnes afin de mettre a jour la liste des noms de station et leurs coordonnees
	 */
	private void charger() {
		String nom;
		try {//Les operations sur les fichiers peuvent lever des exceptions
			// On ouvre le fichier en lecture 
			BufferedReader aLire = new BufferedReader(new FileReader(FICHIER_COORDONNEES)) ;
			do { 
				nom = aLire.readLine() ;
				// Si readLine() retourne null c’est qu’on a atteint la fin du fichier
				if (nom!=null) {// Si on n’a pas atteint la fin du fichier…
					this.stations.add(nom);
					this.coordonnees.add( new Point( Integer.parseInt(aLire.readLine()), Integer.parseInt(aLire.readLine())));
				}
			} while (nom !=null) ; // Tant qu’on n’a pas atteint la fin du fichier
			// On ferme le fichier 
			aLire.close() ;
		}
		catch (IOException e) {
			System.out.println("Une operation sur le fichier coordonnees.txt a leve l’exception "+e) ;
		}
	}

	public void chargerImages() {
		// Creation de oofscreen pour le double buffering
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize(); // dimensions de l'ecran
		offscreen = createImage((int)tailleEcran.getWidth(), (int)tailleEcran.getHeight());//

		// creation de l'image du reseau
		carte = Toolkit.getDefaultToolkit().getImage(FICHIER_IMAGE);

        // attente du chargement via un tracker
		tracker.addImage(offscreen, 0);
		tracker.addImage(carte, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			System.out.println("erreur tracker "+e);
		}
		if (offscreen==null || offscreen.getWidth(null)<0 || carte==null || carte.getWidth(null)<0){
			System.out.println("images pas bien chargees");
			this.charge=false;
		}
		else {
			this.charge=true;
		}
		// appel a ajuster afin de mettre a jour les differentes bornes en fonction des dimensions de la fenetre
		this.largeur = carte.getWidth(null);
		this.hauteur = carte.getHeight(null);
		
		this.ajuster();
		this.x=this.maxX;
		this.y=this.maxY;
		

	}

	/**
	 * met a jour zoomMin, largeur, hauteur, minX, ... et minY pour tenir compte des dimensions de la fenetre
	 */
	private void ajuster() {

	}
	
/**
 * Modifie this.x (resp. this.y) Si this.x (resp. this.y) est en dehors de [minX, maxX] (resp. [minY, maxY]) pour le remettre dans cette plage de validite 
 */
	private void recadrer() {

	}


/**
 * methode appelee automatiquement chaque fois que le composant doit etre redessine
 * @param g, la matrice de pixels du composant
 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!this.charge) {
			chargerImages();
		}
		else {
			// on va tout effectuer sur bufferGraphics avant d'afficher bufferGraphics sur g
			bufferGraphics = offscreen.getGraphics(); 
			bufferGraphics.drawImage(carte, this.x, this.y, largeur, hauteur, this);
			

			g.drawImage(offscreen,0, 0, this);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		this.colDepart=(e.getX());
		this.ligDepart=(e.getY());

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void    mouseClicked(MouseEvent e) {
	
	
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	
	public void mouseMoved(MouseEvent e) {

	
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {

		
		
	}

	public void componentResized(ComponentEvent e) {

		
	}

	public void componentHidden(ComponentEvent arg0) { }
	public void componentMoved(ComponentEvent arg0) { }
	public void componentShown(ComponentEvent arg0) { }
}
