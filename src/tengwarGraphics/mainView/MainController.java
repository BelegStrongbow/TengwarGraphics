package tengwarGraphics.mainView;

import javafx.fxml.FXML;
import tengwarGraphics.Main;

import java.io.IOException;

public class MainController {

    private Main main;

    @FXML
    private void goSavedImages() throws IOException {
        main.loadSavedImages();
    }
}
