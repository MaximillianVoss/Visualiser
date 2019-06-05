package sample;

import DB.Experiment;
import Parse.Parser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1368, 768));
        primaryStage.show();
    }

    public static void Test() {

    }

    public static void main(String[] args) {
        Test();
        launch(args);
    }
}
