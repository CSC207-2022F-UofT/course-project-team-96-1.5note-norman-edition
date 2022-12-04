package app;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import app.media.Media;


/**
 * Main facade class.
 * <p>
 * Users of this class can submit the creation/modification/deletion of Media
 * entities as well as request that changes be saved to whatever MediaStorage
 * implementation is used.
 * <p>
 * The MediaCommunicator provides a central API for modifying Media and allows
 * listening for changes made using its API via the MediaObserver interface.
 */
public class MediaCommunicator {

    private Set<MediaObserver> observers;
    private MediaStorage storage;

    // Changes to media (updates/removals) which have not yet been committed to
    // storage via the `save` method. We need to keep track of these to make
    // sure they don't get lost if a Page, etc. "forgets" about them without
    // the user first saving them.
    private HashMap<Long, Media> unsavedUpdates;
    private Set<Long> unsavedRemovals;

    private Random rng;

    /**
     * Instantiate a MediaCommunicator with the given storage backend.
     */
    public MediaCommunicator(MediaStorage storage) {
        this.storage = storage;
        observers = new HashSet<>();
        unsavedUpdates = new HashMap<>();
        unsavedRemovals = new HashSet<>();
        rng = new Random();
    }

    /**
     * Add a MediaObserver to this MediaCommunicator. The added MediaObserver
     * will be notified whenever Media is added/modified/removed via this
     * MediaCommunicator.
     */
    public void addObserver(MediaObserver o) {
        observers.add(o);
    }

    /**
     * Remove a MediaObserver from this MediaCommunicator. The removed
     * MediaObserver will no longer be notified of any changes to Media made
     * via this MediaCommunicator.
     */
    public void removeObserver(MediaObserver o) {
        observers.remove(o);
    }

    /**
     * Return an ID which is unused at the time this method is called.
     * <p>
     * The ID is not guarateed to <i>remain</i> unique unless it is used by a
     * Media object which gets passed to the `updateMedia` method.
     */
    private long getNewID() throws Exception {
        long id = Media.EMPTY_ID;

        while (
                id == Media.EMPTY_ID
                || unsavedUpdates.containsKey(id) || storage.isIDtaken(id))
        {
            id = rng.nextLong();
        }

        return id;
    }

    /**
     * Update (or add, if it doesn't already exist) the given Media object.
     * <p>
     * If the given Media does not yet have an assigned ID, an ID will be
     * generated and the method passed as `idCallback` will be called with the
     * generated ID as its argument <i>before</i> any MediaObservers are
     * notified of the new Media.
     */
    public void updateMedia(Media media, Consumer<Long> idCallback) throws Exception {
        if (media.getID() == Media.EMPTY_ID) {
            media.setID(getNewID());
        }

        unsavedRemovals.remove(media.getID());
        unsavedUpdates.put(media.getID(), media);

        if (idCallback != null) {
            idCallback.accept(media.getID());
        }

        for (MediaObserver o: observers) {
            o.mediaUpdated(media);
        }
    }

    /**
     * Update (or add, if it doesn't already exist) the given Media object.
     */
    public void updateMedia(Media media) throws Exception {
        updateMedia(media, null);
    }

    /**
     * Remove the Media object with the given name.
     */
    public void deleteMedia(long id) {
        unsavedRemovals.add(id);
        unsavedUpdates.remove(id);

        for (MediaObserver o: observers) {
            o.mediaDeleted(id);
        }
    }

    /**
     * Return a set containing <i>at least</i> the IDs of all the Media
     * objects within the rectangular region with top left corner at (`x`, `y`)
     * and with width `w` and height `h`.
     */
    public Set<Long> getIDsWithin(
            double x, double y, double w, double h) throws Exception
    {
        Set<Long> storedIDs = storage.getIDsWithin(x, y, w, h);

        for (Media media: unsavedUpdates.values()) {
            if (media.isWithin(x, y, w, h)) {
                storedIDs.add(media.getID());
            } else {
                storedIDs.remove(media.getID());
            }
        }

        return storedIDs;
    }

    /**
     * Return the Media object with the given ID.
     */
    public Media getMedia(long id) throws Exception {
        // First, try getting the loaded but unsaved Media object, then fall
        // back to trying to load from storage if the Media is not loaded.
        return Optional.ofNullable(unsavedUpdates.get(id))
            .orElse(storage.loadMedia(id));
    }

    /**
     * Save changes to storage.
     */
    public void save() throws Exception {
        for (long id: unsavedRemovals) {
            storage.deleteMedia(id);
        }
        unsavedRemovals.clear();

        for (Media media: unsavedUpdates.values()) {
            storage.saveMedia(media);
        }
        unsavedUpdates.clear();
    }
}
