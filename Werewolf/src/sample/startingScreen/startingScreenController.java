package sample.startingScreen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * Controller class for starting screen.
 */

public class startingScreenController implements Initializable  {

    @FXML
    private TextField name, ip, port;
    @FXML
    private Label nameLabel, ipLabel, connectionLabel, portLabel, localhost;
    @FXML
    private Button joinButton;


    /**
     * An event handler for the host button. The event handler sets the scene to the host scene and
     * the hostGame() method from the controller is called.
     * @param event The action event
     * @throws IOException
     */

    @FXML
    protected void handleHostButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sample.fxml"));
        Parent root = loader.load();
        Scene gameScene = new Scene(root);
        gameScene.getStylesheets().addAll(this.getClass().getResource("/sample/background.css").toExternalForm());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setResizable(false);
        Controller controller = loader.getController();
        window.setScene(gameScene);
        window.show();
        controller.setName(name.getText());


        int port = Integer.parseInt(this.port.getText());
        String IP = ip.getText();

        controller.hostGame(port,IP);

        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
               controller.shutdown();
               System.exit(0);
               Platform.exit();
            }
        });

    }
    /**
     * An event handler for the test connection button. It checks if the name, IP and Port given. "Connection successful"
     * will be printed out if all given data are valid.
     */
    @FXML
    protected void handleTestConnectionButtonAction() {
        String nameContent = name.getText();
        String ipContent = ip.getText();
        String portContent = port.getText();;
        nameLabel.setText(nameLabel.getText() + " " + nameContent);
        ipLabel.setText(ipLabel.getText() + " " + ipContent);
        portLabel.setText(portLabel.getText() + " " + portContent);
        connectionLabel.setOpacity(1.0);
        joinButton.setDisable(false);
    }

    /**
     * An event handler for the join button. The event handler sets the scene to the client scene and
     * the connectToServer() method from the controller is called.
     * @param event The action event
     * @throws IOException
     */

    @FXML
    protected void handleJoinButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sample.fxml"));
        Parent root = loader.load();
        Scene gameScene = new Scene(root);
        gameScene.getStylesheets().addAll(this.getClass().getResource("/sample/background.css").toExternalForm());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setResizable(false);

        Controller controller = loader.getController();
        window.setScene(gameScene);
        window.show();
        controller.setName(name.getText());

        int port = Integer.parseInt(this.port.getText());
        String IP = ip.getText();
        controller.connectToServer(port, IP);
        controller.getStartMenu().setDisable(true);
    }

    /**
     * Initializes the GUI
     * @param location URL
     * @param resources Resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InetAddress IP = InetAddress.getLocalHost();
            localhost.setText("Your IP := "+IP.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
