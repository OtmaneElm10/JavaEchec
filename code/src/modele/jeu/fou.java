package modele.jeu;

import modele.plateau.*;

public class Fou extends Piece {
    public Fou(Plateau plateau, boolean est_blanc) {
        super(plateau, est_blanc);
        casesAccessibles = new DecorateurCasesEnDiagonale(null, plateau, this, 7);
    }
}

