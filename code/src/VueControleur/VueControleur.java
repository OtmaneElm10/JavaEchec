package VueControleur;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modele.jeu.Jeu;
import modele.jeu.Piece;
import modele.jeu.Coup; // N'oublie pas d'importer Coup si pas déjà
import modele.plateau.Case;
import modele.plateau.Plateau;

public class VueControleur extends JFrame implements Observer {
    private Plateau plateau;
    private Jeu jeu;
    private final int sizeX;
    private final int sizeY;
    private static final int pxCase = 50;
    private Map<String, ImageIcon> icones = new HashMap<>();
    private Case caseClic1;
    private Case caseClic2;
    private JLabel[][] tabJLabel;

    public VueControleur(Jeu jeu) {
        this.jeu = jeu;
        this.plateau = this.jeu.getPlateau();
        this.sizeX = 8;
        this.sizeY = 8;
        this.chargerLesIcones();
        this.placerLesComposantsGraphiques();
        this.plateau.addObserver(this);
        this.mettreAJourAffichage();
    }

    private void chargerLesIcones() {
        String[] pieces = new String[]{"wK", "wQ", "wR", "wB", "wN", "wP", "bK", "bQ", "bR", "bB", "bN", "bP"};

        for (String p : pieces) {
            this.icones.put(p, this.chargerIcone("../Images/" + p + ".png"));
        }
    }

    private ImageIcon chargerIcone(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        this.setTitle("Jeu d'Échecs");
        this.setResizable(false);
        this.setSize(this.sizeX * pxCase, this.sizeY * pxCase);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
        JPanel panel = new JPanel(new GridLayout(this.sizeY, this.sizeX));
        this.tabJLabel = new JLabel[this.sizeX][this.sizeY];
    
        for (int y = 0; y < this.sizeY; y++) {
            for (int x = 0; x < this.sizeX; x++) {
                JLabel label = new JLabel();
                this.tabJLabel[x][y] = label;
    
                label.setOpaque(true);
                label.setFocusable(false); // Évite que le JLabel vole le focus clavier
                label.setHorizontalAlignment(JLabel.CENTER);
    
                // Meilleur gestionnaire de clic
                int finalX = x;
                int finalY = y;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        caseClicked(finalX, finalY); // 💥 Plus fiable que mouseClicked
                    }
                });
    
                if ((y % 2 != 0 || x % 2 != 0) && (y % 2 == 0 || x % 2 == 0)) {
                    label.setBackground(new Color(150, 150, 210)); // clair
                } else {
                    label.setBackground(new Color(50, 50, 110)); // foncé
                }
    
                panel.add(label);
            }
        }
    
        this.add(panel);
    }
    

    private void mettreAJourAffichage() {
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                Case c = this.plateau.getCases()[x][y];
                if (c != null) {
                    Piece p = c.getPiece();
                    if (p != null) {
                        this.tabJLabel[x][y].setIcon(this.getIconForPiece(p));
                    } else {
                        this.tabJLabel[x][y].setIcon((Icon) null);
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.mettreAJourAffichage();
    }

    private ImageIcon getIconForPiece(Piece p) {
        String color = p.estBlanc() ? "w" : "b";

        switch (p.getClass().getSimpleName()) {
            case "Roi":
                return this.icones.get(color + "K");
            case "Dame":
                return this.icones.get(color + "Q");
            case "Tour":
                return this.icones.get(color + "R");
            case "Fou":
                return this.icones.get(color + "B");
            case "Cavalier":
                return this.icones.get(color + "N");
            case "Pion":
                return this.icones.get(color + "P");
            default:
                return null;
        }
    }

    // Nouvelle méthode qui gère les clics
    private void caseClicked(int x, int y) {
        Case caseCliquee = plateau.getCases()[x][y];

        if (caseClic1 == null) {
            // Premier clic : sélection d'une pièce
            if (caseCliquee != null && caseCliquee.getPiece() != null) {
                caseClic1 = caseCliquee;
                System.out.println("Sélectionné : " + caseCliquee.getPiece().getClass().getSimpleName() + " (" + x + "," + y + ")");
            }
        } else {
            // Deuxième clic : tentative de déplacement
            caseClic2 = caseCliquee;

            if (caseClic2 != null) {
                jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                System.out.println("Coup envoyé de " + caseClic1 + " à " + caseClic2);
            }

            // Réinitialisation pour un nouveau tour
            caseClic1 = null;
            caseClic2 = null;
        }
    }
}
