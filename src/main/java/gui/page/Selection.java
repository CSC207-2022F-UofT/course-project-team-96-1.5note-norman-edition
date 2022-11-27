package gui.page;

import java.util.Arrays;
import java.util.HashSet;

import javafx.collections.*;
import javafx.beans.property.*;
import javafx.scene.effect.*;
import javafx.geometry.Point2D;

import app.media.Media;
import gui.media.GUIMedia;


/**
 * Perform bulk operations on GUIMedia within the same Page.
 * <p>
 * A Selection is created for a Page. Then, GUIMedia from that Page can be
 * added to the Selection.
 * <p>
 * The GUIMedia within a Selection can then be moved, renamed, rotated, etc.
 * all together.
 */
public class Selection {

    private Page page;
    private Effect selectedEffect;
    private ReadOnlySetWrapper<GUIMedia> selection;

    /**
     * Create a new Selection for the given Page.
     * <p>
     * The given selectedEffect will be added to GUIMedia in the selection to
     * visually indicate their membership.
     */
    public Selection(Page page, Effect selectedEffect) {
        this.page = page;
        this.selectedEffect = selectedEffect;
        selection = new ReadOnlySetWrapper<>(
                FXCollections.observableSet(new HashSet<>()));
    }

    private void removeSelectedEffect(GUIMedia media) {
        media.setEffect(null);
    }

    private void addSelectedEffect(GUIMedia media) {
        media.setEffect(selectedEffect);
    }

    /**
     * Add the given GUIMedia to the selection.
     */
    public void addMedia(GUIMedia... media) {
        selection.addAll(Arrays.asList(media));

        for (GUIMedia m: media) {
            addSelectedEffect(m);
            m.use();
        }
    }

    /**
     * Remove the given GUIMedia from the selection.
     */
    public void removeMedia(GUIMedia... media) {
        selection.removeAll(Arrays.asList(media));

        for (GUIMedia m: media) {
            removeSelectedEffect(m);
            m.release();
        }
    }

    /**
     * Empty the selection.
     */
    public void removeAllMedia() {
        removeMedia(selection.toArray(new GUIMedia[0]));
    }

    public ObservableSet<GUIMedia> getMedia() {
        return selection.getReadOnlyProperty();
    }

    /**
     * Return whether or not the given GUIMedia is selected.
     */
    public boolean contains(GUIMedia media) {
        return selection.contains(media);
    }


    /**
     * Increment the positions of the selected Media by the given displacement.
     */
    public void move(Point2D displacement) {
        for (GUIMedia selected: selection) {
            Media media = selected.getMedia();
            media.setX(media.getX() + displacement.getX());
            media.setY(media.getY() + displacement.getY());

            page.updateMedia(selected);
        }
    }

    /**
     * Delete the selected Media from the page.
     */
    public void delete() {
        for (GUIMedia media: selection) {
            page.getCommunicator().deleteMedia(media.getID());
        }

        removeAllMedia();
    }

    /**
     * Rename the selected Media to the given name.
     */
    public void rename(String name) {
        for (GUIMedia media: selection) {
            media.getMedia().setName(name);
            page.updateMedia(media);
        }
    }

    /**
     * Set the angle of rotation of the selected Media to the given angle.
     */
    public void rotate(double angle) {
        for (GUIMedia media: selection) {
            media.getMedia().setAngle(angle);
            page.updateMedia(media);
        }
    }

    /**
     * Set the Z-index of the selected Media to the given Z-index.
     */
    public void setZindex(int zIndex) {
        for (GUIMedia media: selection) {
            media.getMedia().setZindex(zIndex);
            page.updateMedia(media);
        }
    }


    /**
     * Return a name for the selection.
     * <p>
     * If every selected Media has the same name, that name is returned.
     * If the selection is empty, or the selected Media do not share the same
     * name, the empty string is returned.
     */
    public String getName() {
        String name = null;

        for (GUIMedia media: selection) {
            if (name == null || media.getName().equals(name)) {
                name = media.getName();
            } else {
                name = "";
            }
        }

        if (name == null) {
            return "";
        } else {
            return name;
        }
    }

    /**
     * Return an angle for the selection.
     * <p>
     * The mean average of the angles of rotation of the selected Media is
     * returned.
     */
    public double getAngle() {
        double angle = 0;

        for (GUIMedia selected: selection) {
            Media media = selected.getMedia();
            angle += media.getAngle() / selection.size();
        }

        return angle;
    }

    /**
     * Return a Z-Index for the selection.
     * <p>
     * The maximum Z-Index of the selected Media is returned.
     */
    public int getZindex() {
        int zIndex = 0;

        for (GUIMedia selected: selection) {
            Media media = selected.getMedia();

            if (media.getZindex() > zIndex) {
                zIndex = media.getZindex();
            }
        }

        return zIndex;
    }
}
