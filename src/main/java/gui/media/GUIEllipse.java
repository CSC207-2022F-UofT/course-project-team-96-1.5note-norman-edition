package gui.media;

import app.media.EllipseShape;
import app.media.Media;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class GUIEllipse extends GUIShape {
    private Ellipse ellipse;

    public GUIEllipse(Point2D p1, Point2D p2, Color colour) {
        super(new EllipseShape(0, 0,0,0, colour.toString()));
        double[] result = RestrictPoints(p1, p2, false);
        double posX = result[0];
        double posY = result[1];
        double width = result[2];
        double height = result[3];

        // Updating dimensions of media
        getMedia().setX(posX - width/2);
        getMedia().setY(posY - height/2);
        getMedia().setWidth(width);
        getMedia().setHeight(height);

        ellipse = new Ellipse(0, 0, width / 2, height / 2);
        ellipse.setFill(colour);
        getChildren().add(ellipse);
    }

    public GUIEllipse(EllipseShape media) {
        super(media);
        setGenericShape(media);
    }

    public void setGenericShape(EllipseShape ellipse) {
        Color colour = Color.valueOf(ellipse.getColour());
        this.ellipse = new Ellipse(0,0,ellipse.getWidth()/2,ellipse.getHeight()/2);
        this.ellipse.setFill(colour);
        getChildren().add(this.ellipse);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths) {
        double[] result = RestrictPoints(p1, p2, sameSideLengths);

        double centerX = result[0];
        double centerY = result[1];
        double width = result[2];
        double height = result[3];

        // Updating the graphics of our shape
        getMedia().setX(centerX);
        getMedia().setY(centerY);
        ellipse.setRadiusX(width/2);
        ellipse.setRadiusY(height/2);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
