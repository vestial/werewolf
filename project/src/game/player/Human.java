package src.game.player;
import src.game.*;

/**
 * A Player-Object, representing a Human.
 *
 * Humans don't have some kind of action in the nighttime, but they will vote at daytime.
 */
public class Human extends Player {
	
	public Human(String name, Town town) {
		super(name, town);
	}

	/**
	 * The toString-method.
	 *
	 * Returns a String, representing a Human.
	 * Shows name, status (alive or dead) and role.
	 *
	 * @return The Player as a String.
	 */
	public String toString() {
		if(this.alive) {
			return this.playerNumber + ": " + this.name + "(Human) is alive";
		} else {
			return this.playerNumber + ": " + this.name + "(Human) is dead";
		}
	}
}