package app.media;

import javafx.beans.*;
import javafx.beans.value.*;
import javafx.beans.property.*;
import javafx.collections.*;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


/**
 * Core entity representing a piece of media which can be displayed on a page.
 */
public class Media implements Serializable {

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
            String name, Set<String> tags,
            double x, double y,
            double width, double height,
            double angle, int zIndex) implements Serializable {}

    private void writeObject(ObjectOutputStream out) throws IOException {
        Data d = new Data(
                getName(), new HashSet<>(tags), getX(), getY(),
                getWidth(), getHeight(), getAngle(), getZIndex());

        out.writeObject(d);
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        Data d = (Data) in.readObject();
        setFields(
                d.name(), d.tags(), d.x(), d.y(),
                d.width(), d.height(), d.angle(), d.zIndex());
    }

    private void readObjectNoData() throws ObjectStreamException {}

    // the above `writeObject`, `readObject`, and `readObjectNoData` methods
    // are all required in order to re-define the serialization/deserialization
    // procedure for this class.
    //
    // https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/Serializable.html


    public Media(
            String name, Set<String> tags, double x, double y,
            double width, double height, double angle, int zIndex)
    {
        setFields(name, tags, x, y, width, height, angle, zIndex);
    }


    private void setFields(
            String name, Set<String> tags, double x, double y,
            double width, double height, double angle, int zIndex)
    {
        this.name = new SimpleStringProperty(name);
        this.tags = new SimpleSetProperty<String>();
        this.tags.addAll(tags);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.width = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);
        this.angle = new SimpleDoubleProperty(angle);
        this.zIndex = new SimpleIntegerProperty(zIndex);
    }

    public Media(
        String name, double x, double y, double width, double height)
    {
        this(name, new HashSet<>(), x, y, width, height, 0, 0);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.getValue();
    }

    public ObservableSet<String> getTags() {
        return tags;
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public double getX() {
        return x.getValue();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public double getY() {
        return y.getValue();
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public double getWidth() {
        return width.getValue();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public double getHeight() {
        return height.getValue();
    }

    public DoubleProperty angleProperty() {
        return angle;
    }

    public double getAngle() {
        return angle.getValue();
    }

    public IntegerProperty zIndexProperty() {
        return zIndex;
    }

    public int getZIndex() {
        return zIndex.getValue();
    }

    @Override
    public String toString() {
        return  getName() + " (" + getClass().getName() + ")";
    }
}
