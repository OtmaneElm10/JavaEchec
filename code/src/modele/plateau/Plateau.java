/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;


import modele.jeu.*;


import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;


public class Plateau extends Observable {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;


    private HashMap<Case, Point> map = new  HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    private void initPlateauVide() {

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }

        }

    }

  public void placerPieces() {
    // Pions blancs (rangée 6)
    for (int x = 0; x < SIZE_X; x++) {
        Pion pionBlanc = new Pion(this, true);
        pionBlanc.allerSurCase(grilleCases[x][6]);
    }

    // Pions noirs (rangée 1)
    for (int x = 0; x < SIZE_X; x++) {
        Pion pionNoir = new Pion(this, false);
        pionNoir.allerSurCase(grilleCases[x][1]);
    }

    // Pièces blanches (rangée 7)
    new Tour(this, true).allerSurCase(grilleCases[0][7]);
    new Cavalier(this, true).allerSurCase(grilleCases[1][7]);
    new Dame(this, true).allerSurCase(grilleCases[3][7]);
    new Roi(this, true).allerSurCase(grilleCases[4][7]);
    new Fou(this, true).allerSurCase(grilleCases[5][7]);
    new Fou(this, true).allerSurCase(grilleCases[2][7]);
    new Cavalier(this, true).allerSurCase(grilleCases[6][7]);
    new Tour(this, true).allerSurCase(grilleCases[7][7]);

    // Pièces noires (rangée 0)
    new Tour(this, false).allerSurCase(grilleCases[0][0]);
    new Cavalier(this, false).allerSurCase(grilleCases[1][0]);
    new Fou(this, false).allerSurCase(grilleCases[2][0]);
    new Dame(this, false).allerSurCase(grilleCases[3][0]);
    new Roi(this, false).allerSurCase(grilleCases[4][0]);
    new Fou(this, false).allerSurCase(grilleCases[5][0]);
    new Cavalier(this, false).allerSurCase(grilleCases[6][0]);
    new Tour(this, false).allerSurCase(grilleCases[7][0]);

    // Notifie les observateurs pour que la vue se mette à jour
 
}


public void arriverCase(Case c, Piece p) {
    if (c.getPiece() != null) {
        // Il y a une pièce ennemie à prendre
        // Tu peux l'enlever du jeu, ou l'ajouter dans une liste des pièces capturées si tu veux l'afficher
    }
    c.p = p;
}

    public void deplacerPiece(Case c1, Case c2) {
        if (c1.p != null) {
            c1.p.allerSurCase(c2);

        }
        setChanged();
        notifyObservers();

    }

    public Point getPositionCase(Case c) {
        return map.get(c);
    }
    

    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Case caseALaPosition(Point p) {
        Case retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }


}
