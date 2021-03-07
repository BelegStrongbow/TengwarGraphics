package tengwarGraphics;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TengwarText extends Text {
    private String vowels = "aeiouy";
    private String doubleLengthTengwas = "2wsx4rfv5tgb";
    private String singleLengthTengwas = "1qaz6yhnAZdcj";
    private String singleLengthExtendedUpwards = "!Q3e";
    private String singleLengthExtendedDownwards = "1qaz!QAZ";
    private String doubleLengthExtendedDownwards = "2wsx@WSX";
    private String punctuationMarks = ",;-.!?)(";
    private String consonants = "bcdfghjklmnpqrstvwxz";
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

    public TengwarText(String textOriginal, int size, double x, double y, Color color, TengwarFont fontEnum){
        super(x, y, "");
        this.setFontEnum(fontEnum);
        this.setSize(size);
        this.setColor(color);
        this.setTextOriginal(textOriginal);
    }

    public TengwarText(TengwarText textToCopy){
        this(textToCopy.textOriginal, textToCopy.size, textToCopy.getX(), textToCopy.getY(), textToCopy.color, textToCopy.fontEnum);
    }


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
            case PARMAITE: return Font.loadFont("file:resources/fonts/Parmaite2/Parmaite.TTF", size);
            case FORMAL: return Font.loadFont("file:resources/fonts/TengwarFormal/TengwarFormal12.ttf", size);
            case QUENYA: return Font.loadFont("file:resources/fonts/tengwar_quenya/QUENYA.TTF", size);
        }

        return this.getFont();
    }

/*    private String transcribeParmaite(String textToTranscribe){
        String result="";
        textToTranscribe = textToTranscribe.toLowerCase();
        String[] wordsToTranscribe = textToTranscribe.split(" ");
        boolean swapped = false;
        for (int i = 0; i < wordsToTranscribe.length; i++) {
            boolean oneWord = false;

            switch (wordsToTranscribe[i]){
                case "the": result+="@ "; oneWord=true; break;
                case "of": result+="W "; oneWord=true; break;
                case "and": result+="2è "; oneWord=true; break;
            }
            if (oneWord==true) continue;
            for (int j = 0; j < wordsToTranscribe[i].length(); j++) {
                boolean wasSwapped = swapped;
                boolean noFollowingConsonant=false;
                String temp = "" + wordsToTranscribe[i].charAt(j);
                if (vowels.contains(temp)) {
                    if (j == wordsToTranscribe[i].length() - 1) {
                        noFollowingConsonant = true;
                        swapped=false;
                    }
                    else {
                        String temp2 = "" + wordsToTranscribe[i].charAt(j + 1);
                        if (temp2=="s") {
                            swapped=false;
                            noFollowingConsonant = true;
                        }
                        else if (vowels.contains(temp2)){
                            noFollowingConsonant = true;
                            swapped=false;
                        }
                        else if (swapped==false) {
                            wordsToTranscribe[i]=wordsToTranscribe[i].substring(0, j) + temp2 + temp + wordsToTranscribe[i].substring(j+2);
                            swapped=true;
                        }
                    }
                }
                if (noFollowingConsonant==true && wasSwapped==false) result+="`";
                if (result.length()>0) temp = "" + result.charAt(result.length()-1);
                switch (wordsToTranscribe[i].charAt(j)){
                    case 'a':
                        if (doubleLengthTengwas.contains(temp)) result+="#";
                        else if (singleLengthTengwas.contains(temp)) result+="E";
                        else if (singleLengthLong.contains(temp)) result+="D";
                        else if (temp.equals("`")) result+="C";
                        else result+="Ñ";
                        break;
                    case 'b': result+="w"; break;
                    case 'c': result+="i"; break;
                    case 'd': result+="2"; break;
                    case 'e':
                        if (doubleLengthTengwas.contains(temp)) result+="$";
                        else if (singleLengthTengwas.contains(temp)) result+="R";
                        else if (singleLengthLong.contains(temp)) result+="F";
                        else if (temp.equals("`")) result+="V";
                        else result+="Š";
                        break;
                    case 'f': result+="e"; break;
                    case 'g': result+="x"; break;
                    case 'h': result+="9"; break;
                    case 'i':
                        if (doubleLengthTengwas.contains(temp)) result+="%";
                        else if (singleLengthTengwas.contains(temp)) result+="T";
                        else if (singleLengthLong.contains(temp)) result+="G";
                        else if (temp.equals("`")) result+="B";
                        else result+="É";
                        break;
                    case 'j': result+="s"; break;
                    case 'k': result+="z"; break;
                    case 'l': result+="j"; break;
                    case 'm': result+="t"; break;
                    case 'n': result+="5"; break;
                    case 'o':
                        if (doubleLengthTengwas.contains(temp)) result+="Ü";
                        else if (singleLengthTengwas.contains(temp)) result+="Ý";
                        else if (singleLengthLong.contains(temp)) result+="Þ";
                        else if (temp.equals("`")) result+="ß";
                        else result+="Í";
                        break;
                    case 'p': result+="q"; break;
                    case 'q': result+="q"; break;
                    case 'r': result+="6"; break;
                    case 's': result+="8"; break;
                    case 't': result+="1"; break;
                    case 'u':
                        if (doubleLengthTengwas.contains(temp)) result+="&";
                        else if (singleLengthTengwas.contains(temp)) result+="U";
                        else if (singleLengthLong.contains(temp)) result+="J";
                        else if (temp.equals("`")) result+="M";
                        else result+="’";
                        break;
                    case 'v': result+="r"; break;
                    case 'w': result+="y"; break;
                    case 'x': result+="d"; break;
                    case 'y':
                        if (doubleLengthTengwas.contains(temp)) result+="è";
                        else if (singleLengthTengwas.contains(temp)) result+="é";
                        else if (singleLengthLong.contains(temp)) result+="ê";
                        else if (temp.equals("`")) result+="ë";
                        else result+="„";
                        break;
                    case 'z': result+="k"; break;
                    case ',': result+="="; break;
                    case ';': result+="-"; break;
                    case '-': result+="-"; break;
                    case '.': result+="-"; break;
                    case '!': result+="Á"; break;
                    case '?': result+="À"; break;
                    case ')': result+=">"; break;
                    case '(': result+=">"; break;
                }
            }
            result+=" ";

        }
        return result;
    }*/

    private String transcribeParmaite(String textToTranscribe){
        String result="";
        textToTranscribe = textToTranscribe.toLowerCase();
        String[] wordsToTranscribe = textToTranscribe.split(" ");
        for (int i = 0; i < wordsToTranscribe.length; i++) {
            boolean oneWord = false;

            switch (wordsToTranscribe[i]){
                case "the": result+="@ "; oneWord=true; break;
                case "of": result+="W "; oneWord=true; break;
                case "and": result+="2è "; oneWord=true; break;
            }
            if (oneWord==true) continue;

            String temporary = "";
            for (int j = 0; j < wordsToTranscribe[i].length(); j++) {
                if (consonants.contains(String.valueOf(wordsToTranscribe[i].charAt(j))) || vowels.contains(String.valueOf(wordsToTranscribe[i].charAt(j))) || punctuationMarks.contains(String.valueOf(wordsToTranscribe[i].charAt(j)))){
                    temporary+=wordsToTranscribe[i].charAt(j);
                }
            }

            wordsToTranscribe[i] = temporary;

            boolean[] swapped = new boolean[wordsToTranscribe[i].length()];

            for (int j = 0; j < wordsToTranscribe[i].length(); j++) {
                String temp = "" + wordsToTranscribe[i].charAt(j);
                if (vowels.contains(temp)){
                    if (swapped[j]==true){
                        temp = "" + result.charAt(result.length()-1);
                        switch (wordsToTranscribe[i].charAt(j)){
                            case 'a':
                                if (doubleLengthTengwas.contains(temp)) result+="#";
                                else if (singleLengthTengwas.contains(temp)) result+="E";
                                else if (singleLengthExtendedUpwards.contains(temp)) result+="D";
                                //else if (temp.equals("`")) result+="C";
                                else result+="Ñ";
                                break;
                            case 'e':
                                if (doubleLengthTengwas.contains(temp)) result+="$";
                                else if (singleLengthTengwas.contains(temp)) result+="R";
                                else if (singleLengthExtendedUpwards.contains(temp)) result+="F";
                                //else if (temp.equals("`")) result+="V";
                                else result+="Š";
                                break;
                            case 'i':
                                if (doubleLengthTengwas.contains(temp)) result+="%";
                                else if (singleLengthTengwas.contains(temp)) result+="T";
                                else if (singleLengthExtendedUpwards.contains(temp)) result+="G";
                                //else if (temp.equals("`")) result+="B";
                                else result+="É";
                                break;
                            case 'o':
                                if (doubleLengthTengwas.contains(temp)) result+="Ü";
                                else if (singleLengthTengwas.contains(temp)) result+="Ý";
                                else if (singleLengthExtendedUpwards.contains(temp)) result+="Þ";
                                //else if (temp.equals("`")) result+="ß";
                                else result+="Í";
                                break;
                            case 'u':
                                if (doubleLengthTengwas.contains(temp)) result+="&";
                                else if (singleLengthTengwas.contains(temp)) result+="U";
                                else if (singleLengthExtendedUpwards.contains(temp)) result+="J";
                                //else if (temp.equals("`")) result+="M";
                                else result+="’";
                                break;
                            case 'y':
                                if (doubleLengthTengwas.contains(temp)) result+="è";
                                else if (singleLengthTengwas.contains(temp)) result+="é";
                                else if (singleLengthExtendedUpwards.contains(temp)) result+="ê";
                                //else if (temp.equals("`")) result+="ë";
                                else result+="„";
                                break;
                        }
                        continue;
                    }
                    else{
                        if(j!=wordsToTranscribe[i].length()-1 && !vowels.contains(String.valueOf(wordsToTranscribe[i].charAt(j+1))) && !punctuationMarks.contains(String.valueOf(wordsToTranscribe[i].charAt(j+1)))){
                            String temp2 = "" + wordsToTranscribe[i].charAt(j+1);
                            wordsToTranscribe[i]=wordsToTranscribe[i].substring(0, j) + temp2 + temp + wordsToTranscribe[i].substring(j+2);
                            swapped[j+1]=true;
                            j--;
                            continue;
                        }
                        else{
                            result+="`";
                            switch (wordsToTranscribe[i].charAt(j)){
                                case 'a':
                                    result+="C";
                                    break;
                                case 'e':
                                    result+="V";
                                    break;
                                case 'i':
                                    result+="B";
                                    break;
                                case 'o':
                                    result+="ß";
                                    break;
                                case 'u':
                                    result+="M";
                                    break;
                                case 'y':
                                    result+="ë";
                                    break;
                            }
                            continue;
                        }
                    }
                }
                else{
                    if(j>0 && wordsToTranscribe[i].charAt(j-1)==wordsToTranscribe[i].charAt(j) && !punctuationMarks.contains(temp)){
                        temp = ""+result.charAt(result.length()-1);
                        if (singleLengthExtendedDownwards.contains(temp)) result+="/";
                        else if (doubleLengthExtendedDownwards.contains(temp)) result+="?";
                        else if (singleLengthTengwas.contains(temp)) result+=";";
                        else if (doubleLengthTengwas.contains(temp)) result+=":";
                    }
                    else {
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
                            case 'x': result+="d"; break;
                            case 'z': result+="k"; break;
                            case ',': result+="="; break;
                            case ';': result+="-"; break;
                            case '-': result+="-"; break;
                            case '.': result+="-"; break;
                            case '!': result+="Á"; break;
                            case '?': result+="À"; break;
                            case ')': result+=">"; break;
                            case '(': result+=">"; break;
                        }
                    }
                }
            }
            result+=" ";
        }
        return result;
    }
}
