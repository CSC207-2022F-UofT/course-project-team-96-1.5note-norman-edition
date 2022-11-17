package app.media;

public class TextBox extends Media {

    private String text;
    private String link = null;

    public TextBox(double x, double y, String text) {
        super("text-box", x, y, 0, 0);
        this.text = text;
    }

    public TextBox(double x, double y) {
        super("text-box", x, y, 0, 0);
        this.text = "";
    }

    public TextBox(double x, double y, String text, String link){
        super("text-box", x, y, 0, 0);
        this.text = text;
        this.link = link;
        System.out.println(this.link);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setLink(String link){
        this.link = link;

    }

    public String getLink(){
        return this.link;
    }

}
