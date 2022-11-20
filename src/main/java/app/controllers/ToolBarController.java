package app.controllers;

import app.media_managers.AudioModifier;
import app.media_managers.ImageModifier;
import javafx.geometry.Point2D;

public class ToolBarController {

    /*
    A lot of these reference undefined methods and have unclear jobs. None make sense to return anything right now
    though maybe later they will return booleans to signify if they completed their job or failed.
     */

    public void insertText() {
        TextModifier textModifier = new TextModifier();
        textModifier.addMedia();
    }

    public void insertImage() {
        ImageModifier imageModifier = new ImageModifier();
        imageModifier.addMedia();
    }

    public void insertAudio() {
        AudioModifier audioModifier = new AudioModifier();
        audioModifier.addMedia();
    }

    public void drawShape(Point2D point1, Point2D point2, String shape) {
        ShapeCreator shapeCreator = new ShapeCreator();

        shapeCreator.useTwoPositions(point1, point2, shape);
    }

    // TODO have to figure out how UI stuff works
    public void select(Point2D point1, Point2D point2, String shape)
    {
        SelectionTool selectionTool = new SelectionTool();
        selectionTool.useTwoPositions(point1, point2, shape);
    }

    public void tag(javafx.scene.Node userInput) //TODO: add neccessary input
    {
        Tagger tagger = new Tagger();
        tagger.interact(userInput);
    }
}



