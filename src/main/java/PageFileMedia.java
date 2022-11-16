public class PageFileMedia extends PageMedia {
    //A subclass of PageMedia defining Media that are associated with external files
    //Instance Attributes:
    //  rawData: The raw byte data of the media in question

    private byte[] rawData;


    public PageFileMedia(double[] position, double[] dimensions, double angle, int zIndex, long id, String tag,
                         byte[] rawData) {
        super(position, dimensions, angle, zIndex, id, tag);
        this.rawData = rawData;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}