package modele.plateau;

import modele.jeu.Piece;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

/**
 * Ce décorateur gère les mouvements spécifiques du pion :
 * - Avancer d'une ou deux cases selon sa position
 * - Captures diagonales
 */
public class DecorateurCasesPion extends DecorateurCasesAccessibles {

    /**
     * Constructeur. Le limiteur est fixé à 1, car un pion ne peut pas se déplacer de plus d'une case sauf au premier coup.
     */
    public DecorateurCasesPion(DecorateurCasesAccessibles base, Plateau plateau, Piece piece) {
        super(base, plateau, piece, 1);
    }

    /**
     * Calcule les cases accessibles pour un pion :
     * - Une case devant
     * - Deux cases devant au premier coup
     * - Captures en diagonale (gauche/droite)
     */
    @Override
    public List<Case> getMesCasesAccessibles() {
        List<Case> accessibles = new ArrayList<>();

        // Vérifie que la pièce est bien initialisée et positionnée
        if (!estInitialisationValide()) return accessibles;

        // Récupère la position actuelle du pion
        Point pos = plateau.getPositionCase(piece.getCase());
        if (pos == null) return accessibles;

        // Détermination de la direction selon la couleur du pion
        int direction = piece.estBlanc() ? -1 : 1;

        // 1. Avance d'une case
        int x = pos.x;
        int y = pos.y + direction;
        if (estDansPlateau(x, y)) {
            Case avant = plateau.getCases()[x][y];
            if (avant.getPiece() == null) {
                accessibles.add(avant);

                // 2. Avance de deux cases si en position initiale
                if ((piece.estBlanc() && pos.y == 6) || (!piece.estBlanc() && pos.y == 1)) {
                    int y2 = y + direction;
                    if (estDansPlateau(x, y2)) {
                        Case doublePas = plateau.getCases()[x][y2];
                        if (doublePas.getPiece() == null) {
                            accessibles.add(doublePas);
                        }
                    }
                }
            }
        }

        // 3. Capture diagonale à gauche
        x = pos.x - 1;
        y = pos.y + direction;
        if (estDansPlateau(x, y)) {
            Case diagG = plateau.getCases()[x][y];
            if (diagG.getPiece() != null && diagG.getPiece().estBlanc() != piece.estBlanc()) {
                accessibles.add(diagG);
            }
        }

        // 4. Capture diagonale à droite
        x = pos.x + 1;
        y = pos.y + direction;
        if (estDansPlateau(x, y)) {
            Case diagD = plateau.getCases()[x][y];
            if (diagD.getPiece() != null && diagD.getPiece().estBlanc() != piece.estBlanc()) {
                accessibles.add(diagD);
            }
        }

        return accessibles;
    }

    // Vérifie que la pièce, le plateau, et la case sont valides
    private boolean estInitialisationValide() {
        return piece != null && plateau != null && piece.getCase() != null;
    }

    // Vérifie si les coordonnées (x, y) sont dans les limites du plateau
    private boolean estDansPlateau(int x, int y) {
        return x >= 0 && x < Plateau.SIZE_X && y >= 0 && y < Plateau.SIZE_Y;
    }
}
