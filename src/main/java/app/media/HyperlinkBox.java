package app.media;
import javafx.scene.input.*;


public class HyperlinkBox extends TextBox{

    private String text;
    private String link;

    // using textbox constructor so the hyperlinkbox will have the label of a 'text-box'
    public HyperlinkBox (String text, double x, double y, String link){
        super(x , y, text );
        this.link = link;

    }

    public HyperlinkBox (double x, double y){
        super(x, y);
        this.text = "";
        this.link = null;
    }

    /* in the TextBox superclass
    public void setText(String text){
        this.text = text;
    }

    public String getText (){
        return this.text;
    }
    */

    public void setLink (String link){
        this.link = link;
    }

    public String getLink (){
        return this.link;

    }


}
