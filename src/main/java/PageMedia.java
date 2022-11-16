public class PageMedia {
    //Superclass of different types of Media that will exist on the page
    //Instance Attributes:
    //  position: Position of the object with coordinates relative to the Page it's on
    //  dimension: Size dimensions the GUI element representation will have
    //  zIndex: The Z-index the Media will have on the page (determining how it stacks on other Media)
    //  id: A unique identifier for each PageMedia
    //  tag: A user determined tag for this media

    private double[] position, dimensions;
    private double angle;
    private int zIndex;
    private long id;
    private String tag;

    public PageMedia(double[] position, double[] dimensions, double angle, int zIndex, long id, String tag) {
        this.position = position;
        this.dimensions = dimensions;
        this.angle = angle;
        this.zIndex = zIndex;
        this.id = id;
        this.tag = tag;
    }

    public double[] getPosition() {
        return position;
    }

    public double[] getDimensions() {
        return dimensions;
    }

    public double getAngle() {
        return angle;
    }

    public int getzIndex() {
        return zIndex;
    }

    public long getID() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public void setDimensions(double[] dimensions) {
        this.dimensions = dimensions;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public void setID(long id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}