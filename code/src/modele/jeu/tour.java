/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.jeu;

import modele.plateau.*;

import java.util.ArrayList;


public class Tour extends Piece {

    public Tour(Plateau plateau, boolean est_blanc) {
        super(plateau, est_blanc);
        casesAccessibles = new DecorateurCasesEnLigne(null, plateau, this, -1);
    }
}


