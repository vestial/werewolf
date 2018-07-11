package src.sample.startingScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import src.game.Town;
import src.sample.Controller;

import java.io.IOException;

public class startingScreenController {

    @FXML
    private TextField name, ip, port;
    @FXML
    private Label nameLabel, ipLabel, connectionLabel, portLabel;
    @FXML
    private Button playButton;

    @FXML
    protected void handlePlayButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample.fxml"));
        Parent root = loader.load();
        Scene gameScene = new Scene(root);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        Controller controller = loader.getController();
        window.setScene(gameScene);
        window.show();
        controller.setName(name.getText());
    }

    @FXML
    protected void handleTestConnectionButtonAction() {
        String nameContent = name.getText();
        String ipContent = ip.getText();
        String portContent = port.getText();
        nameLabel.setText(nameLabel.getText() + " " + nameContent);
        ipLabel.setText(ipLabel.getText() + " " + ipContent);
        portLabel.setText(portLabel.getText() + " " + portContent);
        connectionLabel.setOpacity(1.0);
        playButton.setDisable(false);
    }


}
