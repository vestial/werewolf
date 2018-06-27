package src;
import src.*;
import java.util.Scanner;

public class Seer extends Player {
	
	public Seer(String name, Town town) {
		super(name, town);
	}
	
	public String toString() {
		if(this.alive) {
			return this.name + "(Seer) is alive";
		} else {
			return this.name + "(Seer) is dead";
		}
	}
	
	public void action () {
		
		//System.out.println("The Seer awakens... ");
		
	}
}