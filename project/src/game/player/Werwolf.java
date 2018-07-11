package src.game.player;
import src.game.*;
import java.util.Scanner;

/**
 * A Player-Object, representing a Werwolf.
 *
 * Each night, the Werwolves can vote for a Player.
 * Like in daytime, the Player with the most votes against will die.
 * The game ends, if there are only Werwolves left, or if there are no Werwolves left.
 */
public class Werwolf extends Player {
	
	public Werwolf(String name, Town town) {
		super(name, town);
	}

	/**
	 * The toString-method.
	 *
	 * Returns a String, representing a Werwolf.
	 * Shows name, status (alive or dead) and role.
	 *
	 * @return The Player as a String.
	 */
	public String toString() {
		if(this.alive) {
			return this.playerNumber + ": " + this.name + "(Werwolf) is alive";
		} else {
			return this.playerNumber + ": " + this.name + "(Werwolf) is dead";
		}
	}

	/**
	 * The nighttime-action of the Werwolf.
	 *
	 * Similar to vote, but only Werwolves will vote at nighttime.
	 * Cannot target other Werwolves.
	 */
	public void action () {
        System.out.println(this.name + " hunts:");
        Scanner input = new Scanner(System.in);
        int hunt;
        while(true) {
            hunt = input.nextInt();
			if(hunt < 0) {
				System.out.println("Werwolves killing a Pet? Nah! Please vote again...");
			} else if(hunt >= this.town.playerCount) {
                System.out.println("Werwolves killing a Pet? Nah! Please vote again...");
            } else if (hunt == this.playerNumber) {
                System.out.println("Werwolves eating themselves? WTF?!? Please vote again...");
            } else if (!this.town.players[hunt].alive) {
                System.out.println("Eating the dead? You`re a Werwolf, not a Ghoul! Please vote again...");
            } else {
                this.town.players[hunt].votesAgainst++;
                return;
            }
        }
	}
}