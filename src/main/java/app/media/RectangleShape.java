package app.media;

public class RectangleShape extends GenericShape {
    public RectangleShape(double x, double y, double width, double height, String colour) {
        super("Rectangle", x, y, 0, 0, colour);
    }

    @Override
    public String toString() {
        return "Rectangle: x["+super.getX()+"] y["+super.getY()+"] w["+super.getWidth()+"] h["+super.getHeight()+"] c["+super.getColour()+"]";
    }
}
