

package bowling;

import bowling.SinglePlayerGame;
import bowling.MultiPlayerGame;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MultiP implements MultiPlayerGame {
            
        private static final String NextShot  = "Prochain tir : joueur %s, tour n° %d, boule n° %d";
	private static final String End = " Fin de la Partie";	
	private final Map<String, SinglePlayerGame> games;
	private Iterator<String> playerIterator;
	private String joueur;
	private SinglePlayerGame LancerEnCours;
	private boolean jeu= false;
    

    
    public String startNewGame(String[] playerNames) throws Exception {

	if ((playerNames == null) || playerNames.length == 0) {throw new Exception("Pas de joueur");}
	    
		// Efface le jeu précédent
		games.clear();
		
		for (String name : playerNames) {
			games.put(name, new SinglePlayerGame());
		}

		playerIterator = games.keySet().iterator();
		changementDeJoueur();
		
		jeu = true;

		return retour();}
    
	
    
	public String lancer(int nombreDeQuillesAbattues) throws Exception {
            if (!jeu) {
			throw new Exception(End);
		}
		
		LancerEnCours.lancer(nombreDeQuillesAbattues);

		// Si le tour du joueur est terminé
		if (LancerEnCours.isFinished() || LancerEnCours.hasCompletedFrame()) {
			jeu = changementDeJoueur();
		}

		return retour();
	}

        
        private String retour() {
		if (!jeu) {
			return End;
		} else {
			int tour = LancerEnCours.getFrameNumber();
			int ball = LancerEnCours.getNextBallNumber();
			return String.format(NextShot, joueur, tour, ball);
		}
	}
        
        private boolean changementDeJoueur() {
		if (!playerIterator.hasNext()) { 
			if (LancerEnCours.isFinished() ) { // Le dernier joueur a fini
				return false; // Le jeu est terminé
			} else { // On démarre un nouveau tour
				playerIterator = games.keySet().iterator(); // On réinitialise l'itérateur
			}
		}
		joueur = playerIterator.next();
		LancerEnCours = games.get(joueur);
		return true;
	}
        
	public int scoreFor(String playerName) throws Exception{
            SinglePlayerGame game = games.get(playerName);
		
		if (game == null)
			throw new Exception("Joueur non reconnu");
		
		return game.score();
	}
}
