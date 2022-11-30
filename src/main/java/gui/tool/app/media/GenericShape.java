package gui.tool.app.media;

/**
 * A sub-class of Media representing an arbitrary shape
 */
public abstract class GenericShape extends Media {
    protected String colour;

    /**
     * Initializes a Generic Shape with the given parameters
     * @param type The shape's type
     * @param x The shape's x position
     * @param y The shape's y position
     * @param width The shape's width
     * @param height The shape's height
     * @param colour The shape's color
     */
    public GenericShape(String type, double x, double y, double width, double height, String colour) {
        super(type, x, y, 0, 0);
        this.colour = colour;
    }

    /**
     * Returns the color of the shape
     * @return the color of the shape
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets the colour of the shape
     * @param colour The desired color to set the shape to
     */
    public void setColour(String colour) {
        this.colour = colour;
    }
}

