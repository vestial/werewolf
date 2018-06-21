package Werwolf;
import Werwolf.*;

public class Main {
	
	public static void main(String[] args) {
		
		Town town = new Town(4);
		
		Witch p1 = new Witch("Lukas", town);
		Werwolf p2 = new Werwolf("Bac", town);
		Seer p3 = new Seer("Penske", town);
		Human p4 = new Human("Daniel", town);
		
		town.players[0] = p1;
		town.players[1] = p2;
		town.players[2] = p3;
		town.players[3] = p4;
		
		town.game();
	}
}