import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;


public class Main extends Application {

    private static String getMessage() {
        return "Come on, fhqwhgads.";
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(300);
        stage.setHeight(100);
        stage.setResizable(true);

        Text text = new Text(getMessage());
        Pane pane = new BorderPane(text);

        stage.setScene(new Scene(pane));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
