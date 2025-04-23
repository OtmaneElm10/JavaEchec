package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;
import java.awt.Point;
import java.util.List;
import VueControleur.VueControleur;

public class Jeu extends Thread {

    private Plateau plateau; // Le plateau de jeu contenant toutes les cases et pièces
    private Joueur j1;       // Joueur 1
    private Joueur j2;       // Joueur 2
    private VueControleur vue; // Lien vers l’interface graphique (optionnel)
    protected Coup coupRecu;   // Coup actuellement reçu pour traitement
    private boolean tourBlanc = true; // Variable pour savoir à qui est le tour

    // Constructeur du jeu : initialise le plateau, place les pièces et démarre le thread
    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();

        j1 = new Joueur(this);
        j2 = new Joueur(this);

        start(); // Lance le thread qui fait tourner la partie
    }

    // Retourne le plateau de jeu
    public Plateau getPlateau() {
        return plateau;
    }

    // Indique si c’est au tour du joueur blanc
    public boolean estTourBlanc() {
        return tourBlanc;
    }

    // Reçoit un coup et notifie le thread principal pour qu’il le traite
    public void envoyerCoup(Coup c) {
        coupRecu = c;

        synchronized (this) {
            notify(); // Réveille le thread de jeu qui attendait un coup
        }

        System.out.println("Coup reçu !");
    }

    // Applique un coup s’il est valide
    public void appliquerCoup(Coup coup) {
        Piece pieceDeplacee = coup.dep.getPiece();

        if (pieceDeplacee != null) {
            // Vérifie si la pièce appartient au joueur qui doit jouer
            if (pieceDeplacee.estBlanc() != tourBlanc) {
                System.out.println("Ce n'est pas ton tour !");
                return;
            }

            // Récupère les cases que cette pièce peut atteindre
            List<Case> casesPossibles = pieceDeplacee.casesAccessibles.getCasesAccessibles();

            if (casesPossibles.contains(coup.arr)) {
                // On sauvegarde l’état avant d’essayer
                Case caseDepart = coup.dep;
                Case caseArrivee = coup.arr;
                Piece pieceCapturee = caseArrivee.getPiece();

                // Déplacement temporaire pour vérifier l’échec
                caseDepart.quitterLaCase();
                caseArrivee.setPiece(pieceDeplacee);
                pieceDeplacee.allerSurCase(caseArrivee);

                // Si le roi est en échec après ce coup, on l'annule
                boolean estEchec = estEnEchec(pieceDeplacee.estBlanc());
                if (estEchec) {
                    caseArrivee.quitterLaCase();
                    caseDepart.setPiece(pieceDeplacee);
                    pieceDeplacee.allerSurCase(caseDepart);
                    if (pieceCapturee != null) {
                        caseArrivee.setPiece(pieceCapturee);
                    }

                    System.out.println("Coup interdit : vous restez en échec !");
                } else {
                    // Si pas d’échec, on applique réellement le déplacement
                    plateau.deplacerPiece(caseDepart, caseArrivee);

                    // On change le tour
                    changerTour();

                    // Message console
                    System.out.println("Coup joué !");

                    // Vérifie s'il y a égalité (pat)
                    if (estEgalite(tourBlanc)) {
                        System.out.println("Égalité (pat) !");
                        // Ici on pourrait appeler une méthode vue.afficherMessage(...)
                    }
                }
            } else {
                System.out.println("Coup invalide !");
            }
        }
    }

    // Boucle principale du thread : attend et joue des coups en boucle
    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {
        while (true) {
            Coup c = j1.getCoup(); // Récupère le prochain coup
            appliquerCoup(c);      // Tente de l’appliquer
        }
    }

    // Inverse le tour et notifie la vue
    private void changerTour() {
        tourBlanc = !tourBlanc;
        plateau.setChanged();
        plateau.notifyObservers(tourBlanc);
    }

    // Vérifie si une case est attaquée par une pièce adverse
    private boolean caseEstAttaquee(Point position, boolean parBlanc) {
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece p = plateau.getCases()[x][y].getPiece();
                if (p != null && p.estBlanc() == parBlanc) {
                    if (p.casesAccessibles.getCasesAccessibles().contains(plateau.getCases()[position.x][position.y])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Vérifie si le roi d'une couleur est en échec
    public boolean estEnEchec(boolean estBlanc) {
        Roi roi = trouverRoi(estBlanc);
        if (roi == null) return false;

        Point positionRoi = plateau.getPositionCase(roi.getCase());
        return caseEstAttaquee(positionRoi, !estBlanc);
    }

    // Cherche et retourne le roi d'une couleur
    private Roi trouverRoi(boolean estBlanc) {
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece p = plateau.getCases()[x][y].getPiece();
                if (p instanceof Roi && p.estBlanc() == estBlanc) {
                    return (Roi) p;
                }
            }
        }
        return null;
    }


    // Vérifie s’il y a égalité (pat)
    public boolean estEgalite(boolean joueurBlanc) {
        if (estEnEchec(joueurBlanc)) return false;

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece piece = plateau.getCases()[x][y].getPiece();
                if (piece != null && piece.estBlanc() == joueurBlanc) {
                    List<Case> accessibles = piece.getCasesAccessibles();
                    if (!accessibles.isEmpty()) return false;
                }
            }
        }

        return true; // Aucun coup possible, pas en échec => pat
    }
}
