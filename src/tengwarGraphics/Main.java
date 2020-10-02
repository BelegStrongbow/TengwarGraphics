package tengwarGraphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.setTitle("Tengwar Graphics");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("mainView/mainView.fxml"));
        GridPane mainLayout = loader.load();
        Scene scene = new Scene(mainLayout, 1920, 1045);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void loadMain() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("mainView/mainView.fxml"));
        GridPane mainLayout = loader.load();
        stage.getScene().setRoot(mainLayout);
    }

    public static void loadSavedImages() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("savedImages/savedImages.fxml"));
        VBox savedImagesLayout = loader.load();
        stage.getScene().setRoot(savedImagesLayout);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
