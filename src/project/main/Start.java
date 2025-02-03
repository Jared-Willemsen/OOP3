package project.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Start extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String path  = "/project/view/UI.fxml";
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));

        // Set up the scene
        stage.setScene(new Scene(root));
        stage.setTitle("Live Webcam Viewer");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}