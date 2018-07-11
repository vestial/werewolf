package src.game.player;
import src.game.*;
import java.util.Scanner;

/**
 * A Player-Object, representing the Witch.
 *
 * A Witch can use Potions with different effects.
 * Once per duel, she can heal the Victim of the werwolves.
 * In addition, she can, once per duel, kill any Player.
 */
public class Witch extends Player {
	
	public boolean healPotion;
	public boolean poisonPotion;
	
	public Witch(String name, Town town) {
		super(name, town);
		this.healPotion = true;
		this.poisonPotion = true;
	}

	/**
	 * The toString-method.
	 *
	 * Returns a String, representing a Witch.
	 * Shows name, status (alive or dead) and role.
	 *
	 * @return The Player as a String.
	 */
	public String toString() {
		if(this.alive) {
			return this.playerNumber + ": " + this.name + "(Witch) is alive";
		} else {
			return this.playerNumber + ": " + this.name + "(Witch) is dead";
		}
	}

	/**
	 * The nighttime-action of the Witch.
	 *
	 * When called, the Witch can heal the victim and/or kill any Player, but each ability once per game.
	 */
	public void action () {

		if(this.healPotion) {
			System.out.println("The victim is " + this.town.players[this.town.victim].name);
			System.out.println("Do you want to heal that person?");
			Scanner input = new Scanner(System.in);
			String s = input.next();
			if(s.equals("y") || s.equals("yes") || s.equals("Y") || s.equals("Yes")) {
				this.town.killTarget = false;
				this.healPotion = false;
			}
		}
		
		if(this.poisonPotion) {
			System.out.println("Do you want to kill someone?");
			Scanner input = new Scanner(System.in);
			String s = input.next();
			if(s.equals("y") || s.equals("yes") || s.equals("Y") || s.equals("Yes")) {
				System.out.println("Who do you want to kill?");
				int poison;
				while(true) {
					poison = input.nextInt();
					if(poison < 0) {
						System.out.println("That person will never come to your town, you know? Please vote again...");
					} else if(poison >= this.town.playerCount) {
						System.out.println("That person will never come to your town, you know? Please vote again...");
					} else if (poison == this.playerNumber) {
						System.out.println("You want to drink your own poison? seriously? Please vote again...");
					} else if (!this.town.players[poison].alive) {
						System.out.println("Dead bodies can't be killed! Please vote again...");
					} else {
						System.out.printf("\n");
						this.town.players[poison].kill();
						this.poisonPotion = false;
						break;
					}
				}
			}
		}
	}
}