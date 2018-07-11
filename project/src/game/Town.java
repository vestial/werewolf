package src.game;
import src.game.player.*;
import java.util.Random;

/**
 * The Town, managing the game.
 *
 * The town represents the whole game.
 * The Players are saved in an array and all their actions will be called here.
 * Will call nighttime-actions and daytime-actions in a strict order in a loop, until the game ends.
 * Games end, if any fraction (Humanity or Werwolves) eliminates the other.
 */
public class Town {
	
	public int day;
	public static int playerCount;
	public static int humanCount;
	public static int werewolfCount;
	public int alivePlayers;
	public Player[] players;
	public boolean killTarget;
	public int victim;
	
	public Town(int playerCount) {
		this.players = new Player[playerCount];
		this.day = 0;
		this.alivePlayers = playerCount;
		this.playerCount = playerCount;
		this.killTarget = true;
		this.humanCount = 0;
		this.werewolfCount = 0;
	}

	/**
	 * Creates a Player.
	 *
	 * Based on input, the created Player will be instance of a subclass of Player.
	 *
	 * @param name The name of the Player
	 * @param role The role of the Player
	 * @return The created Player.
	 */
	public Player createPlayer(String name, int role, int playerNumber) {
		if(role == 0) {
			Werwolf player = new Werwolf(name, this);
			player.playerNumber = playerNumber;
			return player;
		} else if(role == 1) {
			Witch player = new Witch(name, this);
			player.playerNumber = playerNumber;
			return player;
		} else if(role == 2) {
			Seer player = new Seer(name, this);
			player.playerNumber = playerNumber;
			return player;
		} else {
			Human player = new Human(name, this);
			player.playerNumber = playerNumber;
			return player;
		}
	}

	/**
	 * Initiates a Game.
	 *
	 * Creates a Player-array and randomly chooses the roles of Players
	 */
	public void init(String playerName) {
		
		String[] playerNames = new String[30];
		playerNames[0] = playerName;
		playerNames[1] = "Bac";
		playerNames[2] = "Penske";
		playerNames[3] = "Daniel";
		playerNames[4] = "Peter";
		playerNames[5] = "Alice";
		playerNames[6] = "Lennart";
		playerNames[7] = "Fabian";
		playerNames[8] = "Lea";
		playerNames[9] = "Sophie";
		playerNames[10] = "Jan";
		playerNames[11] = "Dennis";
		playerNames[12] = "Malte";
		playerNames[13] = "Manuel";
		playerNames[14] = "Caro";
		playerNames[15] = "Julia";
		playerNames[16] = "Matthias";
		playerNames[17] = "Manuel";
		playerNames[18] = "Larissa";
		playerNames[19] = "Kevin";
		playerNames[20] = "Marie";
		playerNames[21] = "Jonas";
		playerNames[22] = "Hannah";
		playerNames[23] = "Johannes";
		playerNames[24] = "Sarah";
		playerNames[25] = "Florian";
		playerNames[26] = "Bob";
		playerNames[27] = "Cedrik";
		playerNames[28] = "Luisa";
		playerNames[29] = "Eric";
		
		//Werwolf = 0;
		//Witch = 1
		//Seer = 2
		//Human = everything else
		int werwolfCount = 0;
		int humanCount = 0;
		int werwolfLimit = (this.playerCount - 1)/3;
		// -2 for seer and witch (1 each)
		int humanLimit = this.playerCount - werwolfLimit - 2;
		int rnd;
		//solo-roles
		Random random = new Random();
		int seerPos = random.nextInt(this.playerCount);
		int witchPos  = random.nextInt(this.playerCount);

		if(seerPos == witchPos) {
			if(seerPos != this.playerCount) {
				seerPos = this.playerCount;
			} else {
				seerPos = 0;
			}
		}
		
		//get role
		for(int i = 0; i < this.playerCount; i++) {
			if(i == seerPos) {
				this.players[i] = createPlayer(playerNames[i], 2, i);
				continue;
			} else if(i == witchPos) {
				this.players[i] = createPlayer(playerNames[i], 1, i);
				continue;
			} else {
				if(werwolfCount < werwolfLimit) {
					if(this.playerCount > 15) {
						rnd = random.nextInt(25);
						rnd /= 10;
					} else {
						rnd = random.nextInt(3);
					}
					if (rnd == 0) {
						this.players[i] = createPlayer(playerNames[i], 0, i);
						werwolfCount++;
					} else if (humanCount < humanLimit){
						this.players[i] = createPlayer(playerNames[i], 3, i);
						humanCount++;
					} else {
						this.players[i] = createPlayer(playerNames[i], 0, i);
						werwolfCount++;
					}
				} else {
					this.players[i] = createPlayer(playerNames[i], 3, i);
					humanCount++;
				}
			}
		}

		this.humanCount = humanCount;
		this.werewolfCount = werwolfCount;
		System.out.println("Human: " + humanCount);
		System.out.println("Werwolf: " + werwolfCount);

		System.out.println("Players initiated: " + this.players.length);
		for(int i = 0; i < this.players.length; i++) {
			System.out.println(this.players[i].toString());
		}
	}

	/**
	 * The actions at daytime.
	 *
	 * Handles the actions at daytime, including:
	 * 		-voting
	 * 		-killing the voted Players
	 * 		-output the Players and the day
	 * 		-Checking, if any fraction won
	 */
	public void dayTimeActions() {
		System.out.println("\n\nDay: " + this.day);
		printPlayers();
		System.out.println("\n");
		voting();
		killVotedPlayer();
		checkSituation();
		printPlayers();
	}

	/**
	 * The actions at nighttime.
	 *
	 * Handles the actions at nighttime, including:
	 * 		-actions of all roles
	 * 		-Checking, if any fraction won
	 */
	public void nightTimeActions() {
		this.day++;
		System.out.println("\n\nNight: " + this.day);
		//Seer Move
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].alive && players[i] instanceof Seer) {
				System.out.println("The Seer looks into his Crystal Ball...");
				players[i].action();
			}
		}
			
		//Werwolf Move
		//Only part with eventually more than 1 Player.
		System.out.println("\nThe Werwolves are on the hunt...");
		//set voteAgainst to 0 for every Player
		for(int i = 0; i < this.players.length; i++) {
			players[i].votesAgainst = 0;
		}
		//let werwolves vote
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].alive && players[i] instanceof Werwolf) {
				players[i].action();
			}
		}
		detectHuntedPlayer();
			
		//Witch Move
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].alive && players[i] instanceof Witch) {
				System.out.println("\nThe Witch is brewing her potions...");
				players[i].action();
			}
		}
		if(this.killTarget) {
			this.players[victim].kill();
		} else {
			this.killTarget = true;
		}
		checkSituation();
	}

	/**
	 * The state of the Town.
	 *
	 * Outputs every Player in the Town and their Status.
	 */
	public void printPlayers() {
		countAlivePlayers();
		System.out.println("Players alive: " + this.alivePlayers);
		for(int i = 0; i < this.players.length; i++) {
				System.out.println(this.players[i].toString());
		}
	}

	/**
	 * The voting-System.
	 *
	 * All the Players alive in this Town will vote for a Player to die.
	 * The votes are counted and saved.
	 */
	public void voting() {
		//set voteAgainst to 0 for every Player
		for(int i = 0; i < this.players.length; i++) {
				players[i].votesAgainst = 0;
		}
		
		//voting
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].alive) {
				int v = players[i].vote();
				players[v].votesAgainst++;
			}
		}
		
		//print voting result
		System.out.println("\nVotes at day " + this.day + ":");
		for(int i = 0; i < this.players.length; i++) {
				System.out.println(players[i].name + " (" + players[i].votesAgainst + ")");
		}
	}

	/**
	 * Kills the Player with the most votes.
	 *
	 * The Player with the most votes will die.
	 * Will be called every day after voting.
	 */
	public void killVotedPlayer() {

		int max = 0;
		int index = 0;
		//max
		for(int i = 0; i < this.players.length; i++) {
				if(players[i].votesAgainst > max) {
					max = players[i].votesAgainst;
					index = i;
				}
		}

		//count, if there are more than 1 victim
		int count = 0;
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].votesAgainst == max) {
				count++;
			}
		}

		//If there is only one victim, kill it!
		if(count == 1) {
			System.out.println(players[index].name + " got hanged!\n");
			players[index].kill();
		} else {
			//if there is more than 1 target, kill a random target!
			Random random = new Random();
			int rnd = random.nextInt(count);
			int victim = 0;
			for(int i = 0; i < this.players.length; i++) {
				if(players[i].votesAgainst == max) {
					if(rnd == victim) {
						System.out.println(players[i].name + " got hanged!\n");
						players[i].kill();
						break;
					} else {
						victim++;
					}
				}
			}
		}
	}

	/**
	 * Detect the Player with the most votes.
	 *
	 * The Player with the most votes will die.
	 * Will be called every night after the Werwolf-actions.
	 */
	public void detectHuntedPlayer() {

		int max = 0;
		int index = 0;
		//max
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].votesAgainst > max) {
				max = players[i].votesAgainst;
				index = i;
			}
		}

		//count, if there are more than 1 victim
		int count = 0;
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].votesAgainst == max) {
				count++;
			}
		}

		//If there is only one victim, choose it!
		if(count == 1) {
			this.victim = index;
			System.out.println(players[this.victim].name + " is the victim!\n");
			return;
		} else {
			//if there is more than 1 target, choose a random target!
			Random random = new Random();
			int rnd = random.nextInt(count);
			int counter = 0;
			for(int i = 0; i < this.players.length; i++) {
				if(players[i].votesAgainst == max) {
					if(rnd == victim) {
						this.victim = i;
						System.out.println(players[this.victim].name + " is the victim!\n");
						break;
					} else {
						counter++;
					}
				}
			}
		}
	}

	/**
	 * Counts the amount of Players alive.
	 *
	 * If there are 0 Players alive, the Game will end as a win for Werwolves.
	 */
	public void countAlivePlayers() {

		checkSituation();

		int count = 0;
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].alive) {
				count++;
			}
		}
		this.alivePlayers = count;
	}

	/**
	 * Evaluates the situation.
	 *
	 * Werwolves win, if there is no Human alive left.
	 * Humanity wins, if there is no Werwolf and at least 1 Human alive left.
	 */
	public void checkSituation() {
		int werwolfCount = 0;
		int humanCount = 0;
		
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].alive) {
				if(players[i] instanceof Werwolf) {
					werwolfCount++;
				} else {
					humanCount++;
				}
			}
		}

		if(humanCount == 0) {
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++Werwolves Won!+++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.exit(0);
		}
		if(werwolfCount == 0) {
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++Humanity Won!++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.exit(0);
		}
	}

	/**
	 * Runs the Game
	 *
	 * The method controlling the Game. Will call nighttime-actions and daytime-actions in a loop until the Game ends.
	 */
	public void game() {

		while(true) {

			nightTimeActions();

			dayTimeActions();

		}
	}
}