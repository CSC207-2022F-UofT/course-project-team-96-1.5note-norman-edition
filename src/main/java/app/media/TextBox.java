package app.media;

/**
 * Stores data about Text Box media. Is used by GUITextBox and TextTool to store textboxes, while they manipulate and
 * draw them.
 */
public class TextBox extends Media {

    private String text;

    /**
     * Creates a new TextBox.
     *
     * @param x The X-value of the TextBox's position
     * @param y The Y-value of the TextBox's position
     * @param text The text contained in the textbox
     */
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
