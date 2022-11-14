package gui.media;

import app.media.*;


/**
 * Simple factory which creates the appropriate GUIMedia object for a given
 * Media object.
 */
public class GUIMediaFactory {

    private GUIMediaFactory() {}

    /**
     * Instatiate the correct GUIMedia sub-class for the given Media object.
     * <p>
     * When we load a Media object from storage, we don't yet know specifically
     * what type it is, but each type of Media has a different GUI representation,
     * so we need to check the type of the Media and then create an instance of
     * the correct sub-class of GUIMedia.
     */
    public static GUIMedia getFor(Media media) throws Exception {
        if (media instanceof PenStroke) {
            return new GUIPenStroke((PenStroke) media);
        } else if (media instanceof Shape) {
            return new GUIShape((Shape) media);
        } else {
            throw new Exception("No appropriate GUIMedia class for `" + media + "`.");
        }
    }
}
