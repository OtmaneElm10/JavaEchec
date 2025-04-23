package modele.plateau;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import modele.jeu.Piece;

// Décorateur pour gérer les mouvements spécifiques du cavalier
public class DecorateurCasesCavalier extends DecorateurCasesAccessibles {

    // Enumération représentant les 8 directions possibles en L du cavalier
    private enum Direction {
        HAUT_DROITE(1, -2),
        HAUT_GAUCHE(-1, -2),
        DROITE_HAUT(2, -1),
        DROITE_BAS(2, 1),
        BAS_DROITE(1, 2),
        BAS_GAUCHE(-1, 2),
        GAUCHE_HAUT(-2, -1),
        GAUCHE_BAS(-2, 1);

        final int dx, dy;

        // Constructeur pour chaque direction
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    // Constructeur du décorateur : utilise le constructeur parent
    public DecorateurCasesCavalier(DecorateurCasesAccessibles base, Plateau plateau, Piece piece, int limiteur) {
        super(base, plateau, piece, limiteur); 
    }

    // Méthode qui retourne les cases accessibles spécifiquement pour un cavalier
    @Override
    public List<Case> getMesCasesAccessibles() {
        List<Case> casesAccessibles = new ArrayList<>();

        // Vérifie que la pièce et son emplacement sont valides
        if (!estInitialisationValide()) {
            return casesAccessibles;
        }

        // Récupère la position actuelle de la pièce sur le plateau
        Point position = plateau.getPositionCase(piece.getCase());
        if (position == null) {
            return casesAccessibles;
        }

        // Pour chaque direction en L du cavalier, calcule la position cible
        for (Direction dir : Direction.values()) {
            int x = position.x + dir.dx;
            int y = position.y + dir.dy;

            // Vérifie que la position cible reste dans les limites du plateau
            if (estDansPlateau(x, y)) {
                Case caseCible = plateau.getCases()[x][y];

                // Vérifie si la case est libre ou contient une pièce ennemie
                if (estCaseValide(caseCible)) {
                    casesAccessibles.add(caseCible);
                }
            }
        }

        return casesAccessibles;
    }

    // Vérifie que la pièce, le plateau et la case actuelle sont bien définis
    private boolean estInitialisationValide() {
        return piece != null && plateau != null && piece.getCase() != null;
    }

    // Vérifie si les coordonnées correspondent à une case valide dans les limites du plateau
    private boolean estDansPlateau(int x, int y) {
        return x >= 0 && x < Plateau.SIZE_X && y >= 0 && y < Plateau.SIZE_Y;
    }

    // Vérifie si la case est vide ou occupée par une pièce adverse
    private boolean estCaseValide(Case caseCible) {
        return caseCible.getPiece() == null || caseCible.getPiece().estBlanc() != piece.estBlanc();
    }
}
