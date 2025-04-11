package modele.jeu;

import modele.plateau.*;

public class Pion extends Piece {

    public Pion(Plateau plateau, boolean estBlanc) {
        super(plateau, estBlanc);

        // On respecte la structure du décorateur
        casesAccessibles = new DecorateurCasesPion(null, plateau, this);
    }
}