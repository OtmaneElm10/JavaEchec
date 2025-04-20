package modele.plateau;

import java.util.ArrayList;
import modele.jeu.Piece;
import java.awt.Point;


public class DecorateurCasesEnDiagonale extends DecorateurCasesAccessibles {

    public DecorateurCasesEnDiagonale(DecorateurCasesAccessibles base, Plateau plateau, Piece piece, int limiteur) {
        super(base, plateau, piece, limiteur);
    }

    @Override
    public ArrayList<Case> getMesCasesAccessibles() {

        ArrayList<Case> casesAccessibles = new ArrayList<>();

        if (piece == null || plateau == null || piece.getCase() == null) {
            return casesAccessibles;
        }

        Case caseDepart = piece.getCase();
        Point coordDepart = plateau.getPositionCase(caseDepart);

        if (coordDepart == null) {

            return casesAccessibles;
        }

        // directions 

        verifierDiagonale(coordDepart, -1, -1, casesAccessibles); // haut gauche 
        verifierDiagonale(coordDepart, 1, -1, casesAccessibles);  // haut droit 
        verifierDiagonale(coordDepart, -1, 1, casesAccessibles);  // bas gauche
        verifierDiagonale(coordDepart, 1, 1, casesAccessibles);   // bas droit

        return casesAccessibles;
    }

    private void verifierDiagonale(Point coordDepart, int dx, int dy, ArrayList<Case> casesAccessibles) {
        
       
        for (int pas = 1; pas <= limiteur; pas++) {
            int x = coordDepart.x + dx * pas;
            int y = coordDepart.y + dy * pas;

            if (x >= 0 && x < Plateau.SIZE_X && y >= 0 && y < Plateau.SIZE_Y) {
                Case caseCible = plateau.getCases()[x][y];

                if (caseCible.getPiece() == null) {
                    casesAccessibles.add(caseCible);
                } else if (caseCible.getPiece().estBlanc() != piece.estBlanc()) {
                    casesAccessibles.add(caseCible);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }
}

