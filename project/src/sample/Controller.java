package src.sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import src.game.*;
import src.game.player.Human;
import src.game.player.Seer;
import src.game.player.Werwolf;
import src.game.player.Witch;


public class Controller implements Initializable {
    @FXML
    private TextField inputText;
    @FXML
    private TextArea outputText;
    @FXML
    private TableView<TablePlayer> tableID;
    /*
    @FXML
    private TableColumn<TablePlayer, String> nameColumn;
    @FXML
    private TableColumn<TablePlayer, String> statusColumn;
    */
    @FXML
    private Label playerNameLabel;
    @FXML
    private Button voteButton, killButton, healButton, checkButton, huntButton, skipButton;
    private String role = "seer";
    private String chatName;
    private Boolean witchKillFlag = false;
    private Boolean witchHealFlag = false;
    private boolean sun = false;
    private int day = 1;
    private Town town;
    private List<TablePlayer> players = new ArrayList<>();
    private int phase;
    private int hunt;
    private int victim;
    private boolean killTarget = true;

    //Developer mode
    @FXML
    private MenuItem TurnOnDeveloperMode;
    @FXML
    private Button werewolf, seer, doctor, villager, nextPhase;
    @FXML
    private ImageView playerImage, timeImage;
    @FXML
    private Label roleLabel, phaseLabel, dayLabel;

    public Controller()
    {

    }

    @FXML
    private void printOutput(){
        String content = outputText.getText() + "\n" + chatName +": " + inputText.getText();
        outputText.setText(content);
    }

    public void setName(String name) {
        playerNameLabel.setText("Name: "+ name);
        chatName = name;
    }

    @FXML
    private void switchPlayer(TablePlayer player) {
        player.setHasVoted(true);
        setName(player.getFirstName());
        switch (player.getRole()) {
            case "Werwolf":
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
    }


    private void appendText(String str) {
        Platform.runLater(() -> outputText.appendText(str));
    }

    @FXML
    private void onStart() {
        Town town = new Town(12);
        town.init(chatName);
        final ObservableList<TablePlayer> data = FXCollections.observableArrayList();
        for (int i = 0; i < town.players.length; i++) {
            if (town.players[i] instanceof Werwolf) {
                data.add(new TablePlayer(town.players[i].name, "Alive", "Werwolf", 0, false));
            } else if (town.players[i] instanceof Witch) {
                data.add(new TablePlayer(town.players[i].name, "Alive", "Witch", 0, false));
            } else if (town.players[i] instanceof Seer) {
                data.add(new TablePlayer(town.players[i].name, "Alive", "Seer", 0, false));
            } else if (town.players[i] instanceof Human) {
                data.add(new TablePlayer(town.players[i].name, "Alive", "Human", 0, false));
            }
        }

        tableID.setItems(data);
        for (TablePlayer player : tableID.getItems()) {
            players.add(player);
        }
        this.town = town;
        nightPhaseSeer();
        //throw new RuntimeException("Text exception");
        //town.game();
    }
    @FXML
    private void dayPhase() {
        if(phase != 0) {
            day++;
        }
        if(killTarget && phase != 0) {
            players.get(victim).setStatus("Dead");
            System.out.println(players.get(victim).getFirstName() + " was found dead in the morning");
        } else {
            killTarget = true;
        }
        checkSituation();
        Image noon = new Image("src/sample/sprites/sun.png");
        phase = 0;
        timeImage.setImage(noon);
        phaseLabel.setText("Day");
        dayLabel.setText("Day " + day);
        sun = true;
        skipButton.setDisable(true);

        for (int i = 0; i < town.players.length; i++) {
            if (!players.get(i).getHasVoted() && players.get(i).getStatus().equals("Alive")) {
                switchPlayer(players.get(i));
                break;
            }
        }

        if (hasAllVoted()) {
            killVotedPlayer();
            checkSituation();
            for (TablePlayer player : players) {
                player.setHasVoted(false);
            }
            nightPhaseSeer();
        }
    }

    private boolean isSeerAlive() {
        for (TablePlayer player : players) {
            if(player.getRole().equals("Seer") && player.getStatus().equals("Alive")) {
                return true;
            }
        }
        return false;
    }

    private boolean isWitchAlive() {
        for (TablePlayer player : players) {
            if(player.getRole().equals("Witch") && player.getStatus().equals("Alive")) {
                return true;
            }
        }
        return false;
    }

    private Boolean hasAllVoted() {
        for (TablePlayer player : players) {
           if(!player.getHasVoted() && player.getStatus().equals("Alive")) {
               return false;
           }
        }
        return true;
    }

    private Boolean hasAllVotedWerewolves() {
        for (TablePlayer player : players) {
            if(!player.getHasVoted() && player.getStatus().equals("Alive") && player.getRole().equals("Werwolf")) {
                return false;
            }
        }
        return true;
    }

    private void killVotedPlayer() {

        int max = 0;
        int index = 0;
        //max
        for(int i = 0; i < this.players.size(); i++) {
            if(this.players.get(i).getVoteAgainst() > max) {
                max = this.players.get(i).getVoteAgainst();
                index = i;
            }
        }

        //count, if there are more than 1 victim
        int count = 0;
        for (TablePlayer player : players) {
            if(player.getVoteAgainst() == max) {
                count++;
            }
        }

        //If there is only one victim, kill it!
        if(count == 1) {
            System.out.println(this.players.get(index).getFirstName() + " got hanged!\n");
            this.players.get(index).setStatus("Dead");
        } else {
            //if there is more than 1 target, kill a random target!
            Random random = new Random();
            int rnd = random.nextInt(count);
            int victim = 0;
            for (TablePlayer player : players) {
                if(player.getVoteAgainst() == max) {
                    if(rnd == victim) {
                        System.out.println(player.getFirstName() + " got hanged!\n");
                        player.setStatus("Dead");
                        break;
                    } else {
                        victim++;
                    }
                }
            }
        }
    }

    private int detectVotedPlayer() {

        int max = 0;
        int index = 0;
        //max
        for(int i = 0; i < this.players.size(); i++) {
            if(this.players.get(i).getVoteAgainst() > max) {
                max = this.players.get(i).getVoteAgainst();
                index = i;
            }
        }

        //count, if there are more than 1 victim
        int count = 0;
        for (TablePlayer player : players) {
            if(player.getVoteAgainst() == max) {
                count++;
            }
        }

        //If there is only one victim, kill it!
        if(count == 1) {
            System.out.println(this.players.get(index).getFirstName() + " is victim!\n");
            return index;
        } else {
            //if there is more than 1 target, kill a random target!
            Random random = new Random();
            int rnd = random.nextInt(count);
            int victim = 0;
            for(int i = 0; i < this.players.size(); i++) {
                if(this.players.get(i).getVoteAgainst() == max) {
                    if(rnd == victim) {
                        System.out.println(this.players.get(i).getFirstName() + " is victim!\n");
                        return i;
                    } else {
                        victim++;
                    }
                }
            }
        }
        return 0;
    }

    private int countAliveWerewolves(){
        int count = 0;
        for (TablePlayer player : players) {
            if(player.getRole().equals("Werwolf") && player.getStatus().equals("Alive")) {
                count++;
            }
        }
        return count;
    }

    private int countAlivePlayers(){
        int count = 0;
        for (TablePlayer player : players) {
            if(player.getStatus().equals("Alive")) {
                count++;
            }
        }
        return count;
    }

    @FXML
    private void nightPhaseSeer() {
        Image night = new Image("src/sample/sprites/moon.png");
        phase = 1;
        timeImage.setImage(night);
        phaseLabel.setText("Night");
        sun = false;
        if(!isSeerAlive()) {
            nightPhaseWerewolf();
        } else {
            int position = -1;
            for(int i = 0; i < players.size(); i++) {
                if(players.get(i).getRole().equals("Seer")) {
                    position = i;
                }
            }
            System.out.println("Active Player: " + players.get(position).getFirstName());
            handleSeer();
            seerEnabler();
            System.out.println("\nTell the Crystal Ball your target!");
        }

    }

    @FXML
    private void nightPhaseWerewolf() {
        Image night = new Image("src/sample/sprites/moon.png");
        for(int i = 0; i < players.size(); i++) {
            players.get(i).setVoteAgainst(0);
        }
        timeImage.setImage(night);
        phase = 2;
        phaseLabel.setText("Night");
        handleWerewolf();
        werewolfEnabler();
        System.out.println("\nThe werewolves are on the hunt...");
        System.out.println(chatName + " hunts: ");
    }

    @FXML
    private void nightPhaseWitch() {
        Image night = new Image("src/sample/sprites/moon.png");
        timeImage.setImage(night);
        phase = 3;
        phaseLabel.setText("Night");
        if(!isWitchAlive()) {
            dayPhase();
        } else {
            int position = -1;
            for(int i = 0; i < players.size(); i++) {
                if(players.get(i).getRole().equals("Witch")) {
                    position = i;
                }
            }
            System.out.println("Active Player: " + players.get(position).getFirstName());
            if (witchHealFlag && witchKillFlag) {
                System.out.println("\nA new day has dawned...");
                dayPhase();
            }
            else if (!witchHealFlag && !witchKillFlag) {
                handleWitch();
                witchEnabler();
                System.out.println("\nDo you want to heal or kill?");
            }
            else if (!witchHealFlag && witchKillFlag) {
                handleWitch();
                witchEnabler();
                killButton.setDisable(true);
                System.out.println("\nDo you want to heal?");
            }
            else if (witchHealFlag && !witchKillFlag) {
                handleWitch();
                witchEnabler();
                healButton.setDisable(true);
                System.out.println("\nDo you want to kill?");
            }
            else {
                System.out.println("A bug has occurred");
            }
        }



    }

    @FXML
    private void onCheck() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        System.out.println("\n" + person.getFirstName() + " is a " + person.getRole() + "!");
        hunt = 0;
        for (TablePlayer player : players) {
            player.setHasVoted(false);
        }
        nightPhaseWerewolf();

    }

    @FXML
    private void onVote() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        if (person.getStatus().equals("Alive")) {
            person.setVoteAgainst(person.getVoteAgainst() + 1);
            System.out.println("You have voted for " + person.getFirstName() + "!");
            System.out.println(person.getVoteAgainst() + " has voted for " + person.getFirstName() + " to be killed.");
            dayPhase();
        }
        else {
            System.out.println("That can't vote on a dead person!");
        }
    }

    @FXML
    private void onKill() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        if (person.getStatus().equals("Alive") && role.equals("werewolf")) {
            person.setStatus("Dead");
            System.out.println(person.getFirstName() + " was found dead in the morning!");
            nightPhaseWitch();
        }
        else if (person.getStatus().equals("Alive") && role.equals("witch")) {
            person.setStatus("Dead");
            System.out.println(person.getFirstName() + " was killed!");
            killButton.setDisable(true);
            witchKillFlag = true;
            nightPhaseWitch();
        }
        else {
            System.out.println("You can't kill a dead person! Please try again.");
        }
    }

    @FXML
    private void onSkip() {
        switch (this.phase) {
            case 0: nightPhaseSeer(); break;
            case 1: nightPhaseWerewolf(); break;
            case 2: break;
            case 3: dayPhase(); break;
            default: dayPhase();
        }
    }

    @FXML
    private void onHunt() {
        TablePlayer person = tableID.getSelectionModel().getSelectedItem();
        //Statement
        person.setVoteAgainst(person.getVoteAgainst() + 1);
        for (int i = 0; i < town.players.length; i++) {
            if (!players.get(i).getHasVoted() && players.get(i).getStatus().equals("Alive") && players.get(i).getRole().equals("Werwolf")) {
                switchPlayer(players.get(i));
                System.out.println("Active Player: " + players.get(i).getFirstName());
                break;
            }
        }
        if(hasAllVotedWerewolves()){
            victim = detectVotedPlayer();
            for(int i = 0; i < players.size(); i++) {
                players.get(i).setVoteAgainst(0);
            }
            nightPhaseWitch();
        }
        /*
        if (hunt == (countAliveWerewolves() - 1)) {
            //Statement
            person.setVoteAgainst(person.getVoteAgainst() + 1);
            victim = detectVotedPlayer();
            for(int i = 0; i < players.size(); i++) {
                players.get(i).setVoteAgainst(0);
            }
            nightPhaseWitch();
        } else {
            //Statement
            person.setVoteAgainst(person.getVoteAgainst() + 1);
            hunt++;
        }
        */

    }

    @FXML
    private void onHeal() {
        killTarget = false;
        System.out.println("Healing successful");
        healButton.setDisable(true);
        witchHealFlag = true;
        nightPhaseWitch();
    }

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

        /*
        //Redirects System.in to the textField
        BlockingQueue<Integer> stdInQueue = new LinkedBlockingQueue<>();
        System.setIn(new InputStream() {

            @Override
            public int read() throws IOException {
                try {
                    int c = stdInQueue.take().intValue();
                    return c;
                } catch (InterruptedException exc) {
                    Thread.currentThread().interrupt();
                    return -1;
                }
            }
        });
        inputText.setOnAction(e -> {
            for (char c : inputText.getText().toCharArray()) {
                stdInQueue.add(new Integer(c));
            }
            stdInQueue.add(new Integer('\n'));
            inputText.clear();
        });

        //For testing if the System.in redirection works

        Thread readThread = new Thread(() -> {
            try {
                int i;
                while ((i = System.in.read()) != 1) {
                    System.out.print((char) i);
                }
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
        readThread.setDaemon(true);
        readThread.start();
        */
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

    @FXML
    private void handleVillager() {
        role = "villager";
        Image image = new Image("src/sample/sprites/man.png");
        roleLabel.setText("Role: Villager");
        playerImage.setImage(image);
    }

    @FXML
    private void handleSeer() {
        role = "seer";
        Image image = new Image("src/sample/sprites/seer_male.png");
        roleLabel.setText("Role: Seer");
        playerImage.setImage(image);

    }

    @FXML
    private void handleDoctor() {
        role = "doctor";
        Image image = new Image("src/sample/sprites/doctor_female.png");
        roleLabel.setText("Role: Doctor");
        playerImage.setImage(image);

    }

    @FXML
    private void handleWerewolf() {
        role = "werewolf";
        Image image = new Image("src/sample/sprites/werewolf.png");
        roleLabel.setText("Role: Werewolf");
        playerImage.setImage(image);

    }

    @FXML
    private void handleWitch() {
        role = "witch";
        Image image = new Image("src/sample/sprites/witch.png");
        roleLabel.setText("Role: Witch");
        playerImage.setImage(image);
    }

    @FXML
    private void handleNextPhase() {
        Image noon = new Image("src/sample/sprites/sun.png");
        Image night = new Image("src/sample/sprites/moon.png");
        if (sun) {
            buttonEnabler();
            String content = outputText.getText() + "\n" + "The night has fallen!";
            outputText.setText(content);
            timeImage.setImage(night);
            phaseLabel.setText("Night");
            sun = false;
        }
        else {
            buttonEnabler();
            String content = outputText.getText() + "\n" + "A new day has dawned!";
            outputText.setText(content);
            timeImage.setImage(noon);
            phaseLabel.setText("Daytime");
            dayLabel.setText("Day " + day);
            sun = true;
        }

    }

    private void buttonEnabler() {
        switch (role) {
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

    @FXML
    private void sleepMode() {
        if (!sun) {
            voteButton.setDisable(true);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        }
        else {
            voteButton.setDisable(true);
            killButton.setDisable(true);
            healButton.setDisable(true);
            checkButton.setDisable(true);
            huntButton.setDisable(true);
            skipButton.setDisable(true);
        }
    }

    private void checkSituation() {

        if((countAlivePlayers() - countAliveWerewolves()) == 0) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++Werwolves Won!+++++++++++++++++");
            System.out.println("+++++++++++++++++++++++Game over!+++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
        if(countAliveWerewolves() == 0) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++Humanity Won!++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++Game over!+++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
    }
}
