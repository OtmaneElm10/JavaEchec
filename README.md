# JavaEchec


pour comprendre la structure du model (plus présisement des décorateur)

Chaque décorateur (DecorateurCasesEnLigne, DecorateurCasesEnDiagonale, etc.) est responsable de calculer les cases accessibles dans un certain type de direction.
C’est la méthode principale que chaque décorateur doit implémenter.
Elle retourne les cases accessibles selon le type de déplacement (ligne, diagonale, etc.).
Il appelle donc une méthode interne (explorerDiagonale) pour explorer une direction, et ajoute les cases accessibles dans une liste.