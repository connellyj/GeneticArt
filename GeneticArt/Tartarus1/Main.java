package Tartarus1;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static StackPane rootPane;

    public void start(Stage stage) {
        Text text = new Text("Will display images later.");
        rootPane = new StackPane(text);
        Scene scene = new Scene(rootPane, 512, 512);
        stage.setScene(scene);
        stage.show();

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Tartarus.startGA();
                return null;
            }
        };
        Thread th = new Thread(task);
        th.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}