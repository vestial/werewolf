package game.player;
import game.*;

/**
 * A Player-Object, representing a Human.
 *
 * Humans don't have some kind of action in the nighttime, but they will vote at daytime.
 */
public class Human extends TablePlayer {
	
	public Human(String name) {
		super(name,"Alive", "Human", 0, false  );
	}

}