package gui.tool.app.media;

public class Hyperlink extends TextBox {

    private String link = null;

    public Hyperlink(double x, double y, String text, String link, String colour){
        super(x, y, text, colour);
        this.link = link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }
}
