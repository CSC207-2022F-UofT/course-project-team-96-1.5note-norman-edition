package gui.media;

import app.media.GenericShape;
import app.media.Media;
import app.media.PolygonShape;
import app.media.RectangleShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class GUIRectangle extends GUIShape {
    private Rectangle rectangle;

    public GUIRectangle(Point2D p1, Point2D p2, Color colour) {
        super(new RectangleShape(0,0,0,0, colour.toString()));
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);

        rectangle = new Rectangle(0, 0, width, height);
        rectangle.setFill(colour);
        getChildren().add(rectangle);
    }

    public GUIRectangle(RectangleShape media) {
        super(media);
        setRectangle(media);
    }

    @Override
    public void mediaUpdated(Media media) {
        RectangleShape newRectangle = (RectangleShape) media;
        RectangleShape currentRectangle = (RectangleShape) getMedia();

        if (currentRectangle != newRectangle) {
            setMedia(newRectangle);
            setRectangle(newRectangle);
        }
    }

    public void setRectangle(RectangleShape rectangle) {
        Color colour = Color.valueOf(rectangle.getColour());
        this.rectangle = new Rectangle(0,0, rectangle.getWidth(), rectangle.getHeight());
        this.rectangle.setFill(colour);
        getChildren().add(this.rectangle);
    }
    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){
        double[] result = RestrictPoints(p1, p2, sameSideLengths);

        double centerX = result[0];
        double centerY = result[1];
        double width = result[2];
        double height = result[3];

        // Updating the graphics of our shape
        getMedia().setX(centerX - width/2);
        getMedia().setY(centerY - height/2);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
    }
}
