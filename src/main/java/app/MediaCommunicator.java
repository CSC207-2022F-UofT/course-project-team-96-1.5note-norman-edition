package app;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;

import app.media.Media;


public class MediaCommunicator {

    private Set<MediaObserver> observers;
    private MediaStorage storage;

    public MediaCommunicator(MediaStorage storage) {
        this.observers = new HashSet<>();
        this.storage = storage;
    }

    public void addObserver(MediaObserver o) {
        observers.add(o);
    }

    public void removeObserver(MediaObserver o) {
        observers.remove(o);
    }


    /**
     * Update (or add, if it doesn't already exist) the given Media object.
     */
    public void updateMedia(Media media) {
        for (MediaObserver o: observers) {
            o.mediaUpdated(media);
        }
    }

    /**
     * Remove the Media object with the given name.
     */
    public void removeMedia(String name) {
        for (MediaObserver o: observers) {
            o.mediaRemoved(name);
        }
    }

    /**
     * Return a set containing <i>at least</i> the names of all the Media
     * objects within the rectangular region with top left corner at (`x`, `y`)
     * and with width `w` and height `h`.
     */
    public Set<String> getMediaNamesWithin(int x, int y, int w, int h) {
        return null;
    }

    /**
     * Return the Media object with the given name
     */
    public Media getMediaWithName(String name) {
        return null;
    }
}
