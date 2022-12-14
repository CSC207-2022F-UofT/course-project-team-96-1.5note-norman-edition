package app.media;

public class Hyperlink extends MediaText {

    private String link;

    public Hyperlink(double x, double y, String text, String link, String colour){
        super(x, y, text, colour);
        this.link = link;

        getTags().add("URL");
        getTags().add("url");
        getTags().add("hyperlink");
        getTags().add("link");

        setZindex(-1);
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }
}
