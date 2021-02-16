package tengwarGraphics;

import javafx.scene.paint.Color;
import tengwarGraphics.mainView.MainController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static tengwarGraphics.TengwarFont.*;

public class ImageManager {
    public static Stack<TengwarImage> currentImages = new Stack<>();
    public static Stack<TengwarImage> undoneImages = new Stack<>();

    public static TengwarImage currentTengwarImage;

    public static ImageManager imageManager = new ImageManager();
    private ImageManager(){
        currentTengwarImage = new TengwarImage(Color.WHITE, new TengwarText("", 114, 710, 335, Color.BLACK, ANNATAR), new ArrayList<>(), "", -1, new ArrayList<>(), false);
        /*currentTengwarImage.tengwarText = new TengwarText(710, 335);
        currentTengwarImage.tengwarText.setFontEnum(ANNATAR);
        currentTengwarImage.tengwarText.setColor(Color.BLACK);
        currentTengwarImage.tengwarText.setSize(114);
        currentTengwarImage.tengwarText.setTextOriginal("");*/
        currentImages.push(currentTengwarImage);
    }

    public void addImage(TengwarImage tengwarImage){
        currentImages.push(tengwarImage);
        undoneImages.clear();
        currentTengwarImage = tengwarImage;
    }

    public void undoImage(){
        undoneImages.push(currentImages.pop());
        currentTengwarImage = currentImages.peek();
        //if(undoneImages.peek().actions.get(undoneImages.peek().actions.size()-1).equals(Action.FILTER)) currentTengwarImage.filters.remove(currentTengwarImage.filters.size()-1);
    }

    public void redoImage(){
        currentImages.push(undoneImages.pop());
        currentTengwarImage = currentImages.peek();
    }

    public void loadImage(TengwarImage tengwarImage){
        currentImages.clear();
        addImage(tengwarImage);
    }

/*    public TengwarImage cloneImage(TengwarImage tengwarImage){
        TengwarImage returnImage = new TengwarImage();
        returnImage.tengwarText = tengwarImage.tengwarText;
        returnImage.background = tengwarImage.background;
        returnImage.actions = tengwarImage.actions;
        return returnImage;
    }*/
}
