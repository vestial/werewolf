package Werwolf;
import Werwolf.*;
import java.util.Scanner;

public class Town {
	
	public int day;
	public int alivePlayers;
	public Player[] players;
	
	public Town(int playerCount) {
		this.players = new Player[playerCount];
		this.day = 0;
		this.alivePlayers = playerCount;
		
	}
	
	public void game() {
		while(true) {
			//Night-Time
			
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
			checkSituation();
			
			//Day-Time

			countAlivePlayers();
			this.day++;
			System.out.println("Day: " + this.day);
			System.out.println("Players alive: " + this.alivePlayers);
			for(int i = 0; i < this.players.length; i++) {
				System.out.println(this.players[i].toString());
			}
			System.out.println("\n");
			for(int i = 0; i < this.players.length; i++) {
					int v = players[i].vote();
					System.out.println(v);
			}
			checkSituation();
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
			System.out.println("+++++++++++++++++++++Werwolves Won!+++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.exit(0);
		}
		if(humanCount == 0) {
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++Humans Won!++++++++++++++++++++");
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.exit(0);
		}
		
	}
}