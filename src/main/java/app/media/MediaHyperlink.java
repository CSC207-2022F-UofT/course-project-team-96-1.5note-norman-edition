package app.media;


public class MediaHyperlink extends Media{
    //A subclass of PageMedia, defining a Hyperlink that exists on the page
    //Instance Attributes:
    //  text: The GUI representation of the hyperlink in question
    //  link: Either a file path or a timestamp to be linked to
    private String text;
    private String link;

    public MediaHyperlink(String name, double x, double y, double width, double height, String text, String link) {
        super(name, x, y, width, height);
        this.text = text;
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
