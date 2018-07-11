package src.game.player;
import src.game.*;
import java.util.Scanner;

/**
 * A Player-Object, representing the Seer.
 *
 * Each night, the Seer is able to select 1 Player and see his role.
 */
public class Seer extends Player {
	
	public Seer(String name, Town town) {
		super(name, town);
	}

	/**
	 * The toString-method.
	 *
	 * Returns a String, representing a Seer.
	 * Shows name, status (alive or dead) and role.
	 *
	 * @return The Player as a String.
	 */
	public String toString() {
		if(this.alive) {
			return this.playerNumber + ": " + this.name + "(Seer) is alive";
		} else {
			return this.playerNumber + ": " + this.name + "(Seer) is dead";
		}
	}

	/**
	 * The nighttime-action of the Seer.
	 *
	 * When called, reveals the role of any other Player.
	 */
	public void action () {
		System.out.println("Tell the Crystal Ball your target!");
		Scanner input = new Scanner(System.in);
		int check;
		while(true) {
			check = input.nextInt();
			if(check < 0) {
				System.out.println("You see a Squirrel in the forest! Please check someone else...");
			} else if(check >= this.town.playerCount) {
				System.out.println("You see a Squirrel in the forest! Please check someone else...");
			} else if (check == this.playerNumber) {
				System.out.println("You see yourself... Surprise, its a Seer! Please vote again...");
			} else if (!this.town.players[check].alive) {
				System.out.println("You see a dead body... well, nothing special! Please vote again...");
			} else {
				if(this.town.players[check] instanceof Werwolf) {
					System.out.println(this.town.players[check].name + " is a Werwolf!");
					return;
				} else if(this.town.players[check] instanceof Seer) {
					System.out.println(this.town.players[check].name + " is a Seer!");
					return;
				} else if(this.town.players[check] instanceof Witch) {
					System.out.println(this.town.players[check].name + " is a Witch!");
					return;
				} else if (this.town.players[check] instanceof Human){
					System.out.println(this.town.players[check].name + " is a Human!");
					return;
				} else {
					System.out.println("It's a bug? It's a feature? No, its SPAGHETTI-CODE!");
					return;
				}
			}
		}
	}
}