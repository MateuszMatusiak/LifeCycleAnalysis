package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.lang.*;

import java.io.IOException;

public class PrevMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PrevMain.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Life cycle analysis project");
        stage.setScene(scene);
        stage.setX(stage.getX()-400);
        stage.show();
        stage.setOnCloseRequest(event->System.exit(0));
    }

    public static void main(String[] args) {
        launch();
    }
}