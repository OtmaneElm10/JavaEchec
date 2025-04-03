package modele.plateau;

import java.util.ArrayList;

public class DecorateurCasesEnLigne extends DecorateurCasesAccessibles {

    public DecorateurCasesEnLigne(DecorateurCasesAccessibles base, Plateau plateau, Piece piece, int limiteur) {
        super(base, plateau, piece, limiteur);
    }

    @Override
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> casesPossibles = new ArrayList<>();

        if (piece == null || plateau == null || piece.getCase() == null) {
            return casesPossibles;
        }

        Case caseActuelle = piece.getCase();
        Point position = plateau.getPositionCase(caseActuelle);  

        if (position == null) {
            return casesPossibles;
        }

        // Directions 

        verifierDirection(position, 0, -1, casesPossibles); // haut
        verifierDirection(position, 0, 1, casesPossibles);  // bas
        verifierDirection(position, -1, 0, casesPossibles); // gauche
        verifierDirection(position, 1, 0, casesPossibles);  // droite

        return casesPossibles;
    }

    //focntion privé qui perment de détecter les obstacle elle nous perment de savoir si le déplacement est ok 
    //si on est pas en bord du plateau ou si il y a une autre piece qui nous bloque etc ...
    
    private void verifierDirection(Point depart, int deltaX, int deltaY, ArrayList<Case> destinationsPossibles) {

        for (int etape = 1; etape <= limiteur; etape++) {
    
            int coordX = depart.x + deltaX * etape;
            int coordY = depart.y + deltaY * etape;
    
            if (coordX >= 0 && coordX < Plateau.SIZE_X && coordY >= 0 && coordY < Plateau.SIZE_Y) {
                Case caseCandidate = plateau.getCases()[coordX][coordY];
    
                if (caseCandidate.getPiece() == null) {
                    destinationsPossibles.add(caseCandidate);
                } else if (caseCandidate.getPiece().estBlanc() != piece.estBlanc()) {
                    destinationsPossibles.add(caseCandidate);
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