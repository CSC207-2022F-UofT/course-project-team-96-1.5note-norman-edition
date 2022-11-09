package gui.media;

import app.media.*;


/**
 * Simple factory which creates the appropriate GUIMedia object for a given
 * Media object.
 */
public class GUIMediaFactory {

    private GUIMediaFactory() {}

    public static GUIMedia getFor(Media media) throws Exception {
        if (media instanceof PenStroke) {
            return new GUIPenStroke((PenStroke) media);
        } else {
            throw new Exception("No appropriate GUIMedia class for `" + media + "`.");
        }
    }
}
