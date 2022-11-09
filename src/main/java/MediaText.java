public class MediaText extends PageMedia {
    private String text; // TODO: Temp object type, feel free to change as needed.

    public MediaText(double[] position, double[] dimensions, double angle, int zIndex, String name, String tag,
                     byte[] rawData, String text) {
        super(position, dimensions, angle, zIndex, name, tag, rawData);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
