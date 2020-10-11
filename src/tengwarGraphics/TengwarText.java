package tengwarGraphics;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;

public class TengwarText extends Text {
    private String textOriginal;
    private int size;
    private Color color;
    private TengwarFont fontEnum;

    public TengwarText(int x, int y){
        super(x,y,"");
        size=128;
        setFontEnum(TengwarFont.ANNATAR);
        setColor(Color.BLACK);

    }

/*    public TengwarText(String text, int size, Color color, TengwarFont font, int x, int y) {
        super("");
        this.fontEnum = font;
        fetchFont(fontEnum);
        this.textOriginal = text;
        this.setText(transcribeParmaite(textOriginal));
        fetchFont(font);
        this.setFont(this.font);

        this.color = color;
        this.x=x;
        this.y=y;

    }*/

    public String getTextOriginal() {
        return textOriginal;
    }

    public void setTextOriginal(String textOriginal) {
        this.textOriginal = textOriginal;
        this.setText(transcribeParmaite(textOriginal));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.setFont(fetchFont(fontEnum));
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setFill(color);
    }

    public TengwarFont getFontEnum() {
        return fontEnum;
    }

    public void setFontEnum(TengwarFont fontEnum) {
        this.fontEnum = fontEnum;
        this.setFont(fetchFont(fontEnum));
    }

    private Font fetchFont(TengwarFont f){
        switch (f) {
            case ANNATAR: return Font.loadFont("file:resources/fonts/tengwar_annatar/tngan.ttf", size);
            case ANNATAR_BOLD: return Font.loadFont("file:resources/fonts/tengwar_annatar/tnganb.ttf", size);
            case ANNATAR_ITALICS: return Font.loadFont("file:resources/fonts/tengwar_annatar/tngani.ttf", size);
            case ANNATAR_BOLD_ITALICS: return Font.loadFont("file:resources/fonts/tengwar_annatar/tnganbi.ttf", size);
        }

        return this.getFont();
    }

    private String transcribeParmaite(String textToTranscribe){
        boolean oneWord = false;
        String result="";
        textToTranscribe.toLowerCase();
        String[] wordsToTranscribe = textToTranscribe.split(" ");
        for (int i = 0; i < wordsToTranscribe.length; i++) {
            switch (wordsToTranscribe[i]){
                case "the": result+="@ "; oneWord=true; break;
                case "of": result+="W "; oneWord=true; break;
                case "and": result+="2è "; oneWord=true; break;
            }
            if (oneWord==true) continue;
            for (int j = 0; j < wordsToTranscribe[i].length(); j++) {
                switch (wordsToTranscribe[i].charAt(j)){
                    case 'a': result+="E"; break;
                    case 'b': result+="w"; break;
                    case 'c': result+="i"; break;
                    case 'd': result+="2"; break;
                    case 'f': result+="e"; break;
                    case 'g': result+="x"; break;
                    case 'h': result+="9"; break;
                    case 'j': result+="s"; break;
                    case 'k': result+="z"; break;
                    case 'l': result+="j"; break;
                    case 'm': result+="t"; break;
                    case 'n': result+="5"; break;
                    case 'p': result+="q"; break;
                    case 'q': result+="q"; break;
                    case 'r': result+="6"; break;
                    case 's': result+="8"; break;
                    case 't': result+="1"; break;
                    case 'v': result+="r"; break;
                    case 'w': result+="y"; break;
                    case 'z': result+="k"; break;
                }
            }
            result+=" ";

        }
        return result;
    }
}
