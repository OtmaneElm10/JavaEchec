package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.List;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur j1;
    private Joueur j2;
    protected Coup coupRecu;
    private boolean tourBlanc = true; 

    public Jeu() {
        plateau = new Plateau();
        plateau.placerPieces();

        j1 = new Joueur(this);
        j2 = new Joueur(this);

        start();
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public boolean estTourBlanc() {
        return tourBlanc;
    }

    public void envoyerCoup(Coup c) {
        coupRecu = c;

        synchronized (this) {
            notify();
        }

        System.out.println("Coup reçu !");
    }

    public void appliquerCoup(Coup coup) {
        if (coup.dep.getPiece() != null) {
            // Vérifie que la pièce appartient au bon joueur
            if (coup.dep.getPiece().estBlanc() != tourBlanc) {
                System.out.println("Ce n'est pas ton tour !");
                return;
            }
    
            List<Case> casesPossibles = coup.dep.getPiece().getCasesAccessibles();
            if (casesPossibles.contains(coup.arr)) {
               
                plateau.deplacerPiece(coup.dep, coup.arr);
                changerTour(); // ✅ ici !
    
            } else {
                System.out.println("Coup invalide !");
            }
        }
    }
    
    
    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {
        while (true) {
            Coup c = j1.getCoup();
            appliquerCoup(c);
        }
    }

    private void changerTour() {
        tourBlanc = !tourBlanc;
        plateau.setChanged(); // 
        plateau.notifyObservers(tourBlanc); 
    }
}
