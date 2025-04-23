package modele.jeu;

import java.util.ArrayList;
import java.util.List;

import modele.plateau.Case;
import modele.plateau.DecorateurCasesAccessibles;
import modele.plateau.Direction;
import modele.plateau.Plateau;

/**
 * Entités amenées à bouger
 */
public abstract class Piece {

    protected Case c;
    protected Plateau plateau;
    protected DecorateurCasesAccessibles casesAccessibles;
    protected boolean blanc ;

    public Piece(Plateau _plateau , boolean _blanc) {
        plateau = _plateau;
        blanc = _blanc; 

    }

    public void quitterCase() {

        c.quitterLaCase();
    }

    public boolean estBlanc() {

        return blanc;
    }

    public void allerSurCase(Case _c) {
        if (c != null) {

            quitterCase();
        }
        
        c = _c;
        plateau.arriverCase(c, this);
    }

    public Case getCase() {
        return c;
    }

    public List<Case> getCasesAccessibles() {
    if (casesAccessibles != null) {
        return casesAccessibles.getCasesAccessibles();
    } else {
        return new ArrayList<>();
    }
}






}
