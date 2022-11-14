package gui.tool;

import gui.media.GUIPenStroke;
import gui.media.GUIShape;
import gui.page.Page;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.awt.*;

public class ShapeTool implements Tool {
    private HandlerMethod[] handlers;
    private Page page;
    private ObjectProperty<Color> colour;

    private GUIShape currentShape;

    private Point2D point1;

    private Point2D point2;

    public ShapeTool(ObjectProperty<Color> colour) {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_PRESSED, this::startShape),
                new HandlerMethod<>(MouseEvent.MOUSE_RELEASED, this::endShape)
        };
        this.handlers = handlers;

        this.colour = colour;
    }

    @Override
    public String getName() {
        return "Shape";
    }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public void disabledFor(Page page) {
        this.page = null;
    }


    private void startShape(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            point1 = page.getMouseCoords(e);
        }
    }

    private void endShape(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            point2 = page.getMouseCoords(e);
            finishShape();
        }
    }

    private void finishShape() {
        if (point1 != null && point2 != null){
            currentShape = new GUIShape(point1, point2, colour.getValue());
            page.addMedia(currentShape);
            currentShape = null;
        }
    }
}
