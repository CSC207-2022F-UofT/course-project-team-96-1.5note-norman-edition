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
    private final DoubleProperty width;
    private final DoubleProperty height;

    private int numUsers;

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

        numUsers = 0;
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

    /**
     * Indicate that the caller is using the GUIMedia instance and it should
     * not be un-loaded.
     * <p>
     * The `release` method MUST be called when the GUIMedia is no longer
     * needed. Each call to `use` MUST have a corresponding call to `release`.
     */
    public void use() {
        numUsers++;
    }

    /**
     * Indicate that the caller is no longer using the GUIMedia instance and
     * it can be un-loaded.
     */
    public void release() {
        numUsers--;
        if (numUsers < 0) {
            throw new RuntimeException(
                    new IllegalStateException(
                        "GUIMedia released more times than used."));
        }
    }

    /**
     * Return whether or not there are any users of this GUIMedia.
     * If this method returns `false`, the Media should not be automatically
     * removed from the page.
     */
    public boolean isInUse() {
        return numUsers > 0;
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
     *
     * @throws ClassCastException if the Media object is not of the appropriate
     * type for this GUIMedia.
     */
    public void mediaUpdated(Media media) throws ClassCastException {}

    /**
     * This method is called when this GUIMedia object is "removed" and will
     * no longer be displayed.
     * <p>
     * Implementations of this method can specify the procedure to "clean up"
     * any resources used by an instance of GUIMedia when it is no longer
     * visible.
     */
    public void removed() {}
}
