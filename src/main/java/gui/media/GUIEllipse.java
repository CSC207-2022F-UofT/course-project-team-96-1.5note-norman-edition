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
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        ellipse = new Ellipse(0, 0, width / 2, height / 2);
        ellipse.setFill(colour);
        getChildren().add(ellipse);
    }

    public GUIEllipse(EllipseShape media) {
        super(media);
        setEllipse(media);
    }

    @Override
    public void mediaUpdated(Media media) {
        EllipseShape newEllipse = (EllipseShape) media;
        EllipseShape currentEllipse = (EllipseShape) getMedia();

        if (currentEllipse != newEllipse) {
            setMedia(newEllipse);
            setEllipse(newEllipse);
        }
    }

    public void setEllipse(EllipseShape ellipse) {
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
