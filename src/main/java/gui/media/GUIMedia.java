package gui.media;

import javafx.scene.layout.*;
import javafx.event.*;
import javafx.beans.property.*;
import javafx.beans.*;

import app.media.Media;


/**
 * Base class for GUI elements which represent a Media object.
 */
public class GUIMedia<M extends Media> extends Pane {

    private M media;
    private DoubleProperty width;
    private DoubleProperty height;

    private GUIMedia() {
        // Consume events from child nodes and re-emit them with their target
        // set to the GUIMedia object.
        setEventHandler(Event.ANY, e -> {
            if (e.getTarget() != this) {
                e.consume();
                Event copy = e.copyFor(null, this);
                fireEvent(copy);
            }
        });

        // Don't block mouse clicks/other input
        setPickOnBounds(false);
        // Don't use a managed layout, i.e. stop the page in which the GUIMedia
        // is placed from influencing the layout bounds of the GUIMedia.
        setManaged(false);

        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();

        // Only start listening for visible bounds changes when the GUIMedia
        // is actually being displayed in another node.
        parentProperty().addListener((o, oldVal, newVal) -> {
            boundsInParentProperty().removeListener(this::setDimensions);
            if (newVal != null) {
                boundsInParentProperty().addListener(this::setDimensions);
            }
        });
    }

    private void setDimensions(Observable o) {
        width.setValue(getBoundsInParent().getWidth());
        height.setValue(getBoundsInParent().getHeight());
    }

    public GUIMedia(M media) {
        this();
        setMedia(media);
    }

    public String getName() {
        return getMedia().getName();
    }

    public Long getID() {
        return getMedia().getID();
    }

    public M getMedia() {
        return media;
    }

    public void setMedia(M media) {
        if (this.media != null) {
            translateXProperty().unbindBidirectional(this.media.xProperty());
            translateYProperty().unbindBidirectional(this.media.yProperty());
            width.unbindBidirectional(this.media.widthProperty());
            height.unbindBidirectional(this.media.heightProperty());
            rotateProperty().unbindBidirectional(this.media.angleProperty());
            viewOrderProperty().unbindBidirectional(this.media.zIndexProperty());
        }

        this.media = media;

        translateXProperty().bindBidirectional(media.xProperty());
        translateYProperty().bindBidirectional(media.yProperty());
        width.bindBidirectional(media.widthProperty());
        height.bindBidirectional(media.heightProperty());
        rotateProperty().bindBidirectional(media.angleProperty());
        viewOrderProperty().bindBidirectional(media.zIndexProperty());
    }

    /**
     * This method is called when the underlying Media object is updated.
     * <p>
     * After this method finishes the GUIMedia object on which it was called
     * should properly represent the Media object which was passed in.
     */
    public void mediaUpdated(Media media) {

    }
}
