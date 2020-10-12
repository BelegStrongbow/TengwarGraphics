package tengwarGraphics.mainView;

import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import tengwarGraphics.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static tengwarGraphics.Main.*;

public class MainController {

    private Main main;
    @FXML
    TextField userstext;

    @FXML   Canvas canvas;

    @FXML
    private void goSavedImages() throws IOException {
        loadSavedImages();
    }

    @FXML
    private void drawText() throws IOException {
        tengwarText.setTextOriginal(userstext.getText());

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFont(tengwarText.getFont());
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);

        graphicsContext.fillText(
                tengwarText.getText(),
                tengwarText.getX(),
                tengwarText.getY()
        );
    }
}
