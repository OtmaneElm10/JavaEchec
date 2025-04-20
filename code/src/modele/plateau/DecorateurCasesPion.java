package modele.plateau;

import modele.jeu.Piece;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class DecorateurCasesPion extends DecorateurCasesAccessibles {

    public DecorateurCasesPion(DecorateurCasesAccessibles base, Plateau plateau, Piece piece) {
        super(base, plateau, piece, 1); // le limiteur est 1 par défaut pour avancer d'une case
    }

    @Override
    public List<Case> getMesCasesAccessibles() {
        List<Case> accessibles = new ArrayList<>();

        if (!estInitialisationValide()) return accessibles;

        Point pos = plateau.getPositionCase(piece.getCase());
        if (pos == null) return accessibles;

        int direction = piece.estBlanc() ? -1 : 1; // blanc monte, noir descend

        // Case en avant
        int x = pos.x;
        int y = pos.y + direction;
        if (estDansPlateau(x, y)) {
            Case avant = plateau.getCases()[x][y];
            if (avant.getPiece() == null) {
                accessibles.add(avant);

                // Premier coup : peut avancer de 2
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

        // Capture diagonale gauche
        x = pos.x - 1;
        y = pos.y + direction;
        if (estDansPlateau(x, y)) {
            Case diagG = plateau.getCases()[x][y];
            if (diagG.getPiece() != null && diagG.getPiece().estBlanc() != piece.estBlanc()) {
                accessibles.add(diagG);
            }
        }

        // Capture diagonale droite
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

    private boolean estInitialisationValide() {
        return piece != null && plateau != null && piece.getCase() != null;
    }

    private boolean estDansPlateau(int x, int y) {
        return x >= 0 && x < Plateau.SIZE_X && y >= 0 && y < Plateau.SIZE_Y;
    }
}
