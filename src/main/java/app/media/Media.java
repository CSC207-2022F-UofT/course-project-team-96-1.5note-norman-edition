package app.media;

import javafx.beans.*;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


/**
 * Core entity representing a piece of media which can be displayed on a page.
 */
public class Media<D extends Serializable> implements Serializable, Observable {

    // Identification
    private String name;
    private Set<String> tags;

    // Position
    private double x;
    private double y;
    // Size
    private double width;
    private double height;

    // Rotation
    private double angle;
    // "Vertical" Order
    private int zIndex;

    private D data;

    private transient List<InvalidationListener> listeners;


    public Media(
            String name, Set<String> tags, double x, double y,
            double width, double height, double angle, int zIndex, D data)
    {
        this.name = name;
        this.tags = tags;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.zIndex = zIndex;
        this.data = data;

        this.listeners = new ArrayList<>();
    }

    public Media(
        String name, double x, double y, double width, double height, D data)
    {
        this(name, new HashSet<>(), x, y, width, height, 0, 0, data);
    }


    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    // This method calls each InvalidationListener to indicate to them that
    // this Media instance has been changed. Every method which modifies any
    // member of the Media class must make a call to this method to notify
    // listeners of the change.
    private void invalidate() {
        for (InvalidationListener listener: listeners) {
            listener.invalidated(this);
        }
    }
}
