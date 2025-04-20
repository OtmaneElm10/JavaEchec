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
    if (coup.dep.getPiece() != null) {
        List<Case> casesPossibles = coup.dep.getPiece().casesAccessibles.getCasesAccessibles();
        if (casesPossibles.contains(coup.arr)) {
            // Si la destination est accessible, on applique le coup
            plateau.deplacerPiece(coup.dep, coup.arr);
        } else {
            System.out.println("Coup invalide !");
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


}
