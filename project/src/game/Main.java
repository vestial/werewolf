package src.game;

/**
 * The Main-class.
 *
 * Used to create and initiate a Town-Object and start the game.
 */
public class Main {

	/**
	 * The main-method.
	 *
	 * Creates a Town and starts the game.
	 */
	public static void main(String[] args) {

		Town town = new Town(12);
		
		town.init("Lukas");
		town.game();
	}
}