package modele.plateau;

import java.util.ArrayList;
import modele.jeu.Piece;
import java.awt.Point;

/**
 * Ce décorateur gère les mouvements d'une pièce en ligne droite :
 * haut, bas, gauche, droite (utilisé pour la tour, dame, roi avec limite).
 */
public class DecorateurCasesEnLigne extends DecorateurCasesAccessibles {

    // Constructeur qui initialise le décorateur avec la pièce, le plateau, le décorateur précédent et la limite de déplacement
    public DecorateurCasesEnLigne(DecorateurCasesAccessibles base, Plateau plateau, Piece piece, int limiteur) {
        super(base, plateau, piece, limiteur);
    }

    /**
     * Renvoie toutes les cases accessibles en ligne droite à partir de la position actuelle de la pièce.
     */
    @Override
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> casesPossibles = new ArrayList<>();

        // Vérifie que la pièce et sa position sont valides
        if (piece == null || plateau == null || piece.getCase() == null) {
            return casesPossibles;
        }

        // Récupère la position de départ de la pièce sur le plateau
        Case caseActuelle = piece.getCase();
        Point position = plateau.getPositionCase(caseActuelle);  

        if (position == null) {
            return casesPossibles;
        }

        // Appelle les 4 directions cardinales (haut, bas, gauche, droite)
        verifierDirection(position, 0, -1, casesPossibles); // vers le haut
        verifierDirection(position, 0, 1, casesPossibles);  // vers le bas
        verifierDirection(position, -1, 0, casesPossibles); // vers la gauche
        verifierDirection(position, 1, 0, casesPossibles);  // vers la droite

        return casesPossibles;
    }

    /**
     * Méthode qui ajoute les cases valides dans une direction donnée, jusqu'à une limite ou un obstacle.
     *
     * @param depart : point de départ (position de la pièce)
     * @param deltaX : déplacement horizontal
     * @param deltaY : déplacement vertical
     * @param destinationsPossibles : liste à remplir avec les cases accessibles
     */
    private void verifierDirection(Point depart, int deltaX, int deltaY, ArrayList<Case> destinationsPossibles) {

        for (int etape = 1; limiteur == -1 || etape <= limiteur; etape++) {
            int coordX = depart.x + deltaX * etape;
            int coordY = depart.y + deltaY * etape;

            // Vérifie qu'on reste dans les limites du plateau
            if (coordX >= 0 && coordX < Plateau.SIZE_X && coordY >= 0 && coordY < Plateau.SIZE_Y) {
                Case caseCandidate = plateau.getCases()[coordX][coordY];

                // Si la case est vide, elle est accessible
                if (caseCandidate.getPiece() == null) {
                    destinationsPossibles.add(caseCandidate);
                } 
                // Si la case contient une pièce ennemie, elle est accessible (capture possible), puis on s'arrête
                else if (caseCandidate.getPiece().estBlanc() != piece.estBlanc()) {
                    destinationsPossibles.add(caseCandidate);
                    break;
                } 
                // Si la case contient une pièce alliée, on ne peut pas aller plus loin
                else {
                    break;
                }
            } else {
                break; // on sort du plateau
            }
        }
    }
}
