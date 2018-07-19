package network;

import com.google.gson.Gson;
import game.Town;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.google.gson.Gson;
import game.player.TablePlayer;

/**
 * Server side of the network.
 * Manage a list of thread to handle connection with multiple clients.
 * Assign one random role for all clients.
 * Create the game's cycle and send game's update to all clients.
 */

public class Server implements Runnable {

    private int port;
    private ServerSocket server;
    private Socket client;
    public ArrayList<ThreadHandler> clients;

    private Town game;
    private int MAX_CLIENTS = 30;
    private int MIN_CLIENTS = 4;
    private int currentPlayer;


    /**
     * Create new Instance of server at destination port.
     * @param port : port used for managing connection .
     */
    public Server(int port) {
        this.port = port;
        clients = new ArrayList<>(MAX_CLIENTS);
        currentPlayer = 0;

    }

    /**
     * get number of current players
     *
     * @return : current number of players
     *
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get the game object of this
     *
     * @return this game that saved in this server
     */
    public Town getGame() {
        return game;
    }

    /**
     * Get the the minimum number of players
     * @return : minimum required number of players
     */
    public int getMIN_CLIENTS() {
        return MIN_CLIENTS;
    }

    /**
     * Get this server socket
     * @return this server socket
     */
    public ServerSocket getServerSocket() {
        return server;
    }

    /**
     * Run this server as Thread at the given port
     */
    public void run() {
       try{
        server = new ServerSocket(port);
        server.setReuseAddress(true); // allow server to bind in TIME_WAIT
        waitForClient();

        } catch(IOException ioe){
           ioe.printStackTrace();
       }
    }

    /**
     * Wait and accept connection from client.
     * Create Thread for each client and save it in a list
     */
    public void waitForClient(){
        while(true) {
            try {
                client = server.accept();

                ThreadHandler thread = new ThreadHandler(client, this);

                this.clients.add(thread);
                thread.start();

                currentPlayer++;

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Create new game for all connected clients
     * after that the game will start.
     */
    public void createGame(){
        // list player
        String[] input = new String[currentPlayer];
        for(int i =0; i<clients.size();i++){
            if(clients.get(i).name != null){
                input[i] = clients.get(i).name;
            }
        }
        game = new Town(currentPlayer,this);
        game.init(input);

        //sendToAllClients(s,clients);
        sendRole();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                nightPhase();
            }
        });
        thread.start();
    }

    /**
     * Send message to all clients
     * @param message : message to be sent
     * @param serverThreads : list of clients
     */
    public void sendToAllClients(String message, ArrayList<ThreadHandler> serverThreads) {
        for(int i = 0; i < serverThreads.size(); i++) {
            ThreadHandler thread = serverThreads.get(i);
            thread.getWriter().println(message);
            thread.getWriter().flush();
        }
    }


    /**
     * Send a command to assign one role for each client
     */
    public void sendRole(){
        for(int i = 0; i< clients.size(); i++){
            clients.get(i).getWriter().println("setRole  "+game.createGameData().players[i].getRole());
            clients.get(i).getWriter().flush();
        }
    }


    /**
     * Run the day's phase of the game
     * 	 Handles the actions at daytime, including:
     * 	 		-voting
     * 	 		-killing the voted Players
     * 	  		-output the Players and the day
     * 	 		-Checking, if any fraction won
     *   Change to next phase when done
     */
    public void dayPhase() {

        if(game.phase != 0) {
            game.day++;
        }

        // kill the victim was hunt by werwolf or witch
        if(game.killTarget && game.phase != 0) {
            game.tablePlayers[game.victim].setStatus("Dead");
            game.killTarget = false;
            sendToAllClients(game.tablePlayers[game.victim].getFirstName() + " was found dead in the morning\n",clients);

        }

        if(game.witchKillFlag){
            game.tablePlayers[game.witchKill].setStatus("Dead");
            game.witchKillFlag=false;
            sendToAllClients(game.tablePlayers[game.witchKill].getFirstName() + " was found dead in the morning\n",clients);
        }

        game.phase = 0;
        game.sun = true;


        if(game.checkSituation()) {
            Gson gson = new Gson();
            GameData data = game.createGameData();
            String s = gson.toJson(data);
            sendToAllClients(s, clients);
            sendToAllClients("Disable",clients);
        }else {

            Gson gson = new Gson();
            GameData data = game.createGameData();
            String s = gson.toJson(data);
            sendToAllClients(s, clients);
            for (int i = 0; i < game.tablePlayers.length; i++) {
                if (game.tablePlayers[i].getStatus().equals("Dead")) sendDisable(clients.get(i));
            }

            while (true) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (game.hasAllVoted()) {
                    game.killVotedPlayer();
                    //game.checkSituation();
                    for (TablePlayer player : game.tablePlayers) {
                        player.setHasVoted(false);
                        player.setVoteAgainst(0);
                    }
                    nightPhase();
                    break;
                }
            }
        }
    }

    /**
     * Run the night's phase of the game
     * 	 Handles the actions at nighttime, including:
     * 	 		-hunting
     * 	 		-killing or healing
     * 	  		-checking info
     * 	 		-Checking, if any fraction won
     *   Change to next phase when done
     */
    public void nightPhase(){
        if(game.phase == 0) {
            game.day++;
        }
        if(game.checkSituation()){
            Gson gson = new Gson();
            String s = gson.toJson(game.createGameData());
            sendToAllClients(s, clients);
            sendToAllClients("Disable",clients);
        }else {
            game.phase = 1;
            game.sun = false;

            Gson gson = new Gson();
            String s = gson.toJson(game.createGameData());
            sendToAllClients(s, clients);

            for (int i = 0; i < game.tablePlayers.length; i++) {
                if (game.tablePlayers[i].getStatus().equals("Dead")){
                    sendDisable(clients.get(i));
                    sendChat("You're dead!\n", clients.get(i));
                }
            }

            int count = 0;
            for (TablePlayer player : game.tablePlayers) {
                if (!player.getRole().equals("Human") && player.getStatus().equals("Alive")) {
                    count++;
                }
            }
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (game.actionCount == count) {
                    game.detectVotedPlayer();

                    for (TablePlayer player : game.tablePlayers) {
                        player.setVoteAgainst(0);
                    }
                /*
				Gson gson1 = new Gson();
				String s1 = gson1.toJson(game.createGameData());
				sendToAllClients(s1,clients);
                */

                    game.actionCount = 0;
                    dayPhase();
                    break;
                }
            }
        }
    }

    /**
     * Send disable command to client.That happends when the player is dead
     * @param client : client that will be sent
     */
    public void sendDisable(ThreadHandler client){
        client.getWriter().println("Disable");
        client.getWriter().flush();
    }


    /**
     * Send Text to one client
     * @param mess : message to be sent
     * @param client : client to be sent
     */
    public void sendChat(String mess, ThreadHandler client){
        client.getWriter().println(mess);
        client.getWriter().flush();
    }
}
