package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;
import java.awt.Point;


import java.util.List;

import VueControleur.VueControleur;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur j1;
    private Joueur j2;
    private VueControleur vue;
    protected Coup coupRecu;
    private boolean tourBlanc = true; 

    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();

        j1 = new Joueur(this);
        j2 = new Joueur(this);

        start();
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public boolean estTourBlanc() {
        return tourBlanc;
    }

    public void envoyerCoup(Coup c) {
        coupRecu = c;

        synchronized (this) {
            notify();
        }

        System.out.println("Coup reçu !");
    }

    public void appliquerCoup(Coup coup) {
        Piece pieceDeplacee = coup.dep.getPiece();
    
        if (pieceDeplacee != null) {
            // 1. Vérifie le tour du joueur
            if (pieceDeplacee.estBlanc() != tourBlanc) {
                System.out.println("Ce n'est pas ton tour !");
                return;
            }
    
            // 2. Vérifie que le coup est autorisé
            List<Case> casesPossibles = pieceDeplacee.casesAccessibles.getCasesAccessibles();
            if (casesPossibles.contains(coup.arr)) {
                // 3. Sauvegarde l'état
                Case caseDepart = coup.dep;
                Case caseArrivee = coup.arr;
                Piece pieceCapturee = caseArrivee.getPiece();
    
                // 4. Applique temporairement le coup
                caseDepart.quitterLaCase(); // enlève la pièce
                caseArrivee.setPiece(pieceDeplacee); // met la pièce sur la nouvelle case
                pieceDeplacee.allerSurCase(caseArrivee);
    
                // 5. Vérifie si le roi est en échec
                boolean estEchec = estEnEchec(pieceDeplacee.estBlanc());
    
                if (estEchec) {
                    // Annule le coup
                    caseArrivee.quitterLaCase();
                    caseDepart.setPiece(pieceDeplacee);
                    pieceDeplacee.allerSurCase(caseDepart);
                    if (pieceCapturee != null) {
                        caseArrivee.setPiece(pieceCapturee);
                    }
    
                    System.out.println("Coup interdit : vous restez en échec !");
                } else {
                    // 6. Coup valide : applique réellement le coup
                    plateau.deplacerPiece(caseDepart, caseArrivee);
                    changerTour(); // alterne entre tourBlanc = true/false
                    System.out.println("Coup joué !");
                }
            } else {
                System.out.println("Coup invalide !");
            }
        }
    }
    
    
    
    
    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {
        while (true) {
            Coup c = j1.getCoup();
            appliquerCoup(c);
        }
    }

    private void changerTour() {
        tourBlanc = !tourBlanc;
        plateau.setChanged(); // 
        plateau.notifyObservers(tourBlanc); 
    }

    private boolean caseEstAttaquee(Point position, boolean verif) {

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {

                Piece p = plateau.getCases()[x][y].getPiece();
                if (p != null && p.estBlanc() == verif) {

                    if (p.casesAccessibles.getCasesAccessibles().contains(plateau.getCases()[position.x][position.y])) 
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    

    public boolean estEnEchec(boolean est_roi) {

        Roi roi = trouverRoi(est_roi);
        if (roi == null) {

            return false; // probleme 
        }
        
        Point positionRoi = plateau.getPositionCase(roi.getCase());
        return caseEstAttaquee(positionRoi, !est_roi); 
    }
    
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

    public void setVue(VueControleur vue) {
        this.vue = vue;
    }


}
