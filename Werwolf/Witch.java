package Werwolf;
import Werwolf.*;
import java.util.Scanner;

public class Witch extends Player {
	
	public boolean healPotion;
	public boolean poisonPotion;
	
	public Witch(String name, Town town) {
		super(name, town);
		this.healPotion = true;
		this.poisonPotion = true;
	}
	
	public String toString() {
		if(this.alive) {
			return this.name + "(Witch) is alive";
		} else {
			return this.name + "(Witch) is dead";
		}
	}
	
	public void action () {
		
		System.out.println("The Witch awakens... ");
		if(this.healPotion) {
			
		}
		
		if(this.poisonPotion) {
			
		}
	}
}