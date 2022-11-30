package gui.tool.app;

import java.util.Set;

import gui.tool.app.media.Media;


/**
 * Implementors of this interface can be used as a Media storage back-end by
 * MediaCommunicator.
 * <p>
 * The methods in this interface permit throwing exceptions to accomodate
 * implementations backed by something fallible, such as the filesystem.
 * The possible sources of error (if any) depend on the specific implementation.
 */
public interface MediaStorage {

    /**
     * Delete the Media with the given ID.
     */
    void deleteMedia(long id) throws Exception;

    /**
     * Save the given Media.
     * <p>
     * The Media must be assigned an ID other than `Media.EMPTY_ID`.
     */
    void saveMedia(Media media) throws Exception;

    /**
     * Load the Media with the given name.
     * @param id The unique identifier of the Media to load
     * @return The Media object with the given ID, or `null` if no such Media
     * object is stored.
     */
    Media loadMedia(long id) throws Exception;

    /**
     * @return The IDs of the Media objects within the rectangular region
     * with width `w`, height `h`, and top-left corner at (`x`, `y`).
     */
    Set<Long> getIDsWithin(double x, double y, double w, double h) throws Exception;


    /**
     * @return Whether or not Media with the given identifier is stored.
     */
    boolean isIDtaken(long id) throws Exception;
}
