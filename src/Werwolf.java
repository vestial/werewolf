package src;
import src.*;
import java.util.Scanner;

public class Werwolf extends Player {
	
	public Werwolf(String name, Town town) {
		super(name, town);
	}
	
	public String toString() {
		if(this.alive) {
			return this.name + "(Werwolf) is alive";
		} else {
			return this.name + "(Werwolf) is dead";
		}
	}
	
	public void action () {
		
	}
}