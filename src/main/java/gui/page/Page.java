package gui.page;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.transform.*;
import javafx.scene.input.*;
import javafx.beans.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.event.EventHandler;

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
import gui.Zoomable;


/**
 * GUI element which displays the visual representation of the Media entities
 * in a page.
 */
public class Page extends StackPane implements MediaObserver, Zoomable {

    // Additional padding added to the visible region
    private static double LOADABLE_BOUNDS_MARGIN = 1000;

    private MediaCommunicator c;
    private PageEventHandler.HandlerMethod<?>[] handlerMethods;
    private PageEventHandler handler;
    private Pane mediaLayer;
    private Pane uiLayer;

    private Map<Long, GUIMedia> contents;

    private Bounds prevLoadableBounds;

    private final Scale scale;
    private double scaleFactor = 1.0;

    public Page(MediaCommunicator c) {
        this.c = c;
        handlerMethods = new PageEventHandler.HandlerMethod<?>[0];
        getStyleClass().add("page");

        contents = new HashMap<>();
        mediaLayer = new Pane();
        // mediaLayer.setManaged(false);

        uiLayer = new Pane();
        uiLayer.setPickOnBounds(false);

        getChildren().addAll(mediaLayer, uiLayer);

        prevLoadableBounds = new BoundingBox(0, 0, 0, 0);

        c.addObserver(this);

        scale = new Scale(scaleFactor, scaleFactor);
        mediaLayer.getTransforms().add(scale);

        setOnScroll(new Page.ScrollHandler());

        // Reload media when view changes
        Observable[] reloadMediaOnChangesTo = new Observable[] {
            layoutBoundsProperty(),
            mediaLayer.boundsInParentProperty(),
            mediaLayer.translateXProperty(),
            mediaLayer.translateYProperty()
        };

        for (Observable obs: reloadMediaOnChangesTo) {
            obs.addListener(o -> reloadMedia());
        }

        scale.setOnTransformChanged(e -> reloadMedia());
    }

    // Do necessary loading/unloading of Media for the currently visible region.
    // If an error occurs, catch it and report it to the user with ErrorWindow.
    private void reloadMedia() {
        try {
            reloadMediaForLoadableBounds();
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
        // Don't use a managed layout, i.e. stop the page in which the GUIMedia
        // is placed from influencing the layout bounds of the GUIMedia.
        media.setManaged(false);

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
            c.updateMedia(media.getMedia(), id -> contents.put(id, media));
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
     * Returns the currently visible region.
     */
    private Bounds getVisibleBounds() {
        return mediaLayer.parentToLocal(getLayoutBounds());
    }

    /**
     * Return a regions containing everything that should currently be loaded
     * on the page.
     */
    private Bounds getLoadableBounds() {
        Bounds b = getVisibleBounds();
        // Add the LOADABLE_BOUNDS_MARGIN to each dimension to make sure we only
        // have to load in more media objects for large movements instead of for
        // every movement.
        return new BoundingBox(
                b.getMinX() - LOADABLE_BOUNDS_MARGIN,
                b.getMinY() - LOADABLE_BOUNDS_MARGIN,
                b.getWidth() + 2 * LOADABLE_BOUNDS_MARGIN,
                b.getHeight() + 2 * LOADABLE_BOUNDS_MARGIN);
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

    private void reloadMediaForLoadableBounds() throws Exception {
        Bounds loadableBounds = getLoadableBounds();

        // If the visible region moved far enough that we need to load/unload media
        if (boundsDistance(loadableBounds, prevLoadableBounds) >= LOADABLE_BOUNDS_MARGIN) {
            prevLoadableBounds = loadableBounds;

            // Load any newly visible nodes
            Set<Long> visibleIDs = c.getIDsWithin(
                    loadableBounds.getMinX(), loadableBounds.getMinY(),
                    loadableBounds.getWidth(), loadableBounds.getHeight());

            for (long id: visibleIDs) {
                if (!contents.containsKey(id)) {
                    // Only add Media which is not already on the page.
                    Media media = c.getMedia(id);
                    addGUIMediaFor(media);
                }
            }

            // Remove nodes which are no longer visible
            Set<Long> initialIDs = new HashSet<>(contents.keySet());
            for (long id: initialIDs) {
                GUIMedia media = contents.get(id);

                if (
                        media != null
                        && !media.isInUse()
                        && !media.getBoundsInParent().intersects(loadableBounds))
                {
                    removeMedia(media);
                }
            }
        }
    }

    // Return whether or not the media falls within the current loadable bounds
    private boolean isMediaLoadable(Media media) {
        return media.isWithin(
                prevLoadableBounds.getMinX(), prevLoadableBounds.getMinY(),
                prevLoadableBounds.getWidth(), prevLoadableBounds.getHeight());
    }

    // Instantiate a GUIMedia object for the given Media entity and add it
    // to the page.
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


    /*
     * Implementation of app.MediaObserver interface
     */

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
        } else if (isMediaLoadable(media)) {
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

    /*
     * Implementation of Zoomable interface
     */

    /** Given a factor to scale the Page, scale in x and y directions by that factor. no pivot
     *
     * @param factor the factor by which to scale toZoom, > 0
     */
    @Override
    public void zoomToFactor(double factor) {
        // The scale transform stays rooted at the origin. Therefore, to keep
        // the view centered on the same point, our approach is as follows:
        //
        // * Get the center of the view (in the mediaLayer's coordinate space)
        // * Scale the mediaLayer
        // * Get the center of the view again and compute the displacement
        // * Translate the mediaLayer to un-do the displacement caused by the
        //   scale.
        //
        // This guarantees that the center of the page stays fixed during
        // zooming.

        Bounds b = getVisibleBounds();
        Point2D center = new Point2D(b.getCenterX(), b.getCenterY());

        scaleFactor = factor;
        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        b = getVisibleBounds();
        Point2D scaledCenter = new Point2D(b.getCenterX(), b.getCenterY());
        Point2D diff = scaledCenter.subtract(center);

        mediaLayer.setTranslateX(mediaLayer.getTranslateX() + diff.getX() * factor);
        mediaLayer.setTranslateY(mediaLayer.getTranslateY() + diff.getY() * factor);
    }

    /** Scale toZoom by jumping to the next smallest/largest (depending on the value of inOrOut) double in zoomOptions
     *
     * @param inOrOut "In" to zoom in, "Out" to zoom out
     */
    @Override
    public void zoomInOrOut(String inOrOut){
        double[] zoomOptions = {0.1, 0.25, 1.0/3.0, 0.5, 2.0/3.0, 0.75, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0,
                9.0, 10.0};
        double currentFactor = scaleFactor;
        if (currentFactor == 0.1 && inOrOut.equals("Out")) {
            return;
        } else if (currentFactor == 10.0 && inOrOut.equals("In")) {
            return;
        }
        int i;
        if (inOrOut.equals("In")) {
            // once the loop ends, we have the index of a factor that is greater than the current one
            i = 0;
            while (i < zoomOptions.length && zoomOptions[i] <= currentFactor) {
                i++;
            }
        } else {
            i = zoomOptions.length - 1;
            while (i >= 0 && zoomOptions[i] >= currentFactor) {
                i--;
            }
            // once the loop ends, we have the index of a factor that is less than the current one
        }
        this.zoomToFactor(zoomOptions[i]);
    }

    /** given the x and y coords of a point, make that point the center of the visible box
     *
     * @param x x coordinate of point you want to jump to
     * @param y y coordinate of point you want to jump to
     */
    public void jumpToCenter(double x, double y) {
        double currentZoom = scaleFactor;
        this.zoomToFactor(1.0);
        this.jumpToTopLeft(x, y);
        this.zoomToFactor(currentZoom);
    }

    /** given the x and y coords of a point, make that point the top left of the visible box
     *
     * @param x x coordinate of point you want to jump to
     * @param y y coordinate of point you want to jump to
     */
    public void jumpToTopLeft(double x, double y) {
        double translateX = x - getTranslateX();
        double translateY = y - getTranslateY();
        mediaLayer.setTranslateX(translateX);
        mediaLayer.setTranslateY(translateY);
    }

    /** handles scrolling inputs, zooming when control is pressed, horizontal scrolling when shift is pressed, and
     * vertical scrolling otherwise
     *
     */
    private class ScrollHandler implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.isControlDown()) {
                // zooming
                zoomHandle(scrollEvent);
                scrollEvent.consume();
            } else if (scrollEvent.isShiftDown()) {
                // scrolling horizontally
                scrollHorizontallyHandle(scrollEvent);
                scrollEvent.consume();
            } else {
                // scrolling vertically
                scrollVerticallyHandle(scrollEvent);
                scrollEvent.consume();
            }
        }
        private void zoomHandle(ScrollEvent scrollEvent) {
            double delta = scrollEvent.getDeltaY();

            if (delta < 0) {
                // zooming out
                if (scaleFactor == 0.1) {
                    scrollEvent.consume();
                    return;
                }
                zoomInOrOut("Out");
            } else if (delta > 0) {
                // zooming in
                if (scaleFactor == 10.0) {
                    scrollEvent.consume();
                    return;
                }
                zoomInOrOut("In");
            }
        }
        private void scrollVerticallyHandle(ScrollEvent scrollEvent) {
            double currentTranslation = mediaLayer.getTranslateY();
            mediaLayer.setTranslateY(currentTranslation + scrollEvent.getDeltaY());
        }

        private void scrollHorizontallyHandle(ScrollEvent scrollEvent) {
            double currentTranslation = mediaLayer.getTranslateX();
            mediaLayer.setTranslateX(currentTranslation + scrollEvent.getDeltaX());
        }
    }
}
