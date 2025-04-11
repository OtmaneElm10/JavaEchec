package modele.jeu;

import modele.plateau.*;

public class Fou extends Piece {

    public Fou(Plateau plateau, boolean estBlanc) {
        super(plateau, estBlanc);

        casesAccessibles = new DecorateurCasesEnDiagonale(null, plateau, this, -1);
    }
}
