package src;
import src.*;

public class Human extends Player {
	
	public Human(String name, Town town) {
		super(name, town);
	}
	
	public String toString() {
		if(this.alive) {
			return this.name + "(Human) is alive";
		} else {
			return this.name + "(Human) is dead";
		}
	}
}