package app.drawables;

import javafx.geometry.Point2D;

public interface Drawable {
    /** Takes in two positions from mouse input and does something with them
     */
    public default void useTwoPositions(Point2D first, Point2D second, String shape){}
}
