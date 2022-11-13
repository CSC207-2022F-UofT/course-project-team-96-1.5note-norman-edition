package app.media;

public class TextBox extends Media {

    private String text;

    public TextBox(double x, double y, String text) {
        super("text-box", x, y, 0, 0);
        this.text = text;
    }

    public TextBox(double x, double y) {
        super("text-box", x, y, 0, 0);
        this.text = "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
