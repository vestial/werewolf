package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;

import network.Client;
import network.GameData;
import network.Server;
import game.player.*;


public class Controller implements Initializable {
    @FXML
    private TextField inputText;
    @FXML
    public TextArea outputText;
    @FXML
    private TableView<TablePlayer> tableID;
    @FXML
    private MenuItem startMenu;
    @FXML
    public Label playerNameLabel;
    @FXML
    private Button voteButton, killButton, healButton, checkButton, huntButton, skipButton;

    public String role = "";
    private String chatName;
    private boolean sun = false;
    private int day = 1;
    private String alive = "" ;

    //Developer mode
    @FXML
    private MenuItem TurnOnDeveloperMode;
    @FXML
    private Button werewolf, seer, doctor, villager, nextPhase;
    @FXML
    private ImageView playerImage, timeImage;
    @FXML
    public Label roleLabel;
    @FXML
    private Label phaseLabel;
    @FXML
    private Label dayLabel;

    public Server server;
    public Client client;

    public Controller()
    {
    }

    /**
     * Getter method for start menu item
     * @return this start menu
     */
    public MenuItem getStartMenu(){
        return this.startMenu;
    }

    /**
     * Set the name of the players representing this client
     * @param name : name of the player
     */
    public void setName(String name) {
        playerNameLabel.setText("Name: "+ name);
        chatName = name;
    }

    /**
     * Appends text to the output text.
     * @param str : Text to be appends
     */
    private void appendText(String str) {
        Platform.runLater(() -> outputText.appendText(str));
    }


    /**
     * Show role of the highlighted player in the chat log
     * and send action to the server.
     */
    @FXML
    private void onCheck() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        if(person!=null) {
            appendChat("\n" + person.getFirstName() + " is a " + person.getRole() + "!");
            client.send("SeerCheckDone");
            checkButton.setDisable(true);
        }
    }

    /**
     * Vote action. Increase the number of votes against the highlighted player if that player is alive
     * and send action to the server.
     */
    @FXML
    private void onVote() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        if(person!=null) {
            if (person.getStatus().equals("Alive")) {
                //person.setVoteAgainst(person.getVoteAgainst() + 1);
                appendChat("You have voted for " + person.getFirstName() + "!\n");
                client.send("Vote " + person.getFirstName());
                voteButton.setDisable(true);
            } else {
                appendChat("That can't vote on a dead person!\n");
            }
        }
    }

    /**
     * Kill action of the witch , can be done once per turn.
     * The highlighted player will be selected
     * and send action to server.
     */
    @FXML
    private void onKill() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        if(person!=null) {
            if (person.getStatus().equals("Alive")) {
                person.setStatus("Dead");
                appendChat(person.getFirstName() + " was chosen!\n");
                killButton.setDisable(true);
                client.send("WitchKillDone " + person.getFirstName());
            } else {
                appendChat("You can't kill a dead person! Please try again!\n");
            }
        }
    }

    /**
     * Skip current move or do nothing on this turn
     * and send action to server.
     */
    @FXML
    private void onSkip() {
      client.send("ActionDone");
      skipButton.setDisable(true);
      healButton.setDisable(true);
      killButton.setDisable(true);
      checkButton.setDisable(true);
    }

    /**
     * Hunt action of werewolf during night phase.
     * Increase the number of votes against the highlighted player
     * and send action to server.
     */
    @FXML
    private void onHunt() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        if(person!=null) {
            if (person.getStatus().equals("Alive")) {
                appendChat("You're hunting  " + person.getFirstName() + "!\n");
                client.send("Hunt " + person.getFirstName());
                huntButton.setDisable(true);
            } else {
                appendChat("You can't hunt a dead person!\n");
            }
        }
    }

    /**
     * Heal action of the witch during night phase. Heal the highlighted player
     * and send action to server.
     */
    @FXML
    private void onHeal() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        if(person!=null) {
            appendChat("Healing successful!\n");
            healButton.setDisable(true);
            client.send("WitchHealDone " + person.getFirstName());
        }
    }

    /**
     * Initializes the GUI
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Redirects System.out to textArea
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                appendText(String.valueOf((char) b));
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    //Developer mode methods
    private boolean on = false;
    @FXML
    private void showDeveloperButtons() {
        if (!on) {
            werewolf.setOpacity(1.0);
            seer.setOpacity(1.0);
            doctor.setOpacity(1.0);
            villager.setOpacity(1.0);
            nextPhase.setOpacity(1.0);
            werewolf.setDisable(false);
            seer.setDisable(false);
            doctor.setDisable(false);
            villager.setDisable(false);
            nextPhase.setDisable(false);
            TurnOnDeveloperMode.setText("Turn off developer mode");
            on = true;
        }
        else {
            werewolf.setOpacity(0);
            seer.setOpacity(0);
            doctor.setOpacity(0);
            villager.setOpacity(0);
            nextPhase.setOpacity(0);
            werewolf.setDisable(true);
            seer.setDisable(true);
            doctor.setDisable(true);
            villager.setDisable(true);
            nextPhase.setDisable(true);
            TurnOnDeveloperMode.setText("Turn on developer mode");
            on = false;
        }
    }

    /**
     * Set the image, role description of this player in the GUI
     * if this player is human
     */
    @FXML
    private void handleVillager() {
        role = "villager";
        Image image = new Image("sample/sprites/man.png");
        roleLabel.setText("Role: Villager");
        playerImage.setImage(image);
    }

    /**
     * Set the image, role description of this player in the GUI
     * if this player is seer
     */
    @FXML
    private void handleSeer() {
        role = "seer";
        Image image = new Image("sample/sprites/seer_male.png");
        roleLabel.setText("Role: Seer");
        playerImage.setImage(image);

    }

    /**
     * Set the image, role description of this player in the GUI
     * if this player is doctor
     */
    @FXML
    private void handleDoctor() {
        role = "doctor";
        Image image = new Image("sample/sprites/doctor_female.png");
        roleLabel.setText("Role: Doctor");
        playerImage.setImage(image);

    }

    /**
     * Set the image, role description of this player in the GUI
     * if this player is werewolf
     */
    @FXML
    private void handleWerewolf() {
        role = "werewolf";
        Image image = new Image("sample/sprites/werewolf.png");
        roleLabel.setText("Role: Werewolf");
        playerImage.setImage(image);

    }

    /**
     * Set the image, role description of this player in the GUI
     * if this player is witch
     */
    @FXML
    private void handleWitch() {
        role = "witch";
        Image image = new Image("sample/sprites/witch.png");
        roleLabel.setText("Role: Witch");
        playerImage.setImage(image);
    }

    /**
     * update the GUI with new phase, day counter, buttons setting, image for night and day depends on current phase
     */
    @FXML
    private void handlePhase() {
        Image noon = new Image("sample/sprites/sun.png");
        Image night = new Image("sample/sprites/moon.png");
        if (!sun) {
            if(this.alive.equals("Alive")){
                buttonEnabler();
            }else this.setDisable();

            String content = outputText.getText() + "\n" + "The night has fallen!\n"+"Let the hunt begin!\n";
            outputText.appendText(content);
            timeImage.setImage(night);
            phaseLabel.setText("Night");
            sun = false;
        }
        else {
            if(this.alive.equals("Alive")){
                buttonEnabler();
            }else this.setDisable();

            String content = outputText.getText() + "\n" + "A new day has dawned!\n"+"Start Voting!\n";
            outputText.appendText(content);
            timeImage.setImage(noon);
            phaseLabel.setText("Daytime");
            dayLabel.setText("Day " + day);
            sun = true;
        }

    }

    /**
     * Enable buttons for the village depends on role and day time
     */
    private void buttonEnabler() {
        switch (role) {
            case "witch":
                witchEnabler();
                break;
            case "villager":
                villagerEnabler();
                break;
            case "seer":
                seerEnabler();
                break;
            case "doctor":
                doctorEnabler();
                break;
            case "werewolf":
                werewolfEnabler();
                break;
            default:
                break;
        }
    }

    /**
     * Enable buttons for villager
     */
    @FXML
    private void villagerEnabler() {
        if (!sun) {
            voteButton.setDisable(true);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        }
        else {
            voteButton.setDisable(false);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        }
    }

    /**
     * Enable buttons for seer
     */
    @FXML
    private void seerEnabler() {
        if (!sun) {
            voteButton.setDisable(true);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(false);
            huntButton.setDisable(true);
            skipButton.setDisable(false);
        }
        else {
            voteButton.setDisable(false);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        }
    }

    /**
     * Enable buttons for doctor
     */
    @FXML
    private void doctorEnabler() {
        if (!sun) {
            voteButton.setDisable(false);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
        }
        else {
            voteButton.setDisable(true);
            killButton.setDisable(true);
            healButton.setDisable(false);
            checkButton.setDisable(true);
        }
    }

    /**
     * Enable buttons for werewolf
     */
    @FXML
    private void werewolfEnabler() {
        if (!sun) {
            voteButton.setDisable(true);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(false);
            skipButton.setDisable(true);
        }
        else {
            voteButton.setDisable(false);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        }
    }

    /**
     * Enable buttons for witch
     */
    @FXML
    private void witchEnabler() {
        if (!sun) {
            voteButton.setDisable(true);
            killButton.setDisable(false);
            healButton.setDisable(false);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(false);
        }
        else {
            voteButton.setDisable(false);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        }
    }

    /**
     * Append text to the chat box and request focus on newest text
     * @param s Text to be added
     */
    public void appendChat(String s) {
        Platform.runLater(() -> {
            outputText.appendText(s);
            outputText.requestFocus();
        });
    }

    /**
     * Update game's state on this client when new data arrive
     * then parse this state to update the GUI
     * @param data new Game data
     */
    public void updateTable(GameData data){
        Platform.runLater(() -> {
            final ObservableList<TablePlayer> s = FXCollections.observableArrayList();
            for(TablePlayer tp : data.players){
                s.add(tp);
            }
            tableID.setItems(s);
            this.sun = data.sun;
            this.day = data.day;
            handlePhase();
        });
    }

    /**
     * Start the game. Show a warning if there are not enough player
     */
    public void startGame(){
        if(server.getCurrentPlayer()<server.getMIN_CLIENTS()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Can not create a game with less than 4 players!", ButtonType.OK);
            alert.showAndWait();
        }
        else server.createGame();
    }

    /**
     * Connect the clients of this GUI to server
     * @param port : port to connect
     * @param IP   : IP of the server
     */
    public void connectToServer(int port, String IP) {
            client = new Client(port, IP);
            client.setController(this);
            client.run();
            appendChat("Server connected on port " + port + "\n");
    }

    /**
     * Update role and buttons setting for this client
     * @param s : The role to be set
     */
    public void updateRole(String s){
        this.role = s;
        this.alive ="Alive";
        Platform.runLater(() -> {
            switch (role) {
                case "Werewolf":
                    handleWerewolf();
                    werewolfEnabler();
                    break;
                case "Human":
                    handleVillager();
                    villagerEnabler();
                    break;
                case "Witch":
                    handleWitch();
                    witchEnabler();
                    break;
                case "Seer":
                    handleSeer();
                    seerEnabler();
                    break;
                default:
                    break;
            }
            this.appendChat("You are "+role+"\n");
        });
    }

    /**
     * Create new server to host the game and create parallel a client that assigned to this GUI
     * @param port : port of server
     * @param IPAddress : IP of server
     * @return true if success else false
     * @throws RuntimeException
     */
    @FXML
    public boolean hostGame(int port, String IPAddress) throws  RuntimeException{
        try {
            server = new Server(port);
            new Thread() {
                public void run() {

                    server.run();
                }
            }.start();
            client = new Client(port, IPAddress);
            client.setController(this);
            new Thread(client).start();
            appendChat("Hosting game on port: " + port + " and IP: " + IPAddress + "\n");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Send chat message that user input to the server
     */
    @FXML
    private void sendChat(){
        if(inputText.getText().length()!=0) {
            client.send(inputText.getText());
            inputText.clear();
        }
    }

    /**
     * Disable all buttons. Will be used when the player is dead
     */
    public void setDisable(){
        Platform.runLater(() -> {
            voteButton.setDisable(true);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        });
    }

    public void showInstruction(){
        String content = "Choose the player from the table then using button to take the action.\n" + "Have fun!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.showAndWait();
    }
}