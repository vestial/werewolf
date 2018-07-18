package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import game.*;

/**
 * Main class for the GUI
 */

public class Main extends Application {

    /**
     * Starts the GUI
     * @param primaryStage Primary stage for the GUI
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("startingScreen/startingScreen.fxml"));
        primaryStage.setTitle("Werewolf");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setResizable(false);
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Launches the GUI
     * @param args Arguments string array
     */

    public static void main(String[] args) {
        launch(args);
    }
}
