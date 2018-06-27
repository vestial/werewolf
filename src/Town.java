package src;
import src.*;
import java.util.Scanner;
import java.util.Random;

public class Town {
	
	public int day;
	public int playerCount;
	public int alivePlayers;
	public Player[] players;
	
	public Town(int playerCount) {
		this.players = new Player[playerCount];
		this.day = 0;
		this.alivePlayers = playerCount;
		this.playerCount = playerCount;
		
	}
	
	public Player createPlayer(String name, int i) {
		if(i == 0) {
			Werwolf player = new Werwolf(name, this);
			return player;
		} else if(i == 1) {
			Witch player = new Witch(name, this);
			return player;
		} else if(i == 2) {
			Seer player = new Seer(name, this);
			return player;
		} else {
			Human player = new Human(name, this);
			return player;
		}
	}
	
	public void init() {
		
		String[] playerNames = new String[4];
		playerNames[0] = "Lukas";
		playerNames[1] = "Bac";
		playerNames[2] = "Penske";
		playerNames[3] = "Daniel";
		
		//Werwolf = 0;
		//Witch = 1
		//Seer = 2
		//Human = everything else
		int werwolfCount = 0;
		int witchCount = 0;
		int seerCount = 0;
		int humanCount = 0;
		int rnd;
		Random random = new Random();
		
		//get role
		for(int i = 0; i < 4; i++) {
			while(true) {
				rnd = random.nextInt(4);
				if(rnd == 0) {
					if(werwolfCount != 1) {
						this.players[i] = createPlayer(playerNames[i], rnd);
						werwolfCount++;
						break;
					}
				}
				if(rnd == 1) {
					if(witchCount != 1) {
						this.players[i] = createPlayer(playerNames[i], rnd);
						witchCount++;
						break;
					}
				}
				if(rnd == 2) {
					if(seerCount != 1) {
						this.players[i] = createPlayer(playerNames[i], rnd);
						seerCount++;
						break;
					}
				}
				if(rnd == 3) {
					if(humanCount != 1) {
						this.players[i] = createPlayer(playerNames[i], rnd);
						humanCount++;
						break;
					}
				}
			}
		}
		System.out.println("Players initiated: " + this.players.length);
		for(int i = 0; i < this.players.length; i++) {
			System.out.println(this.players[i].toString());
		}
	}
	
	public void game() {
		while(true) {
			
			nightTimeActions();
			
			dayTimeActions();
			
		}
	}
	
	public void dayTimeActions() {
		this.day++;
		System.out.println("\n\nDay: " + this.day);
		printPlayers();
		System.out.println("\n");
		voting();
		killVotedPlayer();
		checkSituation();
		printPlayers();
	}
	
	public void nightTimeActions() {
		//Seer Move
			for(int i = 0; i < this.players.length; i++) {
				if(players[i].alive && players[i] instanceof Seer) {
					players[i].action();
				}
			}
			
			//Werwolf Move
			for(int i = 0; i < this.players.length; i++) {
				if(players[i].alive && players[i] instanceof Werwolf) {
					players[i].action();
				}
			}
			
			//Witch Move
			for(int i = 0; i < this.players.length; i++) {
				if(players[i].alive && players[i] instanceof Witch) {
					players[i].action();
				}
			}
			//checkSituation();
	}
	
	public void printPlayers() {
		countAlivePlayers();
		System.out.println("Players alive: " + this.alivePlayers);
		for(int i = 0; i < this.players.length; i++) {
				System.out.println(this.players[i].toString());
		}
	}
	
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
	
	
	public void countAlivePlayers() {
		int count = 0;
		for(int i = 0; i < this.players.length; i++) {
			if(players[i].alive) {
				count++;
			}
		}
		this.alivePlayers = count;
	}
	
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
		
		if(werwolfCount == 0) {
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++Humans Won!++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.exit(0);
		}
		if(humanCount == 0) {
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++Werwolves Won!+++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.exit(0);
		}
		
	}
}