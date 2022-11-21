package gui.page;

import gui.Zoomable;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.transform.*;
import javafx.scene.input.*;
import javafx.geometry.*;

import java.util.*;

import app.media.Media;
import app.MediaCommunicator;
import app.MediaObserver;
import gui.media.GUIMedia;
import gui.media.GUIMediaFactory;
import gui.error_window.ErrorWindow;

// , gui.Zoomable
public class Page extends StackPane implements MediaObserver, Zoomable {

    // Additional padding added to the visible region
    private static double VISIBLE_BOUNDS_MARGIN = 100;

    private MediaCommunicator c;
    private PageEventHandler.HandlerMethod<?>[] handlerMethods;
    private PageEventHandler handler;
    private Pane mediaLayer;

    private Map<Long, GUIMedia> contents;

    private Bounds prevVisibleBounds;
    private Scale scale;
    private double scaleFactor = 1.0;

    public Page(MediaCommunicator c) {
        this.c = c;
        handlerMethods = new PageEventHandler.HandlerMethod<?>[0];
        getStyleClass().add("page");

        contents = new HashMap<>();
        mediaLayer = new Pane();
        mediaLayer.setManaged(false);
        getChildren().add(mediaLayer);

        prevVisibleBounds = new BoundingBox(0, 0, 0, 0);

        c.addObserver(this);

        mediaLayer.boundsInParentProperty().addListener(o -> reloadMedia());
        layoutBoundsProperty().addListener(o -> reloadMedia());

        scale = new Scale(scaleFactor, scaleFactor, getWidth()/2,
                getHeight()/2);
        mediaLayer.getTransforms().add(scale);

        setOnScroll(new Page.ScrollHandler());

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
     * Indicate that the given GUIMedia object has been updated.
     */
    public void updateMedia(GUIMedia media) {
        try {
            if (media.getID() == Media.EMPTY_ID) {
                media.getMedia().setID(c.getNewID());
            }

            contents.put(media.getID(), media);
            c.updateMedia(media.getMedia());
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


    public ArrayList<Media> getMedia(){
        ArrayList<Media> mediaOnPage = new ArrayList<>();
        for (GUIMedia<?> value : contents.values()){
            mediaOnPage.add(value.getMedia());
        }
        return mediaOnPage;
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
                if (!contents.get(id).getBoundsInParent().intersects(visibleBounds)) {
                    nodesToRemove.add(contents.get(id));
                    contents.remove(id);
                }
            }

            for (Node n: mediaLayer.getChildren()) {
                if (!n.getBoundsInParent().intersects(visibleBounds)) {
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

    public Pane getMediaLayer() {
        return mediaLayer;
    }

    //TODO zoom page or group

    public double getScaleFactor() {
        return scaleFactor;
    }

    /** Given a factor to scale the Page, scale in x and y directions by that factor. no pivot
     *
     * @param factor the factor by which to scale toZoom, > 0
     */
    @Override
    public void zoomToFactor(double factor) {
        scaleFactor = factor;

        scale.setPivotX(getWidth()/2);
        scale.setPivotY(getHeight()/2);
        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        reloadMedia();
    }

    /** Scale toZoom by jumping to the next smallest/largest (depending on the value of inOrOut) double in zoomOptions
     *
     * @param inOrOut "In" to zoom in, "Out" to zoom out
     */
    @Override
    public void zoomInOrOut(String inOrOut){
        double[] zoomOptions = {0.1, 0.25, 1.0/3.0, 0.5, 2.0/3.0, 0.75, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0,
                9.0, 10.0};
        double currentFactor = getScaleFactor();
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
            // if we zoom in we can then plug this factor right into our zoomToFactor function
        } else {
            i = zoomOptions.length - 1;
            while (i >= 0 && zoomOptions[i] >= currentFactor) {
                i--;
            }
            // once the loop ends, we have the index of a factor that is less than the current one
            // if we zoom out we can then plug this factor right into our zoomToFactor function
        }
        this.zoomToFactor(zoomOptions[i]);
    }

    public void jumpToPoint(double x, double y) {
        double translateX = x + getLayoutX();
        double translateY = y + getLayoutY();
        mediaLayer.relocate(translateX, translateY);
    }

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
            if (scrollEvent.getDeltaY() < 0) {
                // zooming out
                if (scaleFactor == 0.1) {
                    scrollEvent.consume();
                    return;
                }
                zoomInOrOut("Out");
            } else {
                // zooming in
                if (scaleFactor == 10.0) {
                    scrollEvent.consume();
                    return;
                }
                zoomInOrOut("In");
            }
        }
        private void scrollVerticallyHandle(ScrollEvent scrollEvent) {
            if (scrollEvent.getDeltaY() < 0) {
                // scroll down, translate up
                mediaLayer.setTranslateY(-50);
                mediaLayer.setLayoutY(mediaLayer.getLayoutY()-50);
            } else if (scrollEvent.getDeltaY() > 0) {
                // scroll up, translate down
                mediaLayer.setTranslateY(50);
                mediaLayer.setLayoutY(mediaLayer.getLayoutY()+50);
            }
        }



        private void scrollHorizontallyHandle(ScrollEvent scrollEvent) {
            if (scrollEvent.getDeltaX() < 0) {
                // scroll left, translate right
                mediaLayer.setTranslateX(-50);
                mediaLayer.setLayoutX(mediaLayer.getLayoutX() - 50);
            } else if (scrollEvent.getDeltaX() > 0) {
                // scroll left, translate right
                mediaLayer.setTranslateX(50);
                mediaLayer.setLayoutX(mediaLayer.getLayoutX() + 50);
            }
        }
    }
}