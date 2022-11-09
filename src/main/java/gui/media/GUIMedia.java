package gui.media;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.beans.property.*;

import app.media.Media;


/**
 * Base class for GUI elements which represent a Media object.
 */
public class GUIMedia<M extends Media> extends Pane {

    private M media;
    private DoubleProperty layoutWidth;
    private DoubleProperty layoutHeight;

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

        layoutWidth = new SimpleDoubleProperty();
        layoutHeight = new SimpleDoubleProperty();

        layoutBoundsProperty().addListener((o, oldVal, newVal) -> {
            layoutWidth.setValue(newVal.getWidth());
            layoutHeight.setValue(newVal.getHeight());
        });
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
            layoutWidth.unbindBidirectional(this.media.widthProperty());
            layoutHeight.unbindBidirectional(this.media.heightProperty());
            rotateProperty().unbindBidirectional(this.media.angleProperty());
            viewOrderProperty().unbindBidirectional(this.media.zIndexProperty());
        }

        this.media = media;

        translateXProperty().bindBidirectional(media.xProperty());
        translateYProperty().bindBidirectional(media.yProperty());
        layoutWidth.bindBidirectional(media.widthProperty());
        layoutHeight.bindBidirectional(media.heightProperty());
        rotateProperty().bindBidirectional(media.angleProperty());
        viewOrderProperty().bindBidirectional(media.zIndexProperty());
    }

    public void mediaUpdated(Media media) {}
}
