package network;

import java.io.*;
import java.net.Socket;


/**
 * Class used to handle server and client's protocol
 * Listen to client as well as send message to that client using Socket
 */
public class ThreadHandler extends Thread {

    private Socket client;
    public String name;
    private Server server;

    private PrintWriter writer;
    private BufferedReader reader;


    public ThreadHandler(Socket client, Server server){
        this.client = client;
        this.server = server;

        try {
            this.writer = new PrintWriter(this.client.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get the print writer of this thread
     * @return : this print writer
     */
    public PrintWriter getWriter() {
        return writer;
    }


    /**
     * Start this Thread and parse message from clients
     */
    public void run() {
        String s ;
        try {
            while ((s = reader.readLine()) != null) {
                if(s.startsWith("setName")) {
                    this.name = s.substring(9,s.length());
                }else if (s.startsWith("SeerCheckDone")){
                   server.getGame().handleSeerDone();
                }
                else if (s.startsWith("WitchHealDone")){
                    server.getGame().handleWitchHealDone(s.substring(14,s.length()));
                }
                else if (s.startsWith("WitchKillDone")){
                    server.getGame().handleWitchKillDone(s.substring(14,s.length()));
                }
                else if (s.startsWith("Vote")){
                    server.getGame().handleVote(this.name,s.substring(5,s.length()));
                }
                else if (s.startsWith("Hunt")){
                    server.getGame().handleActionDone();
                    server.getGame().handleHunt(s.substring(5,s.length()));
                }
                else if (s.startsWith("ActionDone")){
                    server.getGame().handleActionDone();
                }
                else {
                    server.sendToAllClients(this.name + ": "+s+"", server.clients);
                }
            }
            /*
            writer.close();
            reader.close();
            client.close();
            */
        } catch (IOException ioe){
           ioe.printStackTrace();
        }
    }
}
