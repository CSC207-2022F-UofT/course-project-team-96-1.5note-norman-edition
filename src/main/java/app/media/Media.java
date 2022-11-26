package app.media;

import javafx.beans.property.*;
import javafx.collections.*;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;


/**
 * Core entity representing a piece of media which can be displayed on a page.
 */
public class Media implements Serializable {

    public static final Long EMPTY_ID = 0l;

    long id;
    StringProperty name;
    ObservableSet<String> tags;
    DoubleProperty x;
    DoubleProperty y;
    DoubleProperty width;
    DoubleProperty height;
    DoubleProperty angle;
    IntegerProperty zIndex;

    // JavaFX property objects are not serializable, so a record class is used
    // for the purpose of actually serializing and de-seralizing the data which
    // is then loaded into an instance of Media with the `setFields` method.
    private static record Data(
            long id,
            String name, Set<String> tags,
            double x, double y,
            double width, double height,
            double angle, int zIndex) implements Serializable {}

    private void writeObject(ObjectOutputStream out) throws IOException {
        Data d = new Data(
                id, getName(), new HashSet<>(tags), getX(), getY(),
                getWidth(), getHeight(), getAngle(), getZindex());

        out.writeObject(d);
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        Data d = (Data) in.readObject();
        initializeProperties(
                d.id(), d.name(), d.tags(), d.x(), d.y(),
                d.width(), d.height(), d.angle(), d.zIndex());
    }

    private void readObjectNoData() throws ObjectStreamException {}

    // the above `writeObject`, `readObject`, and `readObjectNoData` methods
    // are all required in order to re-define the serialization/deserialization
    // procedure for this class.
    //
    // https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/Serializable.html


    public Media(
            long id, String name, Set<String> tags, double x, double y,
            double width, double height, double angle, int zIndex)
    {
        initializeProperties(id, name, tags, x, y, width, height, angle, zIndex);
    }

    public Media(
            String name, Set<String> tags, double x, double y,
            double width, double height, double angle, int zIndex)
    {
        this(EMPTY_ID, name, tags, x, y, width, height, angle, zIndex);
    }


    private void initializeProperties (
            long id, String name, Set<String> tags, double x, double y,
            double width, double height, double angle, int zIndex)
    {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.tags = FXCollections.observableSet(new HashSet<>());
        this.tags.addAll(tags);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.width = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);
        this.angle = new SimpleDoubleProperty(angle);
        this.zIndex = new SimpleIntegerProperty(zIndex);
    }

    public Media(
        long id, String name, double x, double y, double width, double height)
    {
        this(id, name, new HashSet<>(), x, y, width, height, 0, 0);
    }

    public Media(
        String name, double x, double y, double width, double height)
    {
        this(EMPTY_ID, name, new HashSet<>(), x, y, width, height, 0, 0);
    }

    public final long getID() {
        return id;
    }

    public final StringProperty nameProperty() {
        return name;
    }

    public final String getName() {
        return name.getValue();
    }

    public final ObservableSet<String> getTags() {
        return tags;
    }

    public final DoubleProperty xProperty() {
        return x;
    }

    public final double getX() {
        return x.getValue();
    }

    public final DoubleProperty yProperty() {
        return y;
    }

    public final double getY() {
        return y.getValue();
    }

    public final DoubleProperty widthProperty() {
        return width;
    }

    public final double getWidth() {
        return width.getValue();
    }

    public final DoubleProperty heightProperty() {
        return height;
    }

    public final double getHeight() {
        return height.getValue();
    }

    public final DoubleProperty angleProperty() {
        return angle;
    }

    public final double getAngle() {
        return angle.getValue();
    }

    public final IntegerProperty zIndexProperty() {
        return zIndex;
    }

    public final int getZindex() {
        return zIndex.getValue();
    }

    public final void setID(long id) throws Exception {
        if (this.id == EMPTY_ID) {
            this.id = id;
        } else {
            throw new Exception("Cannot assign ID to Media twice.");
        }
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public void setTags(Set<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public void setAngle(double angle) {
        this.angle.set(angle);
    }

    public void setZindex(int zIndex) {
        this.zIndex.set(zIndex);
    }

    /**
     * Return whether or not the Media object is within the rectangle with
     * width `w`, height `h`, and top-left corner at (`x`, `y`).
     */
    public final boolean isWithin(double x, double y, double w, double h) {
        return
            (getX() >= x - getWidth() && getX() <= x + w + getWidth())
            && (getY() >= y - getHeight() && getY() <= y + h + getHeight());
    }

    @Override
    public String toString() {
        return  getName() + " (" + getClass().getName() + ")";
    }
}
