package game.player;
import game.*;
import java.util.Scanner;

/**
 * A Player-Object, representing a Werewolf.
 *
 * Each night, the Werwolves can vote for a Player.
 * Like in daytime, the Player with the most votes against will die.
 * The game ends, if there are only Werwolves left, or if there are no Werwolves left.
 */
public class Werewolf extends TablePlayer {
	
	public Werewolf(String name) {
		super(name,"Alive", "Werewolf", 0, false  );

	}


}