package app;

import app.media.Media;


/**
 * Implementors of this interface can be given to an instance of
 * MediaCommunicator to be notified when Media is added/updated/removed.
 */
public interface MediaObserver {

    /**
     * Called whenever a Media object is updated/added.
     */
    default void mediaUpdated(Media media) {}

    /**
     * Called whenever a Media object is removed.
     */
    default void mediaRemoved(String name) {}
}
