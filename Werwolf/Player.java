package Werwolf;
import Werwolf.*;
import java.util.Scanner;

public class Player {
	
	public String name;
	public Town town;
	public boolean alive;
	
	public Player(String name, Town town) {
		this.name = name;
		this.town = town;
		this.alive = true;
	}
	
	public void kill() {
		this.alive = false;
	}
	
	public void action() {
		
	}
	
	public int votes() {
		System.out.println(this.name + " voes:");
		Scanner input = new Scanner(System.in);
		int i = input.nextInt();
		return i;
	}
}