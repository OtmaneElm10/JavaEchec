package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;
import java.util.List;


import javax.print.event.PrintJobEvent;

public class Jeu extends Thread{
    private Plateau plateau;
    private Joueur j1;
    private Joueur j2;
    protected Coup coupRecu;

    private Roi roi;

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

    public void placerPieces() {

        plateau.placerPieces();
    }


    public void envoyerCoup(Coup c) {
        coupRecu = c;

        synchronized (this) {
            notify();
        }
        System.out.println("hello");
    }


    public void appliquerCoup(Coup coup) {

        Piece pieceDeplacee = coup.dep.getPiece();

    if (coup.dep.getPiece() != null) {
        List<Case> casesPossibles = coup.dep.getPiece().casesAccessibles.getCasesAccessibles();
        if (casesPossibles.contains(coup.arr)) {
            // Si la destination est accessible, on applique le coup
            plateau.deplacerPiece(coup.dep, coup.arr);

            boolean estEchec = estEnEchec(pieceDeplacee.estBlanc());
            if (estEchec) {
                System.out.println("Attention : votre roi est en échec !");
            }
        } else {
            System.out.println("Coup invalide !");
        }
    }
  
}

}

    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

        while(true) {
            Coup c = j1.getCoup();
            appliquerCoup(c);
        }

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
    
}
