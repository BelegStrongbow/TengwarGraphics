package tengwarGraphics.mainView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import tengwarGraphics.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static tengwarGraphics.Action.*;
import static tengwarGraphics.FilterEnum.*;
import static tengwarGraphics.ImageManager.*;
import static tengwarGraphics.Main.*;

public class MainController implements Initializable {

    private Main main;
    @FXML
    TextField userstext;

    @FXML
    Canvas canvas;

    @FXML
    CheckBox textplacing;

    @FXML
    ColorPicker fontcolor, backcolor;

    @FXML
    ComboBox<Integer> fontsize;

    @FXML
    ComboBox<FilterEnum> filterComboBox;

    @FXML
    ComboBox<TengwarFont> fonttypes;

    @FXML
    Button undo, redo;


    HashMap<FilterEnum, Double[][]> kernels = new HashMap<FilterEnum, Double[][]>();

    @FXML
    private void goSavedImages() throws IOException {
        loadSavedImages();
    }

    @FXML
    private void drawImage() throws IOException {

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        drawBackground(graphicsContext);
        if (currentTengwarImage.textOnTopOfFilters==false) drawText(graphicsContext);
        for (int i = 0; i < currentTengwarImage.filters.size(); i++) {
            applyFilter(graphicsContext, currentTengwarImage.filters.get(i));
        }
        if (currentTengwarImage.textOnTopOfFilters==true) drawText(graphicsContext);

/*        graphicsContext.setFont(currentTengwarImage.tengwarText.getFont());
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);
        graphicsContext.setStroke(currentTengwarImage.tengwarText.getColor());
        graphicsContext.setFill(currentTengwarImage.tengwarText.getColor());

        graphicsContext.fillText(
                currentTengwarImage.tengwarText.getText(),
                currentTengwarImage.tengwarText.getX(),
                currentTengwarImage.tengwarText.getY()
        );*/

        if(currentImages.size()<2) undo.setDisable(true);
        if(currentImages.size()>1) undo.setDisable(false);
        if(undoneImages.size()<1) redo.setDisable(true);
        if(undoneImages.size()>0) redo.setDisable(false);
    }

    @FXML
    void drawText(GraphicsContext graphicsContext) throws IOException {
        graphicsContext.setFont(currentTengwarImage.tengwarText.getFont());
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);
        graphicsContext.setStroke(currentTengwarImage.tengwarText.getColor());
        graphicsContext.setFill(currentTengwarImage.tengwarText.getColor());

        graphicsContext.fillText(
                currentTengwarImage.tengwarText.getText(),
                currentTengwarImage.tengwarText.getX(),
                currentTengwarImage.tengwarText.getY()
        );
    }

    @FXML
    void changeBackgroundColor() throws IOException{
        if(!currentTengwarImage.background.equals(backcolor.getValue())) {
            TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
            tengwImg.background = backcolor.getValue();
            tengwImg.typeOfBackground = 0;
            tengwImg.actions.add(BACKGROUND);
            imageManager.addImage(tengwImg);
            drawImage();
        }
    }

    @FXML
    void changeBackgroundImage() throws IOException{

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File selectedImage = fileChooser.showOpenDialog(null);

        if (selectedImage!=null){
            if (!selectedImage.toURI().toString().equals(currentTengwarImage.backImageLocation)){
                TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
                tengwImg.backImageLocation = selectedImage.toURI().toString();
                tengwImg.typeOfBackground = 1;
                tengwImg.actions.add(BACKGROUND);
                imageManager.addImage(tengwImg);
                drawImage();
            }
        }
    }

    @FXML
    void drawBackground(GraphicsContext graphicsContext) throws IOException {
        if (currentTengwarImage.typeOfBackground==0){
            graphicsContext.setFill(currentTengwarImage.background);
            graphicsContext.fillRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        }
        else if (currentTengwarImage.typeOfBackground==1){
            Image image = new Image(currentTengwarImage.backImageLocation);
            graphicsContext.drawImage(image, 0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        }
    }

    @FXML
    void addFilter() throws IOException {
        TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
        //tengwImg.filters.add(new Filter(currentTengwarImage.actions.size(), filterComboBox.getValue(), 100, kernels.get(filterComboBox.getValue())));
        tengwImg.actions.add(FILTER);
        tengwImg.filters.add(filterComboBox.getValue());
        imageManager.addImage(tengwImg);
        calculateExtrapolatedSnapshot(canvas.getGraphicsContext2D());
        drawImage();
    }

/*    void addFilter(Filter f) throws IOException {
        TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
        tengwImg.filters.add(f);
        tengwImg.actions.add(FILTER);
        imageManager.addImage(tengwImg);
        drawImage();
    }*/

    @FXML
    void applyFilter(GraphicsContext graphicsContext, FilterEnum filterEnum) throws IOException {
       /* SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        Image snapshot = graphicsContext.getCanvas().snapshot(null, null);
        WritableImage extrapolatedSnapshot = new WritableImage((int) snapshot.getWidth()+2, (int) snapshot.getHeight()+2);
        extrapolatedSnapshot.getPixelWriter().setPixels(1, 1, (int) snapshot.getWidth(), (int) snapshot.getHeight(), snapshot.getPixelReader(), 0, 0);
        for (int i = 0; i < extrapolatedSnapshot.getHeight(); i++) {
            extrapolatedSnapshot.getPixelWriter().setColor(0, i, extrapolatedSnapshot.getPixelReader().getColor(1, i));
            extrapolatedSnapshot.getPixelWriter().setColor((int) extrapolatedSnapshot.getWidth()-1, i, extrapolatedSnapshot.getPixelReader().getColor((int) extrapolatedSnapshot.getWidth()-2, i));
        }
        for (int i = 1; i < extrapolatedSnapshot.getWidth()-1; i++) {
            extrapolatedSnapshot.getPixelWriter().setColor(i, 0, extrapolatedSnapshot.getPixelReader().getColor(i, 1));
            extrapolatedSnapshot.getPixelWriter().setColor(i, (int) extrapolatedSnapshot.getHeight()-1, extrapolatedSnapshot.getPixelReader().getColor(i, (int) extrapolatedSnapshot.getHeight()-2));
        }*/
       // WritableImage filteredGraphics = new WritableImage((int) graphicsContext.getCanvas().getWidth(), (int) graphicsContext.getCanvas().getHeight());
        /*for (int i = 0; i < filteredGraphics.getWidth(); i++) {
            for (int j = 0; j < filteredGraphics.getHeight(); j++) {
                filteredGraphics.getPixelWriter().setColor(i, j, snapshot.getPixelReader().getColor(i, j));
            }
        }
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < filteredGraphics.getHeight(); j++) {
                filteredGraphics.getPixelWriter().setColor(i, j, Color.BLACK);
            }
        }*/

/*
        for (int i = 1; i < extrapolatedSnapshot.getWidth()-2; i++) {
            for (int j = 1; j < extrapolatedSnapshot.getHeight()-2; j++) {
                filteredGraphics.getPixelWriter().setColor(i-1, j-1, calculateColor(extrapolatedSnapshot, i, j, filter.getKernel(), filter.getWeight()));
            }
        }
*/
        Double[][] kernel = kernels.get(filterEnum);

        Image extrapolatedSnapshot = calculateExtrapolatedSnapshot(graphicsContext);
        WritableImage filteredGraphics = new WritableImage((int)extrapolatedSnapshot.getWidth()-2, (int)extrapolatedSnapshot.getHeight()-2);
        int offset = kernel.length/2;

        PixelReader pixelReader = extrapolatedSnapshot.getPixelReader();
        PixelWriter pixelWriter = filteredGraphics.getPixelWriter();

        for (int i = offset; i < extrapolatedSnapshot.getWidth()-offset; i++) {
            for (int j = offset; j < extrapolatedSnapshot.getHeight()-offset; j++) {
                double[] rgb = new double[3];
                for (int k = 0; k < kernel.length; k++) {
                    for (int l = 0; l < kernel[0].length; l++) {
                        int xn = i+k-offset;
                        int yn = j+l-offset;
                        Color old = pixelReader.getColor(xn, yn);
                        rgb[0]+=old.getRed()*kernel[k][l];
                        rgb[1]+=old.getGreen()*kernel[k][l];
                        rgb[2]+=old.getBlue()*kernel[k][l];
                    }
                }
                if (rgb[0]>1) rgb[0]=1;
                if (rgb[1]>1) rgb[1]=1;
                if (rgb[2]>1) rgb[2]=1;

                if (rgb[0]<0) rgb[0]=0;
                if (rgb[1]<0) rgb[1]=0;
                if (rgb[2]<0) rgb[2]=0;
                Color col = new Color(rgb[0], rgb[1], rgb[2], 1.0);
                pixelWriter.setColor(i-1, j-1, col);
            }
        }


        graphicsContext.drawImage(filteredGraphics, 0, 0);
    }

    WritableImage calculateExtrapolatedSnapshot(GraphicsContext graphicsContext) {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        Image snapshot = graphicsContext.getCanvas().snapshot(snapshotParameters, null);
        WritableImage extrapolatedSnapshot = new WritableImage((int) snapshot.getWidth()+2, (int) snapshot.getHeight()+2);
/*        extrapolatedSnapshot.getPixelWriter().setPixels(1, 1, (int) snapshot.getWidth(), (int) snapshot.getHeight(), snapshot.getPixelReader(), 0, 0);
        for (int i = 0; i < extrapolatedSnapshot.getHeight(); i++) {
            extrapolatedSnapshot.getPixelWriter().setColor(0, i, extrapolatedSnapshot.getPixelReader().getColor(1, i));
            extrapolatedSnapshot.getPixelWriter().setColor((int) extrapolatedSnapshot.getWidth()-1, i, extrapolatedSnapshot.getPixelReader().getColor((int) extrapolatedSnapshot.getWidth()-2, i));
        }
        for (int i = 1; i < extrapolatedSnapshot.getWidth()-1; i++) {
            extrapolatedSnapshot.getPixelWriter().setColor(i, 0, extrapolatedSnapshot.getPixelReader().getColor(i, 1));
            extrapolatedSnapshot.getPixelWriter().setColor(i, (int) extrapolatedSnapshot.getHeight()-1, extrapolatedSnapshot.getPixelReader().getColor(i, (int) extrapolatedSnapshot.getHeight()-2));
        }*/
        PixelWriter pixelWriter = extrapolatedSnapshot.getPixelWriter();
        PixelReader pixelReader = extrapolatedSnapshot.getPixelReader();
        pixelWriter.setPixels(1, 1, (int) snapshot.getWidth(), (int) snapshot.getHeight(), snapshot.getPixelReader(), 0, 0);
        for (int i = 1; i < extrapolatedSnapshot.getHeight()-1; i++) {
/*            extrapolatedSnapshot.getPixelWriter().setColor(0, i, extrapolatedSnapshot.getPixelReader().getColor(1, i));
            extrapolatedSnapshot.getPixelWriter().setColor((int) extrapolatedSnapshot.getWidth()-1, i, extrapolatedSnapshot.getPixelReader().getColor((int) extrapolatedSnapshot.getWidth()-2, i));*/
            /*pixelWriter.setColor(0, i, new Color(0, 0, 0, 0.0));
            pixelWriter.setColor((int) extrapolatedSnapshot.getWidth()-1, i, new Color(0, 0, 0, 0.0));*/
            pixelWriter.setColor(0, i, pixelReader.getColor(1, i));
            pixelWriter.setColor((int) extrapolatedSnapshot.getWidth()-1, i, pixelReader.getColor((int) extrapolatedSnapshot.getWidth()-2, i));
        }
        for (int i = 0; i < extrapolatedSnapshot.getWidth(); i++) {
            /*extrapolatedSnapshot.getPixelWriter().setColor(i, 0, new Color(0, 0, 0, 0.0));
            extrapolatedSnapshot.getPixelWriter().setColor(i, (int) extrapolatedSnapshot.getHeight()-1, new Color(0, 0, 0, 0.0));*/
            pixelWriter.setColor(i, 0, pixelReader.getColor(i, 1));
            pixelWriter.setColor(i, (int) extrapolatedSnapshot.getHeight()-1, pixelReader.getColor(i, (int) extrapolatedSnapshot.getHeight()-2));
        }
        return extrapolatedSnapshot;
    }

/*    Color calculateColor(WritableImage extrapolatedSnapshot, int r, int c, Integer[][] matrix, double weight){
        double[] rgb = new double[3];
        for (int i = r-1; i < r+2; i++) {
            for (int j=c-1; j < c+2; j++) {
                rgb[0]=rgb[0]+extrapolatedSnapshot.getPixelReader().getColor(r, c).getRed()*matrix[i-r+1][j-c+1]*weight;
                rgb[1]=rgb[1]+extrapolatedSnapshot.getPixelReader().getColor(r, c).getGreen()*matrix[i-r+1][j-c+1]*weight;
                rgb[2]=rgb[2]+extrapolatedSnapshot.getPixelReader().getColor(r, c).getBlue()*matrix[i-r+1][j-c+1]*weight;
            }
        }
        if (rgb[0]>1) rgb[0]=1;
        if (rgb[1]>1) rgb[1]=1;
        if (rgb[2]>1) rgb[2]=1;

        if (rgb[0]<0) rgb[0]=0;
        if (rgb[1]<0) rgb[1]=0;
        if (rgb[2]<0) rgb[2]=0;

        return new Color(rgb[0], rgb[1], rgb[2], 1.0);
    }*/

    @FXML
    void changeText() throws IOException {
        if(!userstext.getText().equals(currentTengwarImage.tengwarText.getTextOriginal())) {
            //TengwarImage tengwImg = imageManager.cloneImage(currentTengwarImage);
            TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
            tengwImg.tengwarText.setTextOriginal(userstext.getText());
            tengwImg.actions.add(TEXT);
            imageManager.addImage(tengwImg);
            drawImage();
        }
    }

    @FXML
    void changeFontColor() throws IOException {
        if (!currentTengwarImage.tengwarText.getColor().equals(fontcolor.getValue())){
            //TengwarImage tengwImg = imageManager.cloneImage(currentTengwarImage);
            TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
            tengwImg.tengwarText.setColor(fontcolor.getValue());
            tengwImg.actions.add(TEXTCOLOR);
            imageManager.addImage(tengwImg);
            drawImage();
        }
    }

    @FXML
    void changeFontSize() throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Size should be a positive integer.");
        alert.getButtonTypes().remove(ButtonType.CANCEL);
        int size = currentTengwarImage.tengwarText.getSize();
        int newsize=-1;
/*        if (fontsize.getEditor().getText().matches("[0-9]+"))
        newsize = Integer.parseInt(fontsize.getEditor().getText());
        else alert.show();*/
        try {
            newsize = Integer.parseInt(fontsize.getEditor().getText());
        } catch(NumberFormatException e) {
            alert.show();
        }
        if (newsize>0) {
            if(newsize!=size){
                TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
                tengwImg.tengwarText.setSize(newsize);
                tengwImg.actions.add(TEXTSIZE);
                imageManager.addImage(tengwImg);
                drawImage();
            }
        }
        else alert.show();
    }

    @FXML
    void undo() throws IOException {
        imageManager.undoImage();
        drawImage();
    }

    @FXML
    void redo() throws IOException {
        imageManager.redoImage();
        drawImage();
    }

    @FXML
    void changeFontType() throws IOException {
        if (!currentTengwarImage.tengwarText.getFontEnum().equals(fonttypes.getValue())){
            TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
            tengwImg.tengwarText.setFontEnum(fonttypes.getValue());
            tengwImg.actions.add(FONT);
            imageManager.addImage(tengwImg);
            drawImage();
        }
    }

    @FXML
    void exportAsImage() throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG","*.png"));
        fileChooser.setTitle("Export image");

        File file = fileChooser.showSaveDialog(null);

        if (file!=null){
            try{
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setFill(Color.TRANSPARENT);
                canvas.snapshot(snapshotParameters, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void changeTextPlacing() throws IOException{
        TengwarImage tengwImg = new TengwarImage(currentTengwarImage);
        tengwImg.textOnTopOfFilters = !tengwImg.textOnTopOfFilters;
        imageManager.addImage(tengwImg);
        drawImage();
    }

    @FXML
    void saveImageInDatabase() throws SQLException {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Save an image");
        textInputDialog.setContentText("Enter the name of the design.");
        textInputDialog.showAndWait();
        if (!textInputDialog.getEditor().getText().isEmpty() && textInputDialog.getEditor().getText()!=""){
            DatabaseController.saveImageInDatabase(currentTengwarImage, textInputDialog.getEditor().getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Image saved.");
            alert.setTitle("Save an image");
            alert.show();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Integer> fontsizes = new ArrayList<>();
        fontsizes.add(72);
        fontsizes.add(86);
        fontsizes.add(100);
        fontsizes.add(114);
        fontsizes.add(128);
        fontsizes.add(142);
        fontsizes.add(156);
        fontsizes.add(170);
        fontsizes.add(184);
        fontsizes.add(196);

        ObservableList<Integer> fontSizesObservable = FXCollections.observableList(fontsizes);
        fontsize.getItems().addAll(fontSizesObservable);


        kernels.put(SHARPEN, new Double[][]{
                {0.0, -1.0, 0.0},
                {-1.0, 5.0, -1.0},
                {0.0, -1.0, 0.0}
        });

        kernels.put(BLUR, new Double[][]{
                {1.0/9, 1.0/9, 1.0/9},
                {1.0/9, 1.0/9, 1.0/9},
                {1.0/9, 1.0/9, 1.0/9}
        });

        kernels.put(EDGE_ENHANCE, new Double[][]{
                {0.0, 0.0, 0.0},
                {-1.0, 1.0, 0.0},
                {0.0, 0.0, 0.0}
        });

        kernels.put(EDGE_DETECT, new Double[][]{
                {0.0, 1.0, 0.0},
                {1.0, -4.0, 1.0},
                {0.0, 1.0, 0.0}
        });

        kernels.put(EMBOSS, new Double[][]{
                {-2.0, -1.0, 0.0},
                {-1.0, 1.0, 1.0},
                {0.0, 1.0, 2.0}
        });

        


        ObservableList<TengwarFont> tengwarFonts = FXCollections.observableList(Arrays.asList(TengwarFont.values()));
        fonttypes.getItems().addAll(tengwarFonts);

        ObservableList<FilterEnum> filtersObservable = FXCollections.observableList(Arrays.asList(FilterEnum.values()));
        filterComboBox.getItems().addAll(filtersObservable);

        undo.setDisable(true);
        redo.setDisable(true);


 /*       if (imageflag<-1){
            fonttypes.setValue(TengwarFont.ANNATAR);
            fontsize.setValue(114);
            fontcolor.setValue(Color.BLACK);
        }*/

        //if (imageflag==-1){
            userstext.setText(currentTengwarImage.tengwarText.getTextOriginal());
            fonttypes.setValue(currentTengwarImage.tengwarText.getFontEnum());
            fontsize.setValue(currentTengwarImage.tengwarText.getSize());
            fontcolor.setValue(currentTengwarImage.tengwarText.getColor());
            backcolor.setValue(currentTengwarImage.background);
            if (currentTengwarImage.filters.size()>0) filterComboBox.setValue(currentTengwarImage.filters.get(currentTengwarImage.filters.size()-1));
            textplacing.setSelected(currentTengwarImage.textOnTopOfFilters);
            try {
                drawImage();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
     //   }


    }
}
