package app.media;

public class FileMedia extends Media {
    /** Subclass of PageMedia defining Media that are associated with external files
     * <p>
     * Aside from regular Media parameters, defined by raw data stored by the referenced file stored as an
     * array of bytes
     */


    private byte[] rawData;

    public FileMedia(String name, double x, double y, double width, double height, byte[] rawData) {
        super(name, x, y, width, height);
        this.rawData = rawData;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}
