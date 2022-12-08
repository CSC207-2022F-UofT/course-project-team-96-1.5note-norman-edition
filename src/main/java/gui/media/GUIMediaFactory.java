package gui.media;

import app.media.*;


/**
 * Simple factory which creates the appropriate GUIMedia object for a given
 * Media object.
 */
public class GUIMediaFactory {

    private GUIMediaFactory() {}

    /**
     * Instantiate the correct GUIMedia subclass for the given Media object.
     * <p>
     * When we load a Media object from storage, we don't yet know specifically
     * what type it is, but each type of Media has a different GUI representation,
     * so we need to check the type of the Media and then create an instance of
     * the correct subclass of GUIMedia.
     * <p>
     * This is effectively the "one big switch statement" for GUIMedia objects,
     * except it's an if statement because you can't switch
     */
    public static GUIMedia<?> getFor(Media media) throws Exception {
        if (media instanceof PenStroke) {
            // Pen Stroke
            return new GUIPenStroke((PenStroke) media);
        } else if (media instanceof MediaPlayable) {
            // Playable
            if (((MediaPlayable) media).getType().equals("Video")) {
                // Playable Video
                return new GUIVideo((MediaPlayable) media);
            } else {
                // Playable Audio
                return new GUIAudio((MediaPlayable) media);
            }
        } else if (media instanceof  MediaHyperlink) {
            // Hyperlink
            return new GUIHyperlink((MediaHyperlink) media);
        } else if (media instanceof GenericShape) {
            // Shape
            return GUIShapeFactory.getFor((GenericShape) media);
        } else if (media instanceof TextBox) {
            // Text Box
            if (media instanceof Hyperlink) {
                // Hyperlink Text Box
                return new GUIHyperlinkBox((Hyperlink) media);
            } else {
                // Plain Text Box
                return new GUITextBox((TextBox) media);
            }
        } else {
            throw new Exception("No appropriate GUIMedia class for `" + media + "`.");
        }
    }
}
