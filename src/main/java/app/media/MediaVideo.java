package app.media;

import javafx.util.Duration;

import java.util.ArrayList;

public class MediaVideo extends MediaAudio{
    /** Entity class for storing video
     * <p>
     *  In terms of data stored this is no different from MediaAudio, but it is distinct for other parts of the code
     * </p>
     */

    public MediaVideo(String name, double x, double y, double width, double height, byte[] rawData,
                      ArrayList<Duration> timestamps) {
        super(name, x, y, width, height, rawData, timestamps);
    }

    @Override
    protected void setDefaultTags() {
        getTags().add("video");
    }
}
