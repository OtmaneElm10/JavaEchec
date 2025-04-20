package modele.plateau;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import modele.jeu.Piece;

//crea deco
public class DecorateurCasesCavalier extends DecorateurCasesAccessibles {

    //enum des direction en L
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
    
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
    

    public DecorateurCasesCavalier(DecorateurCasesAccessibles base, Plateau plateau, Piece piece, int limiteur) {
        super(base, plateau, piece, limiteur); // même si limiteur n’est pas utilisé
    }

    @Override
    public List <Case> getMesCasesAccessibles() {
        List<Case> casesAccessibles = new ArrayList<>();

        //si probl init 
        if (!estInitialisationValide()) {

            return casesAccessibles;
        }

        //recup la position actuel sur le plateau
        Point position = plateau.getPositionCase(piece.getCase());
        if (position == null) {

            return casesAccessibles;
        }

        for (Direction dir : Direction.values()) {

            int x = position.x + dir.dx; //parcour toutes les direction du cavalier 
            int y = position.y + dir.dy;

            //verifi si la pos futur est dans le plateau 

            if (estDansPlateau(x, y)) {

                Case caseCible = plateau.getCases()[x][y];

                if (estCaseValide(caseCible)) {

                    casesAccessibles.add(caseCible);
                }
            }
        }

        return casesAccessibles;
    }

    //fct qui verifie piece plateau et emplacement ok 
    private boolean estInitialisationValide() {

        return piece != null && plateau != null && piece.getCase() != null;
    }

    // verifie que la position donné est un emplacement ok 
    private boolean estDansPlateau(int x, int y) {

        return x >= 0 && x < Plateau.SIZE_X && y >= 0 && y < Plateau.SIZE_Y;
    }

    private boolean estCaseValide(Case caseCible) {
        
        return caseCible.getPiece() == null || caseCible.getPiece().estBlanc() != piece.estBlanc();
    }
}
