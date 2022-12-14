package app.media;

import javafx.beans.property.*;
import javafx.collections.*;

import java.io.*;
import java.util.Set;
import java.util.HashSet;


/**
 * Core entity representing a piece of media which can be displayed on a page.
 * <p>
 * Media and its sub-classes all have the following properties:
 * <ul>
 * <li>`id`: A unique identifier. This may initially be the placeholder value
 * given by `EMPTY_ID`, but once it has been set to a non-placeholder value it
 * can no longer be changed.
 * <li>`name`: The name of the Media. This does not have to be unique.
 * <li>`tags`: A set of strings which further identify the media.
 * <li>`x`: X-coordinate of a point within the media
 * <li>`y`: Y-coordinate of a point within the media
 * <li>`width`: Width of the media
 * <li>`height`: Height of the media
 * <li>`angle`: Angle of rotation of the media
 * <li>`zIndex`: Z-index defines the "order" of Media. Media with a lower
 * Z-index should be displayed "above" Media with a higher Z-index.
 * </ul>
 */
public class Media implements Serializable {

    public static final Long EMPTY_ID = 0L;

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
    private record Data(
            long id,
            String name, Set<String> tags,
            double x, double y,
            double width, double height,
            double angle, int zIndex) implements Serializable {}

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        Data d = new Data(
                id, getName(), new HashSet<>(tags), getX(), getY(),
                getWidth(), getHeight(), getAngle(), getZindex());

        out.writeObject(d);
    }

    @Serial
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        Data d = (Data) in.readObject();
        initializeProperties(
                d.id(), d.name(), d.tags(), d.x(), d.y(),
                d.width(), d.height(), d.angle(), d.zIndex());
    }

    @Serial
    @SuppressWarnings("EmptyMethod")
    private void readObjectNoData() throws ObjectStreamException {}

    // the above `writeObject`, `readObject`, and `readObjectNoData` methods
    // are all required in order to re-define the serialization/deserialization
    // procedure for this class.
    //
    // https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/Serializable.html


    /**
     * Instantiate a new Media object given all fields.
     *
     * @param id The unique identifier for this media
     * @param name The inital name for this media
     * @param tags The initial tags for this media
     * @param x The initial horizontal position for this media
     * @param y The initial vertical position for this media
     * @param width The initial width of this media
     * @param height The initial height of this media
     * @param angle The initial angle of rotation of this media
     * @param zIndex The initial Z-index of this media
     */
    public Media(
            long id, String name, Set<String> tags, double x, double y,
            double width, double height, double angle, int zIndex)
    {
        initializeProperties(id, name, tags, x, y, width, height, angle, zIndex);
    }

    /**
     * Instantiate a new Media object given all fields <i>except</i> ID.
     * <p>
     * The Media's ID will be initialized to a placeholder value and can be
     * set later.
     *
     * @param name The inital name for this media
     * @param tags The initial tags for this media
     * @param x The initial horizontal position for this media
     * @param y The initial vertical position for this media
     * @param width The initial width of this media
     * @param height The initial height of this media
     * @param angle The initial angle of rotation of this media
     * @param zIndex The initial Z-index of this media
     */
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
        this.tags = FXCollections.observableSet(tags);
        this.tags.addAll(tags);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.width = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);
        this.angle = new SimpleDoubleProperty(angle);
        this.zIndex = new SimpleIntegerProperty(zIndex);
    }

    /**
     * Instantiate a new Media object given only ID, name, position and size.
     * <p>
     * The tags will be initialized to the empty set while Z-index and angle
     * will be initialized to 0.
     *
     * @param id The unique identifier for this media
     * @param name The inital name for this media
     * @param x The initial horizontal position for this media
     * @param y The initial vertical position for this media
     * @param width The initial width of this media
     * @param height The initial height of this media
     */
    public Media(
        long id, String name, double x, double y, double width, double height)
    {
        this(id, name, new HashSet<>(), x, y, width, height, 0, 0);
    }

    /**
     * Instantiate a new Media object given only name, position and size.
     * <p>
     * The tags will be initialized to the empty set while Z-index and angle
     * will be initialized to 0. The ID will be initialized to the placeholder
     * value.
     *
     * @param name The inital name for this media
     * @param x The initial horizontal position for this media
     * @param y The initial vertical position for this media
     * @param width The initial width of this media
     * @param height The initial height of this media
     */
    public Media(
        String name, double x, double y, double width, double height)
    {
        this(EMPTY_ID, name, new HashSet<>(), x, y, width, height, 0, 0);
    }

    /**
     * Return the unique identifier for this Media.
     */
    public final long getID() {
        return id;
    }

    /**
     * Set the unique identifier on this Media.
     * Setting the ID is only a valid operation if the media has not yet been
     * assigned an ID.
     *
     * @throws UnsupportedOperationException if the placeholder ID is assigned,
     * or if an ID is assigned when the Media already has a non-placeholder ID.
     */
    public final void setID(long id) throws UnsupportedOperationException {
        if (this.id == EMPTY_ID && id != EMPTY_ID) {
            this.id = id;
        } else {
            throw new UnsupportedOperationException();
        }
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
     * Check if this media lies within the given rectangular region.
     *
     * @param x The X-coordinate of the top-left corner of the rectangular
     * region.
     * @param y The Y-coordinate of the top-left corner of the rectangular
     * region.
     * @param w The width of the rectangular region.
     * @param h The height of the rectangular region.
     *
     * @return Whether or not the Media object is within the rectangle with
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
