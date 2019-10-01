/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bowling;

import bowling.SinglePlayerGame;
import bowling.MultiPlayerGame;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 *
 * @author pedago
 */
public class MultiP implements MultiPlayerGame {
            
        private static final String NextShot  = "Prochain tir : joueur %s, tour n° %d, boule n° %d";
	private static final String End = " Fin de la Partie";	
	private final Map<String, SinglePlayerGame> games;
	private Iterator<String> playerIterator;
	private String joueur;
	private SinglePlayerGame LancerEnCours;
	private boolean jeu= false;
    

    
    public String startNewGame(String[] playerNames) throws Exception {

	if ((playerNames == null) || playerNames.length == 0) {
			throw new Exception("Need at least one player");
		}

		games.clear(); // On efface le jeu précédent
		
		// On associe à chaque joueur son jeu
		for (String name : playerNames) {
			games.put(name, new SinglePlayerGame());
		}

		// On initialise le premier joueur
		playerIterator = games.keySet().iterator();
		changementDeJoueur();
		
		// C'est parti !
		jeu = true;

		return retour();}
    
	
	/**
	 * Enregistre le nombre de quilles abattues pour le joueur courant, dans le frame courant, pour la boule courante
	 * @param nombreDeQuillesAbattues : nombre de quilles abattue à ce lancer
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 5, boule n° 2",
	 * ou bien "Partie terminée" si la partie est terminée.
	 * @throws java.lang.Exception si la partie n'est pas démarrée.
	 */
    
	public String lancer(int nombreDeQuillesAbattues) throws Exception {
            if (!jeu) {
			throw new Exception(End);
		}
		
		// On enregistre le lancer courant
		LancerEnCours.lancer(nombreDeQuillesAbattues);

		// Si le tour du joueur est terminé
		if (LancerEnCours.isFinished() || LancerEnCours.hasCompletedFrame()) {
			// On passe au joueur suivant
			jeu = changementDeJoueur();
		}

		return retour();
	}
	
	/**
	 * Donne le score pour le joueur playerName
	 * @param playerName le nom du joueur recherché
	 * @return le score pour ce joueur
	 * @throws Exception si le playerName ne joue pas dans cette partie
	 */
        
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
		if (!playerIterator.hasNext()) { // On a passé tous les joueurs
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
			throw new Exception("Unknown Player");
		
		return game.score();
	}
}
