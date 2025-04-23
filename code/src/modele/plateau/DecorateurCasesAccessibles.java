package modele.plateau;

import modele.jeu.Piece;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite pour les décorateurs de cases accessibles.
 * Utilisée pour étendre dynamiquement les mouvements des pièces
 * (ex : diagonale, ligne, mouvement en L, etc.)
 */
public abstract class DecorateurCasesAccessibles {

    // Référence vers le plateau sur lequel la pièce se trouve
    protected Plateau plateau;

    // Référence vers la pièce concernée par le déplacement
    protected Piece piece;

    // Limite du nombre de cases que la pièce peut parcourir dans une direction (-1 = illimité)
    protected int limiteur;

    // Référence vers un décorateur précédent à chaîner (null si ce décorateur est le premier)
    private DecorateurCasesAccessibles base;

    /**
     * Constructeur commun à tous les décorateurs.
     * Initialise le chaînage, la pièce et les contraintes de mouvement.
     *
     * @param _baseDecorateur  le décorateur précédent (ou null)
     * @param _plateau         le plateau sur lequel les pièces sont posées
     * @param _piece           la pièce concernée
     * @param _limiteur        nombre de cases max que la pièce peut parcourir (-1 = infini)
     */
    public DecorateurCasesAccessibles(DecorateurCasesAccessibles _baseDecorateur, Plateau _plateau, Piece _piece, int _limiteur) {
        base = _baseDecorateur;
        plateau = _plateau;
        piece = _piece;
        limiteur = _limiteur;
    }

    /**
     * Récupère toutes les cases accessibles pour la pièce :
     * - en utilisant ce décorateur
     * - et ceux enchaînés avant (s’il y en a)
     *
     * @return une liste de cases accessibles combinée
     */
    public List<Case> getCasesAccessibles() {
        List<Case> retour = getMesCasesAccessibles();

        // Si un décorateur précédent existe, on récupère aussi ses cases
        if (base != null) {
            retour.addAll(base.getCasesAccessibles());
        }

        return retour;
    }

    /**
     * Méthode à implémenter dans chaque décorateur concret (diagonale, ligne, cavalier, etc.)
     * Cette méthode contient la logique propre au type de mouvement.
     *
     * @return la liste des cases accessibles selon ce décorateur uniquement
     */
    public abstract List<Case> getMesCasesAccessibles();
}
