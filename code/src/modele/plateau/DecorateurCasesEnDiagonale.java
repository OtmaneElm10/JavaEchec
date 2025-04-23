// Package du décorateur qui gère les déplacements en diagonale
package modele.plateau;

import java.awt.Point;
import java.util.ArrayList;
import modele.jeu.Piece;

/**
 * Ce décorateur permet à une pièce de se déplacer en diagonale.
 * Il s'applique aux pièces comme le Fou, la Dame ou le Roi.
 */
public class DecorateurCasesEnDiagonale extends DecorateurCasesAccessibles {

    // Constructeur qui appelle celui de la classe abstraite
    public DecorateurCasesEnDiagonale(DecorateurCasesAccessibles base, Plateau plateau, Piece piece, int limiteur) {
        super(base, plateau, piece, limiteur);
    }

    /**
     * Calcule les cases accessibles en diagonale à partir de la position actuelle de la pièce.
     * @return Une liste de cases accessibles en diagonale.
     */
    public ArrayList<Case> getMesCasesAccessibles() {
        ArrayList<Case> casesAccessibles = new ArrayList<>();

        // Vérifie que les éléments nécessaires sont bien initialisés
        if (this.piece != null && this.plateau != null && this.piece.getCase() != null) {
            Case caseDepart = this.piece.getCase();
            Point positionDepart = this.plateau.getPositionCase(caseDepart);

            if (positionDepart == null) {
                return casesAccessibles;
            }

            // Parcours les 4 directions diagonales
            this.verifierDiagonale(positionDepart, -1, -1, casesAccessibles); // haut-gauche
            this.verifierDiagonale(positionDepart, 1, -1, casesAccessibles);  // haut-droit
            this.verifierDiagonale(positionDepart, -1, 1, casesAccessibles);  // bas-gauche
            this.verifierDiagonale(positionDepart, 1, 1, casesAccessibles);   // bas-droit

            return casesAccessibles;
        } else {
            return casesAccessibles;
        }
    }

    /**
     * Méthode privée qui teste une direction diagonale donnée.
     * Elle ajoute les cases libres ou capturables à la liste.
     *
     * @param depart Position de départ (x, y)
     * @param dx     Déplacement horizontal (ex : -1 pour aller à gauche)
     * @param dy     Déplacement vertical (ex : -1 pour aller en haut)
     * @param resultat Liste où stocker les cases accessibles
     */
    private void verifierDiagonale(Point depart, int dx, int dy, ArrayList<Case> resultat) {
        for (int pas = 1; pas <= this.limiteur; ++pas) {
            int x = depart.x + dx * pas;
            int y = depart.y + dy * pas;

            // Vérifie les limites du plateau
            if (x < 0 || x >= 8 || y < 0 || y >= 8) {
                break;
            }

            // Récupère la case cible
            Case cible = this.plateau.getCases()[x][y];

            if (cible.getPiece() != null) {
                // Si la pièce est ennemie, on peut la capturer
                if (cible.getPiece().estBlanc() != this.piece.estBlanc()) {
                    resultat.add(cible);
                }
                break; // Arrêt dès qu'on rencontre une pièce
            }

            // Case vide → accessible
            resultat.add(cible);
        }
    }
}
