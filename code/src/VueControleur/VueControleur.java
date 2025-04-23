package VueControleur;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import modele.jeu.*;
import modele.plateau.*;
import java.util.List;

/**
 * Classe responsable de l'interface graphique et du contrôle du jeu.
 * Elle observe les changements du plateau et met à jour l'affichage.
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau;             // Référence au plateau de jeu
    private Jeu jeu;                     // Référence au modèle de jeu
    private final int sizeX;             // Largeur du plateau (en cases)
    private final int sizeY;             // Hauteur du plateau (en cases)
    private static final int pxCase = 50; // Taille en pixels d'une case

    private Map<String, ImageIcon> icones = new HashMap<>(); // Associe chaque type de pièce à une icône
    private Case caseClic1;              // Première case cliquée
    private Case caseClic2;              // Deuxième case cliquée (destination)
    private JLabel[][] tabJLabel;        // Tableau graphique des cases
    private JLabel labelTour;            // Affiche le tour du joueur

    public VueControleur(Jeu jeu) {
        this.jeu = jeu;
        this.plateau = this.jeu.getPlateau();
        this.sizeX = 8;
        this.sizeY = 8;

        this.chargerLesIcones();               // Charge les icônes des pièces
        this.placerLesComposantsGraphiques();  // Initialise l'affichage
        this.plateau.addObserver(this);        // Observe le plateau
        this.mettreAJourAffichage();           // Affiche l'état initial
    }

    // Charge les icônes correspondant aux pièces
    private void chargerLesIcones() {
        String[] pieces = {"wK", "wQ", "wR", "wB", "wN", "wP", "bK", "bQ", "bR", "bB", "bN", "bP"};
        for (String p : pieces) {
            this.icones.put(p, this.chargerIcone("../Images/" + p + ".png"));
        }
    }

    // Charge une icône à partir d'un chemin et la redimensionne
    private ImageIcon chargerIcone(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    // Configure l'interface graphique de la fenêtre
    private void placerLesComposantsGraphiques() {
        this.setTitle("Jeu d'Échecs");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Label du haut affichant à qui est le tour
        labelTour = new JLabel("Tour des Blancs", JLabel.CENTER);
        labelTour.setOpaque(true);
        labelTour.setBackground(new Color(30, 30, 30));
        labelTour.setForeground(Color.WHITE);
        labelTour.setFont(labelTour.getFont().deriveFont(16f));
        labelTour.setPreferredSize(new Dimension(sizeX * pxCase, 30));
        this.add(labelTour, BorderLayout.NORTH);

        // Plateau de jeu avec une grille 8x8
        JPanel panelPlateau = new JPanel(new GridLayout(sizeY, sizeX));
        this.tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel label = new JLabel();
                tabJLabel[x][y] = label;
                label.setOpaque(true);
                label.setFocusable(false);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setPreferredSize(new Dimension(pxCase, pxCase));

                final int finalX = x;
                final int finalY = y;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        caseClicked(finalX, finalY); // Gère le clic
                    }
                });

                // Coloration des cases (échiquier)
                if ((y + x) % 2 == 0) {
                    label.setBackground(new Color(150, 150, 210));
                } else {
                    label.setBackground(new Color(50, 50, 110));
                }

                panelPlateau.add(label);
            }
        }

        this.add(panelPlateau, BorderLayout.CENTER);
        this.pack();                     // Ajuste la taille de la fenêtre
        this.setLocationRelativeTo(null); // Centre la fenêtre à l'écran
    }

    // Met à jour l'affichage de toutes les cases du plateau
    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Case c = plateau.getCases()[x][y];
                if (c != null) {
                    Piece p = c.getPiece();
                    if (p != null) {
                        tabJLabel[x][y].setIcon(getIconForPiece(p));
                    } else {
                        tabJLabel[x][y].setIcon(null);
                    }
                }
            }
        }
    }

    // Méthode appelée automatiquement quand le modèle notifie un changement
    @Override
    public void update(Observable o, Object arg) {
        this.mettreAJourAffichage();
        if (arg instanceof Boolean) {
            afficherTour((Boolean) arg);
        }
    }

    // Retourne l'icône correspondant à une pièce donnée
    private ImageIcon getIconForPiece(Piece p) {
        String color = p.estBlanc() ? "w" : "b";
        switch (p.getClass().getSimpleName()) {
            case "Roi": return icones.get(color + "K");
            case "Dame": return icones.get(color + "Q");
            case "Tour": return icones.get(color + "R");
            case "Fou": return icones.get(color + "B");
            case "Cavalier": return icones.get(color + "N");
            case "Pion": return icones.get(color + "P");
            default: return null;
        }
    }

    // Gère les clics de l'utilisateur sur les cases du plateau
    private void caseClicked(int x, int y) {
        Case caseCliquee = plateau.getCases()[x][y];

        reinitialiserCouleurs(); // Réinitialise les couleurs avant de marquer les nouvelles cases accessibles

        if (caseClic1 == null) {
            // Premier clic : sélection de la pièce
            if (caseCliquee != null && caseCliquee.getPiece() != null) {
                caseClic1 = caseCliquee;
                System.out.println("Sélectionné : " + caseCliquee.getPiece().getClass().getSimpleName() + " (" + x + "," + y + ")");
                marquerCasesAccessibles(caseClic1); // Affiche les mouvements possibles
            }
        } else {
            // Deuxième clic : tentative de déplacement
            caseClic2 = caseCliquee;
            if (caseClic2 != null) {
                jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                System.out.println("Coup envoyé de " + caseClic1 + " à " + caseClic2);
            }
            caseClic1 = null;
            caseClic2 = null;
        }
    }

    // Met à jour le texte du label du tour
    public void afficherTour(boolean estBlanc) {
        if (labelTour != null) {
            labelTour.setText("Tour des " + (estBlanc ? "Blancs" : "Noirs"));
        }
    }

    // Colorie en vert clair les cases accessibles pour la pièce sélectionnée
    private void marquerCasesAccessibles(Case caseDepart) {
        List<Case> accessibles = caseDepart.getPiece().getCasesAccessibles();
        for (Case c : accessibles) {
            Point p = plateau.getPositionCase(c);
            if (p != null) {
                tabJLabel[p.x][p.y].setBackground(new Color(173, 255, 47));
            }
        }
    }

    // Remet les couleurs d'origine des cases (claires/foncées)
    private void reinitialiserCouleurs() {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if ((y + x) % 2 == 0) {
                    tabJLabel[x][y].setBackground(new Color(150, 150, 210));
                } else {
                    tabJLabel[x][y].setBackground(new Color(50, 50, 110));
                }
            }
        }
    }

    // Affiche un message d'égalité dans l'interface
    public void afficherEgalite() {
        if (labelTour != null) {
            labelTour.setText("Égalité - Partie nulle !");
            labelTour.setForeground(Color.ORANGE);
        }
    }
}
