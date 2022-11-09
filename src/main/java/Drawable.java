
public interface Drawable {
    /** Takes in two positions from mouse input and does something with them
     */
    public default void useTwoPositions(double[] first, double[] second){}
}
