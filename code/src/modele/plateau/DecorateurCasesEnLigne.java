package modele.plateau;

import java.util.ArrayList;

public class DecorateurCasesEnLigne extends DecorateurCasesAccessibles {

    public DecorateurCasesEnLigne(DecorateurCasesAccessibles _baseDecorateur) {
        super(_baseDecorateur);
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
        
        //prendre en compte que le roi se déplace d'une case 
        int maxEtapes = (piece instanceof modele.jeu.Roi) ? 1 : 7;

        for (int etape = 1; etape <= maxEtapes; etape++) {

            int coordX = depart.x + deltaX * etape;
            int coordY = depart.y + deltaY * etape;


            if (coordX >= 0 && coordX < Plateau.SIZE_X && coordY >= 0 && coordY < Plateau.SIZE_Y) {
                Case caseCandidate = plateau.getCases()[coordX][coordY];

                if (caseCandidate.getPiece() == null) {

                    destinationsPossibles.add(caseCandidate);

                } else if (caseCandidate.getPiece().estBlanc() != piece.estBlanc()) {

                    destinationsPossibles.add(caseCandidate);

                    break; // bloque après prise

                } else {

                    break; // bloqué par  aalie
                }
            } else {

                break; // hors du plateau
            }
        }
    }
}