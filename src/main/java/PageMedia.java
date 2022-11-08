public class PageMedia {
    private double[] position, dimensions;
    private double angle;
    private int zIndex;
    private String name;
    private String tag;

    private byte[] rawData;



    public PageMedia(double[] position, double[] dimensions, double angle, int zIndex, String name, String tag,
                     byte[] rawData) {
        this.position = position;
        this.dimensions = dimensions;
        this.angle = angle;
        this.zIndex = zIndex;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public byte[] getRawData() {
        return rawData;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}
