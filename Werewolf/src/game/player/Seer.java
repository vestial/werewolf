package game.player;
import game.*;
import java.util.Scanner;

/**
 * A Player-Object, representing the Seer.
 *
 * Each night, the Seer is able to select 1 Player and see his role.
 */
public class Seer extends TablePlayer {
	
	public Seer(String name) {
		super(name,"Alive", "Seer", 0, false  );
	}


}