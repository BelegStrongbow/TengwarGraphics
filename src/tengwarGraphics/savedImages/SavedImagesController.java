package tengwarGraphics.savedImages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tengwarGraphics.DatabaseController;
import tengwarGraphics.ImageManager;
import tengwarGraphics.Main;
import tengwarGraphics.TengwarImage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static tengwarGraphics.DatabaseController.getImagesFromDatabase;
import static tengwarGraphics.DatabaseController.removeFromDatabase;
import static tengwarGraphics.ImageManager.*;

public class SavedImagesController implements Initializable {


    private Main main;

    @FXML
    TableView<TengwarImage> imagetable;

    @FXML
    TableColumn namecol, datecol;

    @FXML
    private void goMain() throws IOException {
        main.loadMain();
    }

    @FXML
    private void loadImage(){
        try {
            int index = imagetable.getSelectionModel().getSelectedIndex();
            imageManager.loadImage(imagetable.getItems().get(index));
            goMain();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeImage(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete saved design");
        alert.setContentText("Are you sure that you want to delete this design?");
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK)){
                try {
                    int index = imagetable.getSelectionModel().getSelectedIndex();
                    removeFromDatabase(imagetable.getItems().get(index).getDate());
                    loadImagesToTable();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImagesToTable();
    }

    public void loadImagesToTable(){
        ArrayList<TengwarImage> tengwarImages = new ArrayList<>();
        try {
            tengwarImages = getImagesFromDatabase();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        namecol.setCellValueFactory(new PropertyValueFactory("name"));
        datecol.setCellValueFactory(new PropertyValueFactory("date"));
        ObservableList<TengwarImage> data = FXCollections.observableArrayList(tengwarImages);
        imagetable.setItems(data);
    }

}

