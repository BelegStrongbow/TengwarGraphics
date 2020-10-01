package tengwarGraphics.savedImages;

import javafx.fxml.FXML;
import tengwarGraphics.Main;

import java.io.IOException;

public class SavedImagesController {

    private Main main;

    @FXML
    private void goMain() throws IOException {
        main.loadMain();
    }
}
