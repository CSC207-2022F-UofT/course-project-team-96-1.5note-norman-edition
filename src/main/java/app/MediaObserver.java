package app;

import app.media.Media;


/**
 * Implementors of this interface can be given to an instance of
 * MediaCommunicator to be notified when Media is added/updated/removed.
 */
public interface MediaObserver {

    /**
     * Called whenever a Media object is updated/added.
     *
     * @param media The Media object which was changed
     */
    default void mediaUpdated(Media media) {}

    /**
     * Called whenever a Media object is deleted.
     *
     * @param id The unique identifier of the Media which was removed
     */
    default void mediaDeleted(long id) {}
}
