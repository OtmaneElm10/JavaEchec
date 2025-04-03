package modele.jeu;

import modele.plateau.*;

public class Pion extends Piece {

    public Pion(Plateau _plateau, boolean _estBlanc) {
        
        super(_plateau, _estBlanc);

        casesAccessibles = new DecorateurCasesPion(null);

        asesAccessibles.plateau = _plateau;
        casesAccessibles.piece = this;
        
    }
}