package game.player;
import game.*;
import javafx.scene.control.Tab;

import java.util.Scanner;

/**
 * A Player-Object, representing the Witch.
 *
 * A Witch can use Potions with different effects.
 * Once per duel, she can heal the Victim of the werwolves.
 * In addition, she can, once per duel, kill any Player.
 */
public class Witch extends TablePlayer {
	
	public boolean healPotion;
	public boolean poisonPotion;
	
	public Witch(String name) {
		super(name,"Alive", "Witch", 0, false  );
		this.healPotion = true;
		this.poisonPotion = true;
	}


}