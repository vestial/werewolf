package network;


import com.google.gson.Gson;
import sample.Controller;

import java.io.*;
import java.net.Socket;

/**
 *  Client side of the network
 *  Handle connection with server as well as manager message sending and receiving.
 *  Client has connect to Gui for updating information of Gui
 */

public class Client  implements Runnable{
    private int portToConnect;
    private String IP;

    private Socket client;
    private OutputStream out;
    private InputStream in;
    private PrintWriter writer;
    private BufferedReader reader;

    private Controller controller;


    /**
     * Creates new Client object with specific port and IP Address of server
     * @param port : port to connect
     * @param IP    : IP Address of server
     */
    public Client(int port, String IP){
       this.portToConnect = port;
       this.IP = IP;
    }

    /**
     * Link this client to it's Gui
     *
     * @param controller : controller of the gui , as well as this client
     */
    public void setController(Controller controller){
        this.controller = controller;
    }

    /**
     * Start this client.
     * Create new connection from this client to server.
     * prepare output and input stream to send and receive message from server.
     * Start a new Thread to listen to the server
     */

    public void run() {
        try {
             client = new Socket (IP,portToConnect);
             out = client.getOutputStream();
             writer = new PrintWriter(out);

             in = client.getInputStream();
             reader = new BufferedReader(new InputStreamReader(in));


            String s = controller.playerNameLabel.getText();
            send("setName: "+s.substring(5, s.length()));
            send("has joined the game");

            new Thread(new MessagesFromServerListener()).start();

        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    /**
     * Send a optional message as text to server
     * @param text : message to be sent
     */
    public void send(String text) {
        writer.println(text);
        writer.flush();
    }

    /**
     * A runnable class used to listen and receive message from server .
     * The message will be parsed and handle depends on type of message.
     */
    public class MessagesFromServerListener implements Runnable {

        @Override
        public void run() {
            String message;

            try {
                while((message = reader.readLine()) != null) {
                    if(message.startsWith("setRole")){
                        controller.updateRole(message.substring(9,message.length()));
                    }
                    // parse game data
                    else if (message.startsWith("{")){
                        Gson gson = new Gson();
                        GameData data = gson.fromJson(message, GameData.class);
                        controller.updateTable(data);
                    }
                    else if (message.startsWith("Disable")){
                        controller.setDisable();
                    }else
                    controller.appendChat(message +"\n");
                }
            } catch (IOException e) {
                System.out.println("can not receive message!");
                e.printStackTrace();
            }
        }
    }
}
