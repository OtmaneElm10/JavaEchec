package modele.jeu;

import modele.plateau.*;
import modele.jeu.Piece;

public class Cavalier extends Piece {

    public Cavalier(Plateau plateau ,boolean est_blanc) {
        super(plateau,est_blanc);

        
        casesAccessibles = new DecorateurCasesCavalier(null, plateau, this, 1); // avec le limiteur a 1 meme si il est pas utilisé 
    }
}