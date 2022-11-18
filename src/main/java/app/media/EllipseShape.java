package app.media;

public class EllipseShape extends GenericShape {
    public EllipseShape(double x, double y, double width, double height, String colour) {
        super("Ellipse", x, y, 0, 0, colour);
    }

    @Override
    public String toString() {
        return "Ellipse: x["+super.getX()+"] y["+super.getY()+"] w["+super.getWidth()+"] h["+super.getHeight()+"] c["+super.getColour()+"]";
    }
}
