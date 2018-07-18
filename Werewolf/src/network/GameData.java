package network;


import game.player.*;


/**
 * Create package of Information for a Town object.
 * List of players
 * current day as well as phase and day time
 */
public class GameData {
    public TablePlayer[] players ;
    public int day ;
    public boolean sun;
    public int phase;

    public GameData(int i){
        players = new TablePlayer[i];
        day = 1;
        sun = false;
        phase = 1;
    }
}
