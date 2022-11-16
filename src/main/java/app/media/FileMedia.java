package app.media;
import java.util.Set;

public class FileMedia extends Media {
    //A subclass of PageMedia defining Media that are associated with external files
    //Instance Attributes:
    //  rawData: The raw byte data of the media in question

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
