package app.media;
import java.util.Set;

public class FileMedia extends Media {
    //A subclass of PageMedia defining Media that are associated with external files
    //Instance Attributes:
    //  rawData: The raw byte data of the media in question

    private byte[] rawData;

    public FileMedia(long id, String name, Set<String> tags, double x, double y,
                         double width, double height, double angle, int zIndex, byte[] rawData) {
        super(id, name, tags, x, y, width, height, angle, zIndex);
        this.rawData = rawData;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}
