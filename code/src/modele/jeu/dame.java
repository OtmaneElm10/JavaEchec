/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.jeu;

import modele.plateau.*;


public class Dame extends Piece {

    public Dame(Plateau plateau, boolean est_blanc) {
        super(plateau, est_blanc);
        casesAccessibles = new DecorateurCasesEnLigne(
            new DecorateurCasesEnDiagonale(null, plateau, this, -1),
            plateau, this, -1
        );
    }
}

