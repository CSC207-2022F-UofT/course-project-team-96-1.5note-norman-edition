package gui.page;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.transform.*;
import javafx.scene.input.*;
import javafx.beans.value.*;
import javafx.geometry.Point2D;

import app.MediaCommunicator;
import app.MediaObserver;
import gui.media.GUIMedia;


public class Page extends StackPane implements MediaObserver {

    private MediaCommunicator c;
    private PageEventHandler.HandlerMethod<?>[] handlerMethods;
    private PageEventHandler handler;
    private Pane mediaLayer;

    public Page(MediaCommunicator c) {
        this.c = c;
        this.handlerMethods = new PageEventHandler.HandlerMethod<?>[0];
        getStyleClass().add("page");

        mediaLayer = new Pane();
        getChildren().add(mediaLayer);

        c.addObserver(this);
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
     * Remove the given GUIMedia object from this page.
     */
    public void removeMedia(GUIMedia media) {
        mediaLayer.getChildren().remove(media);
    }

    /**
     * Transform the given coordinates into the page's coordinate space.
     */
    public Point2D getCoords(Point2D coords) {
        try {
            return mediaLayer.getLocalToParentTransform().inverseTransform(coords);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
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
     * Return a rectangle which contains everything that is currently visible on the page.
     */
    public Rectangle2D getVisibleRegion() {
        return null;
    }
}
