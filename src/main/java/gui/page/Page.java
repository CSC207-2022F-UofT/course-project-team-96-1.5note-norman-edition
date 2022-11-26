package gui.page;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.transform.*;
import javafx.scene.input.*;
import javafx.beans.value.*;
import javafx.geometry.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import app.media.Media;
import app.MediaCommunicator;
import app.MediaObserver;
import gui.media.GUIMedia;
import gui.media.GUIMediaFactory;
import gui.error_window.ErrorWindow;


public class Page extends StackPane implements MediaObserver {

    // Additional padding added to the visible region
    private static double VISIBLE_BOUNDS_MARGIN = 100;

    private MediaCommunicator c;
    private PageEventHandler.HandlerMethod<?>[] handlerMethods;
    private PageEventHandler handler;
    private Pane mediaLayer;
    private Pane uiLayer;

    private Map<Long, GUIMedia> contents;

    private Bounds prevVisibleBounds;

    public Page(MediaCommunicator c) {
        this.c = c;
        handlerMethods = new PageEventHandler.HandlerMethod<?>[0];
        getStyleClass().add("page");

        contents = new HashMap<>();
        mediaLayer = new Pane();
        mediaLayer.setManaged(false);

        uiLayer = new Pane();
        uiLayer.setPickOnBounds(false);

        getChildren().addAll(mediaLayer, uiLayer);

        prevVisibleBounds = new BoundingBox(0, 0, 0, 0);

        c.addObserver(this);

        mediaLayer.boundsInParentProperty().addListener(o -> reloadMedia());
        layoutBoundsProperty().addListener(o -> reloadMedia());
    }

    // Do necessary loading/unloading of Media for the currently visible region.
    // If an error occurs, catch it and report it to the user with ErrorWindow.
    private void reloadMedia() {
        try {
            reloadMediaForVisibleBounds();
        } catch (Exception e) {
            new ErrorWindow(this, null, "Failed to load media", e).show();
        }
    }

    /**
     * Return the MediaCommunicator used by this app.media.Page.
     */
    public MediaCommunicator getCommunicator() {
        return c;
    }

    /**
     * Set the handler of input events.
     * <p>
     * The previous handler will be removed.
     */
    public void setEventHandler(PageEventHandler h) {
        // Remove previous handler's handler methods
        for (PageEventHandler.HandlerMethod<?> handlerMethod: handlerMethods) {
            handlerMethod.removeFromPage(this);
        }

        // Notify the previous handler that it will no longer receive events
        // from this page.
        if (handler != null) {
            handler.disabledFor(this);
        }
        handler = h;

        if (handler != null) {
            handlerMethods = handler.getHandlerMethods();
            // Notify the new handler that it will now be receiving events from
            // this page.
            handler.enabledFor(this);
        } else {
            handlerMethods = new PageEventHandler.HandlerMethod<?>[0];
        }

        // Add new handler's handler methods
        for (PageEventHandler.HandlerMethod<?> handlerMethod: handlerMethods) {
            handlerMethod.addToPage(this);
        }
    }

    /**
     * Add the given GUIMedia object to this page.
     */
    public void addMedia(GUIMedia media) {
        mediaLayer.getChildren().add(media);
    }

    /**
     * Return whether or not the given GUIMedia object is within this page
     */
    public boolean contains(GUIMedia media) {
        return contents.containsKey(media.getID());
    }

    /**
     * Indicate that the given GUIMedia object has been updated.
     */
    public void updateMedia(GUIMedia media) {
        try {
<<<<<<< HEAD
            c.updateMedia(media.getMedia(), id -> contents.put(id, media));
=======
            if (media.getID() == Media.EMPTY_ID) {
                media.getMedia().setID(c.getNewID());
                contents.put(media.getID(), media);
            }

            if (contains(media)) {
                c.updateMedia(media.getMedia());
            }
>>>>>>> media-tool
        } catch (Exception e) {
            new ErrorWindow(this, null, "Updating Media object failed.", e)
                .show();
        }
    }

    /**
     * Remove the given GUIMedia object from this page.
     */
    public void removeMedia(GUIMedia media) {
        contents.remove(media.getID());
        mediaLayer.getChildren().remove(media);
        media.removed();
    }

    /**
     * Remove ALL GUIMedia objects from this page.
     */
    public void removeAllMedia() {
        Set<GUIMedia> mediaToRemove = new HashSet<>(contents.values());

        for (GUIMedia media: mediaToRemove) {
            removeMedia(media);
        }
    }

    /**
     * Get all the media currently displayed on this page which have assigned IDs.
     */
    public Set<GUIMedia> getAllMedia() {
        return new HashSet<>(contents.values());
    }

    /**
     * Transform the given coordinates into the page's coordinate space.
     */
    public Point2D getCoords(Point2D coords) {
        return mediaLayer.parentToLocal(coords);
    }

    public Point2D getCoords(double x, double y) {
        return getCoords(new Point2D(x, y));
    }

    /**
     * Return the coordinates of the given MouseEvent in the page's coordinate space.
     */
    public Point2D getMouseCoords(MouseEvent e) {
        return getCoords(e.getX(), e.getY());
    }

    /**
     * Transform the given out of the page's coordinate space.
     */
    public Point2D getCoordsInv(Point2D coords) {
        return mediaLayer.localToParent(coords);
    }

    public Point2D getCoordsInv(double x, double y) {
        return getCoordsInv(new Point2D(x, y));
    }

    /**
     * Return a regions containing everything that is currently visible on the page.
     */
    private Bounds getVisibleBounds() {
        Bounds b = mediaLayer.parentToLocal(getLayoutBounds());
        // Add the VISIBLE_BOUNDS_MARGIN to each dimension to make sure we only
        // have to load in more media objects for large movements instead of for
        // every movement.
        return new BoundingBox(
                b.getMinX() - VISIBLE_BOUNDS_MARGIN,
                b.getMinY() - VISIBLE_BOUNDS_MARGIN,
                b.getWidth() + VISIBLE_BOUNDS_MARGIN,
                b.getHeight() + VISIBLE_BOUNDS_MARGIN);
    }

    /*
     * Return the maximum of all the distances between the vertices of two
     * bounding rectangles.
     *
     * This method is used to decide whether or not the viewable region has
     * moved far enough that loading in new Media objects may be required.
     */
    private static double boundsDistance(Bounds b1, Bounds b2) {
        Double[] distances = {
            // top left
            new Point2D(b1.getMinX(), b1.getMinY()).distance(b2.getMinX(), b2.getMinY()),
            // top right
            new Point2D(b1.getMaxX(), b1.getMinY()).distance(b2.getMaxX(), b2.getMinY()),
            // bottom left
            new Point2D(b1.getMinX(), b1.getMaxY()).distance(b2.getMinX(), b2.getMaxY()),
            // bottom right
            new Point2D(b1.getMaxX(), b1.getMaxY()).distance(b2.getMaxX(), b2.getMaxY())
        };

        return Collections.max(Arrays.asList(distances));
    }

    private void reloadMediaForVisibleBounds() throws Exception {
        Bounds visibleBounds = getVisibleBounds();

        // If the visible region moved far enough that we need to load/unload media
        if (boundsDistance(visibleBounds, prevVisibleBounds) >= VISIBLE_BOUNDS_MARGIN) {
            prevVisibleBounds = visibleBounds;

            // First, remove nodes which are no longer visible
            Set<Node> nodesToRemove = new HashSet<>();
            Set<Long> initialIDs = new HashSet<>(contents.keySet());

            for (long id: initialIDs) {
                GUIMedia media = contents.get(id);

                if (!media.getBoundsInParent().intersects(visibleBounds)) {
                    nodesToRemove.add(contents.get(id));
                    contents.remove(id);
                }
            }

            for (Node n: mediaLayer.getChildren()) {
                if (
                    n instanceof GUIMedia &&
                    !n.getBoundsInParent().intersects(visibleBounds))
                {
                    nodesToRemove.add(n);
                }
            }

            mediaLayer.getChildren().removeAll(nodesToRemove);

            // Then, load any newly visible nodes
            Set<Long> visibleIDs = c.getIDsWithin(
                    visibleBounds.getMinX(), visibleBounds.getMinY(),
                    visibleBounds.getWidth(), visibleBounds.getHeight());

            for (long id: visibleIDs) {
                if (!contents.containsKey(id)) {
                    // Only add Media which is not already on the page.
                    Media media = c.getMedia(id);
                    addGUIMediaFor(media);
                }
            }
        }
    }

    // Return whether or not the media falls within the current visible bounds
    private boolean isMediaVisible(Media media) {
        return media.isWithin(
                prevVisibleBounds.getMinX(), prevVisibleBounds.getMinY(),
                prevVisibleBounds.getWidth(), prevVisibleBounds.getHeight());
    }

    private void addGUIMediaFor(Media media) throws Exception {
        GUIMedia guiMedia = GUIMediaFactory.getFor(media);
        contents.put(guiMedia.getID(), guiMedia);
        addMedia(guiMedia);
    }

    /**
     * Set a node to display <i>on top</i> of the current page.
     * <p>
     * This can be used to display a GUI control "above" the current page
     * contents, such as a popup or context menu.
     * <p>
     * Only one node can be set as the UI layer at a time. Calling this method
     * will replace the previous contents of the UI layer (if any).
     * <p>
     * The UI layer can be cleared by passing `null` as the argument to this
     * method.
     */
    public void setUIlayer(Node node) {
        uiLayer.getChildren().clear();
        if (node != null) {
            uiLayer.getChildren().add(node);
        }
    }

    @Override
    public void mediaDeleted(long id) {
        if (contents.containsKey(id)) {
            removeMedia(contents.get(id));
        }
    }

    @Override
    public void mediaUpdated(Media media) {
        long id = media.getID();

        if (contents.containsKey(id)) {
            // If GUIMedia with the given ID is already on the page, give that
            // GUIMedia the updated Media object.
            contents.get(id).mediaUpdated(media);
        } else if (isMediaVisible(media)) {
            // Otherwise, if the Media object lies within the currently visible
            // region of the page, add a new GUIMedia object for it.
            try {
                addGUIMediaFor(media);
            } catch (Exception e) {
                new ErrorWindow(
                        this, null, "Couldn't add GUIMedia for `" + media + "`.", e)
                    .show();
            }
        }
    }
}
