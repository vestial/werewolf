package game;
import game.player.*;
import network.GameData;
import network.Server;


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

	public boolean sun;
	public int phase;
	public int day;

	public int playerCount;
	public int alivePlayers;

	public boolean killTarget;
	public int victim;

	public TablePlayer[] tablePlayers;
	public Boolean witchKillFlag = false;
	public Boolean witchHealFlag = false;
	public boolean SeerCheckFlag = false;

	public int actionCount;

	public Server server;
	public int witchKill;


	/**
	 * Creates new Object of this class and assigns that object with a Server
	 *
	 * @param playerCount : The number of Players
	 * @param server	: That assigned server
	 */

	
	public Town(int playerCount, Server server) {
		this.day = 1;
		this.alivePlayers = playerCount;
		this.playerCount = playerCount;

		this.killTarget = true;
		sun = false;
		phase = 1; // night phase
		actionCount = 0;
		this.tablePlayers = new TablePlayer[playerCount];
		this.server = server;
		
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
	public TablePlayer createPlayer(String name, int role) {
		if(role == 0) {
			Werewolf player = new Werewolf(name);
			return player;
		} else if(role == 1) {
			Witch player = new Witch(name);
			return player;
		} else if(role == 2) {
			Seer player = new Seer(name);
			return player;
		} else {
			Human player = new Human(name);
			return player;
		}
	}

	/**
	 * Creates a package of information that contains current state of this Town
	 *
	 * @return created GameDate object
	 */
	public GameData createGameData(){
		GameData data = new GameData(tablePlayers.length);
		data.players = this.tablePlayers;

		data.day = day;
		data.phase = phase;
		data.sun = sun;
		return data;
	}

	/**
	 * Initiates a Game.
	 *
	 * Creates a Player-array from a list of name and randomly chooses the roles of Players
	 * @param data : String array that contains name of players
	 */
	public void init(String[] data) {
		
		String[] playerNames = data;

		//Werewolf = 0;
		//Witch = 1
		//Seer = 2
		//Human = everything else
		int werewolfCount = 0;
		int humanCount = 0;
		int werewolfLimit = (this.playerCount - 1)/3;
		// -2 for seer and witch (1 each)
		int humanLimit = this.playerCount - werewolfLimit - 2;
		int rnd;
		//solo-roles
		Random random = new Random();
		int seerPos = random.nextInt(this.playerCount);
		int witchPos  = random.nextInt(this.playerCount);

		if(seerPos == witchPos) {
			if(seerPos != this.playerCount-1) {
				seerPos = this.playerCount-1;
			} else {
				seerPos = 0;
			}
		}
		
		//get role
		for(int i = 0; i < this.playerCount; i++) {
			if(i == seerPos) {
				this.tablePlayers[i] = createPlayer(playerNames[i], 2);
				continue;
			} else if(i == witchPos) {
				this.tablePlayers[i] = createPlayer(playerNames[i], 1);
				continue;
			} else {
				if(werewolfCount < werewolfLimit) {
					if(this.playerCount > 15) {
						rnd = random.nextInt(25);
						rnd /= 10;
					} else {
						rnd = random.nextInt(3);
					}
					if (rnd == 0) {
						this.tablePlayers[i] = createPlayer(playerNames[i], 0);
						werewolfCount++;
					} else if (humanCount < humanLimit){
						this.tablePlayers[i] = createPlayer(playerNames[i], 3);
						humanCount++;
					} else {
						this.tablePlayers[i] = createPlayer(playerNames[i], 0);
						werewolfCount++;
					}
				} else {
					this.tablePlayers[i] = createPlayer(playerNames[i], 3);
					humanCount++;
				}
			}
		}


		server.sendToAllClients("Human: " + humanCount, server.clients);
		server.sendToAllClients("Werewolf: " + werewolfCount, server.clients);
		server.sendToAllClients("Players initiated: " + this.tablePlayers.length, server.clients);


	}





	/**
	 * The state of the Town.
	 *
	 * Outputs every Player in the Town and their Status.
	 */
	public void printPlayers() {
		countAlivePlayers();
		System.out.println("Players alive: " + this.alivePlayers);
		for(int i = 0; i < this.tablePlayers.length; i++) {
				System.out.println(this.tablePlayers[i].toString());
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
		for(int i = 0; i < this.tablePlayers.length; i++) {
			if(this.tablePlayers[i].getVoteAgainst() > max) {
				max = this.tablePlayers[i].getVoteAgainst();
				index = i;
			}
		}

		//count, if there are more than 1 victim
		int count = 0;
		for (TablePlayer player : tablePlayers) {
			if(player.getVoteAgainst() == max) {
				count++;
			}
		}

		//If there is only one victim, kill it!
		if(count == 1) {
			server.sendToAllClients(this.tablePlayers[index].getFirstName() + " got hanged!\n",server.clients);
			this.tablePlayers[index].setStatus("Dead");
		} else {
			//if there is more than 1 target, kill a random target!
			Random random = new Random();
			int rnd = random.nextInt(count);
			int victim = 0;
			for (TablePlayer player : tablePlayers) {
				if(player.getVoteAgainst() == max) {
					if(rnd == victim) {
						server.sendToAllClients(player.getFirstName() + " got hanged!\n", server.clients);
						player.setStatus("Dead");
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
	 * Will be called every night after the Werewolf-actions.
	 */
	public void detectVotedPlayer() {

		int max = 0;
		int index = 0;
		//max
		for(int i = 0; i < tablePlayers.length; i++) {
			if(tablePlayers[i].getVoteAgainst() > max) {
				max = tablePlayers[i].getVoteAgainst();
				index = i;
			}
		}

		//count, if there are more than 1 victim
		int count = 0;
		for(int i = 0; i < tablePlayers.length; i++) {
			if(tablePlayers[i].getVoteAgainst() == max) {
				count++;
			}
		}

		//If there is only one victim, choose it!
		if(count == 1) {
			this.victim = index;
			server.sendToAllClients(tablePlayers[this.victim].getFirstName() + " is the victim!\n",server.clients);
			return;
		} else {
			//if there is more than 1 target, choose a random target!
			Random random = new Random();
			int rnd = random.nextInt(count);
			int victim = 0;
			for(int i = 0; i < tablePlayers.length; i++) {
				if(tablePlayers[i].getVoteAgainst() == max) {
					if(rnd == victim) {
						this.victim = i;
						server.sendToAllClients(tablePlayers[this.victim].getFirstName() + " is the victim!\n",server.clients);
						break;
					} else {
						victim++;
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
	private int countAlivePlayers(){
		int count = 0;
		for (TablePlayer player : tablePlayers) {
			if(player.getStatus().equals("Alive")) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Evaluates the situation.
	 *
	 * Werwolves win, if there is no Human alive left.
	 * Humanity wins, if there is no Werewolf and at least 1 Human alive left.
	 */
	public boolean checkSituation() {

		if((countAlivePlayers() - countAliveWerewolves()) == 0) {
			String s = "++++++++++++++++++++++\n"+"++++Werewolves Won!++++\n"+"++++++Game over!++++++\n"+"++++++++++++++++++++++\n";
			server.sendToAllClients(s,server.clients);

			return true;
		}
		if(countAliveWerewolves() == 0) {
			String s = "++++++++++++++++++++++\n"+"++++Humanity  Won!++++\n"+"++++++Game over!++++++\n"+"++++++++++++++++++++++\n";
			server.sendToAllClients(s,server.clients);
			return true;
		}
		return false;
	}


	public void handleSeerDone(){
		SeerCheckFlag = true;
	}

	/**
	 * Heal the player that was chosen by witch
	 *
	 * @param playerName : player that will be healed
	 */
	public void handleWitchHealDone(String playerName){
		witchHealFlag = true;
		for(TablePlayer tb : tablePlayers){
			if(tb.getFirstName().equals(playerName)){
				tb.setStatus("Alive");
				if(tb.getVoteAgainst()>0) tb.setVoteAgainst(tb.getVoteAgainst()-1);
				break;
			}
		}
	}

	/**
	 * Kill the player that was chosen by witch
	 *
	 * @param playerName : player that will be killed
	 */
	public void handleWitchKillDone(String playerName){
		witchKillFlag = true;
		for(int i =0; i< tablePlayers.length; i++){
			if(tablePlayers[i].getFirstName().equals(playerName)){
				tablePlayers[i].setStatus("Dead");
				witchKill = i;
				break;
			}
		}
	}

	/**
	 * Check if Seer is still alive
	 *
	 * @return : true when at least 1 Seer is alive
	 */
	private boolean isSeerAlive() {
		for (TablePlayer player : tablePlayers) {
			if(player.getRole().equals("Seer") && player.getStatus().equals("Alive")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if Witch is still alive
	 *
	 * @return : true when at least 1 Witch is alive
	 */
	private boolean isWitchAlive() {
		for (TablePlayer player : tablePlayers) {
			if(player.getRole().equals("Witch") && player.getStatus().equals("Alive")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Count the number of Werwolves alive
	 *
	 * @return : number of Werwolves alive
	 */
	private int countAliveWerewolves(){
		int count = 0;
		for (TablePlayer player : tablePlayers) {
			if(player.getRole().equals("Werewolf") && player.getStatus().equals("Alive")) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Handle the vote from player. Raise the amount of vote by victim and marks the vote player as voted
	 *
	 * @param vote : The player that do this vote action
	 * @param victim : The player that will be voted
	 */
	public void handleVote(String vote, String victim){

		for(TablePlayer tb : tablePlayers){
			if(tb.getFirstName().equals(victim)){
				tb.setVoteAgainst(tb.getVoteAgainst() + 1);
				break;
			}
		}
		for(TablePlayer tb : tablePlayers){
			if(tb.getFirstName().equals(vote)){
				tb.setHasVoted(true);
			}
		}



	}

	/**
	 * handle the hunt action from werewolf.
	 *
	 * @param playerName : Player that will be hunted
	 */
	public void handleHunt(String playerName) {
		for(TablePlayer tb : tablePlayers){
			if(tb.getFirstName().equals(playerName)){
				tb.setVoteAgainst(tb.getVoteAgainst() + 1);
				killTarget = true;
				break;
			}
		}
	}

	/**
	 * Check if all player have voted
	 *
	 * @return : true if all players have voted else false
	 */
	public Boolean hasAllVoted() {
		for (TablePlayer tb : tablePlayers) {
			if(!tb.getHasVoted() && tb.getStatus().equals("Alive")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Count the action that has done in night phase
	 */
	public void handleActionDone(){
		actionCount++;
	}

	/**
	 * Handle night phase for Seer
	 */
	private void nightPhaseSeer() {
		phase = 1;
		sun = false;
		if(!isSeerAlive()) {
			nightPhaseWerewolf();
		} else {
			int position = -1;
			for(int i = 0; i < tablePlayers.length; i++) {
				if(tablePlayers[i].getRole().equals("Seer")) {
					position = i;
				}
			}
			server.sendToAllClients("Active Player: " + tablePlayers[position].getFirstName(), server.clients);
		}

	}

	/**
	 * Handle night phase for Werewolf
	 */

	private void nightPhaseWerewolf() {
		for(int i = 0; i < tablePlayers.length; i++) {
			tablePlayers[i].setVoteAgainst(0);
		}

		phase = 2;

		server.sendToAllClients("\nThe werewolves are on the hunt...", server.clients);
	}

	/**
	 * handle night phase for Witch
	 */
	private void nightPhaseWitch() {
		phase = 3;
		if(!isWitchAlive()) {
			// do nothing
			//dayPhase();
		} else {
			int position = -1;

			for(int i = 0; i < tablePlayers.length; i++) {
				if(tablePlayers[i].getRole().equals("Witch")) {
					position = i;
				}
			}
			server.sendToAllClients("Active Player: " + tablePlayers[position].getFirstName(), server.clients);
			if (witchHealFlag && witchKillFlag) {
				server.sendToAllClients("\nA new day has dawned...\n", server.clients);
				//dayPhase();
			}
			else if (!witchHealFlag && !witchKillFlag) {


			}
			else if (!witchHealFlag && witchKillFlag) {

			}
			else if (witchHealFlag && !witchKillFlag) {

			}
			else {
				System.out.println("A bug has occurred");
			}
		}



	}
}