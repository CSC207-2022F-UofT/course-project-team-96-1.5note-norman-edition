package app;

import java.util.Set;

import app.media.Media;


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
     * @return The Media object with the given ID, or `null` if no such Media
     * object is stored.
     */
    Media loadMedia(long id) throws Exception;

    /**
     * Return the IDs of the Media objects within the rectangular region
     * with width `w`, height `h`, and top-left corner at (`x`, `y`).
     */
    Set<Long> getIDsWithin(double x, double y, double w, double h) throws Exception;

    boolean isIDtaken(long id) throws Exception;
}
