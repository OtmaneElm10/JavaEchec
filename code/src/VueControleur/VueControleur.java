package VueControleur;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
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
import java.awt.BorderLayout;
import java.awt.Dimension;


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
    private JLabel labelTour;


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
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
    
        // 🔷 Label d'affichage du tour
        labelTour = new JLabel("Tour des Blancs", JLabel.CENTER);
        labelTour.setOpaque(true);
        labelTour.setBackground(new Color(30, 30, 30));
        labelTour.setForeground(Color.WHITE);
        labelTour.setFont(labelTour.getFont().deriveFont(16f));
        labelTour.setPreferredSize(new Dimension(sizeX * pxCase, 30));
        this.add(labelTour, BorderLayout.NORTH);
    
        // 🔷 Plateau de jeu
        JPanel panelPlateau = new JPanel(new GridLayout(sizeY, sizeX));
        this.tabJLabel = new JLabel[sizeX][sizeY];
    
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel label = new JLabel();
                tabJLabel[x][y] = label;
    
                label.setOpaque(true);
                label.setFocusable(false);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setPreferredSize(new Dimension(pxCase, pxCase)); // ✅ taille forcée
    
                int finalX = x;
                int finalY = y;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        caseClicked(finalX, finalY);
                    }
                });
    
                if ((y + x) % 2 == 0) {
                    label.setBackground(new Color(150, 150, 210)); // clair
                } else {
                    label.setBackground(new Color(50, 50, 110)); // foncé
                }
    
                panelPlateau.add(label);
            }
        }
    
        this.add(panelPlateau, BorderLayout.CENTER);
    
        
        this.pack();
        this.setLocationRelativeTo(null); // centre la fenêtre
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
        if (arg instanceof Boolean) {
            afficherTour((Boolean) arg);
        }
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
    
       
        reinitialiserCouleurs();
    
        if (caseClic1 == null) {
           
            if (caseCliquee != null && caseCliquee.getPiece() != null) {
                caseClic1 = caseCliquee;
    
                System.out.println("Sélectionné : " + caseCliquee.getPiece().getClass().getSimpleName() + " (" + x + "," + y + ")");
    
               
                marquerCasesAccessibles(caseClic1);
            }
        } else {
          
            caseClic2 = caseCliquee;
    
            if (caseClic2 != null) {
                jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                System.out.println("Coup envoyé de " + caseClic1 + " à " + caseClic2);
            }
    
            
            caseClic1 = null;
            caseClic2 = null;
        }
    }
    

    public void afficherTour(boolean estBlanc) {
        if (labelTour != null) {
            labelTour.setText("Tour des " + (estBlanc ? "Blancs" : "Noirs"));
        }
    }

    private void marquerCasesAccessibles(Case caseDepart) {
    List<Case> accessibles = caseDepart.getPiece().getCasesAccessibles();

    for (Case c : accessibles) {
        Point p = plateau.getPositionCase(c);
        if (p != null) {
            tabJLabel[p.x][p.y].setBackground(new Color(173, 255, 47)); // 💡 Vert clair
        }
    }
}

private void reinitialiserCouleurs() {
    for (int y = 0; y < sizeY; y++) {
        for (int x = 0; x < sizeX; x++) {
            if ((y + x) % 2 == 0) {
                tabJLabel[x][y].setBackground(new Color(150, 150, 210)); // clair
            } else {
                tabJLabel[x][y].setBackground(new Color(50, 50, 110)); // foncé
            }
        }
    }
}

public void afficherMessage(String message) {
    javax.swing.JOptionPane.showMessageDialog(this, message);
}


    
}
