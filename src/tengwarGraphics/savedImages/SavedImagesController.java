package tengwarGraphics.savedImages;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import tengwarGraphics.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SavedImagesController implements Initializable {

    private Main main;

    @FXML
    private void goMain() throws IOException {
        main.loadMain();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
