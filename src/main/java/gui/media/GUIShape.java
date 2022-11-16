package gui.media;

import app.media.GenericShape;
import javafx.geometry.Point2D;

public class GUIShape extends GUIMedia<GenericShape> {
    private GUIShape shape;

    public GUIShape(GenericShape media) {
        super(media);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){}
}

