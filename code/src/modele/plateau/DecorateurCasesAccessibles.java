package modele.plateau;

import modele.jeu.Piece;

import java.util.ArrayList;

public abstract class DecorateurCasesAccessibles {

// on place en protected pour pouvoir y acceder dans les sous classes mais pas public
     protected Plateau plateau; 
     protected Piece piece;  
    protected int limiteur ;
    private DecorateurCasesAccessibles base;

    public DecorateurCasesAccessibles(DecorateurCasesAccessibles _baseDecorateur, Plateau _plateau, Piece _piece, int _limiteur) {
        base = _baseDecorateur;
        plateau = _plateau;
        piece = _piece;
        limiteur = _limiteur ;

    }


    public ArrayList<Case> getCasesAccessibles() {
        ArrayList<Case> retour = getMesCasesAccessibles();

        if (base != null) {
            retour.addAll(base.getCasesAccessibles());
        }

        return retour;
    }

    public abstract ArrayList<Case> getMesCasesAccessibles();


}
