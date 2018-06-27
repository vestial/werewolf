package src;
import src.*;
import java.util.Scanner;

public class Player {
	
	public String name;
	public Town town;
	public boolean alive;
	public int votesAgainst;
	
	public Player(String name, Town town) {
		this.name = name;
		this.town = town;
		this.alive = true;
		this.votesAgainst = 0;
	}
	
	public void kill() {
		this.alive = false;
	}
	
	public void action() {
		
	}
	
	public int vote() {
		System.out.println(this.name + " votes:");
		Scanner input = new Scanner(System.in);
		int i = input.nextInt();
		return i;
	}
}