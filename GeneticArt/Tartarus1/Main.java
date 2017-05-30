package Tartarus1;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static StackPane rootPane;
    public static boolean shouldRun = true;
    public static Stage mainStage;

    public void start(Stage stage) {
        mainStage = stage;
        Button button = new Button("Exit");
        button.setOnAction((event) -> shouldRun = false);
        VBox vbox = new VBox(button);
        Text text = new Text("Will display images later.");
        rootPane = new StackPane(text);
        vbox.getChildren().add(rootPane);
        Scene scene = new Scene(vbox, 1280, 512);
        stage.setScene(scene);
        stage.show();

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String[] args = new String[1];
                args[0] = "tartarus";
                Tartarus.main(args);
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
