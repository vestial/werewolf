package src.game.player;
import src.game.*;
import java.util.Scanner;

/**
 * A class representing a Player.
 *
 * A Player saves his name, a reference to the Town he exists in, a status (alive or dead) and the amount of
 * votes against him. In addition, a Player always has the methods kill, action and vote.
 * Human, Seer, Werwolf and Witch are instances of Player.
 */
public class Player {
	
	public String name;
	public Town town;
	public boolean alive;
	public int votesAgainst;
	public int playerNumber;
	
	public Player(String name, Town town) {
		this.name = name;
		this.town = town;
		this.alive = true;
		this.votesAgainst = 0;
	}

	/**
	 * Kills the Player.
	 *
	 * Sets the status of the Player to dead.
	 */
	public void kill() {
		this.alive = false;
		System.out.println(this.name + " found dead in the morning...");
	}

	/**
	 * The nighttime-action of Players.
	 *
	 * Used as an abstract method. Other classes will overwrite their action-method to be able to use abilities.
	 */
	public void action() {
		
	}

	/**
	 * The voting function.
	 *
	 * The most important feature. Each day, the Players can vote, which Player should die on that day.
	 * This is the most common win-condition of the humanity.
	 */
	public int vote() {
		System.out.println(this.name + " votes:");
		Scanner input = new Scanner(System.in);
		int vote;
		while(true) {
			vote = input.nextInt();
			if(vote < 0) {
				System.out.println("You succsesfully accused the air around you for murder! Please vote again...");
			} else if(vote >= this.town.playerCount) {
				System.out.println("You succsesfully accused the air around you for murder! Please vote again...");
			} else if (vote == this.playerNumber) {
				System.out.println("Suicide is NOT supported! Please vote again...");
			} else if (!this.town.players[vote].alive) {
				System.out.println("Don't try to kill the dead! Please vote again...");
			} else {
				return vote;
			}
		}
	}
}