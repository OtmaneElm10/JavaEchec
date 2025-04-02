package modele.plateau;

import modele.jeu.Piece;

import java.util.ArrayList;

public abstract class DecorateurCasesAccessibles {

     protected Plateau plateau; 
     protected Piece piece; // on place en protected pour pouvoir y acceder dans les sous classes mais pas public 

    private DecorateurCasesAccessibles base;

    public DecorateurCasesAccessibles(DecorateurCasesAccessibles _baseDecorateur) {
        base = _baseDecorateur;
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
