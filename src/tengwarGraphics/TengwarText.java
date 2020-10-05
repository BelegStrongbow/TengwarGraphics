package tengwarGraphics;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;

public class TengwarText {
    private String text, textTranscribed;
    private int size, x, y;
    private Color color;
    private TengwarFont fontEnum;
    private Font font;

    public TengwarText(String text, int size, Color color, TengwarFont font) {
        this.fontEnum = font;
        fetchFont(fontEnum);
        this.text = text;
        this.size = size;
        this.color = color;

    }

    private void fetchFont(TengwarFont f){
        switch (f) {
            case ANNATAR: font = Font.loadFont("file:resources/fonts/tngan.ttf", size); break;
            case ANNATAR_BOLD: font = Font.loadFont("file:resources/fonts/tnganb.ttf", size); break;
            case ANNATAR_ITALICS: font = Font.loadFont("file:resources/fonts/tngani.ttf", size); break;
            case ANNATAR_BOLD_ITALICS: font = Font.loadFont("file:resources/fonts/tnganbi.ttf", size); break;
        }


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

        }
        return result;
    }
}
