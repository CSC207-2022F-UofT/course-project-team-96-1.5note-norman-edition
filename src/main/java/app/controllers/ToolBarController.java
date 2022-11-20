package app.controllers;

import app.draweables.SelectionTool;
import app.draweables.ShapeCreator;
import app.interaction_managers.Tagger;
import app.media.MediaAudio;
import app.media.MediaHyperlink;
import app.media_managers.AudioModifier;
import app.media_managers.ImageModifier;
import app.media_managers.TextModifier;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class ToolBarController {

    /*
    A lot of these reference undefined methods and have unclear jobs. None make sense to return anything right now
    though maybe later they will return booleans to signify if they completed their job or failed.
     */

    public void insertText() throws Exception {
        TextModifier textModifier = new TextModifier();
        textModifier.addMedia();
    }

    public void insertImage() throws Exception {
        ImageModifier imageModifier = new ImageModifier();
        imageModifier.addMedia();
    }

    public void insertAudio(Page page) throws Exception {
        AudioModifier audioModifier = new AudioModifier();
        audioModifier.setPage(page);
        audioModifier.addMedia();
    }

    public void modifyTimestamp(MediaAudio audioUI, Duration Timestamp, Page page) throws Exception {
        AudioModifier audioModifier = new AudioModifier();
        audioModifier.setAudio(audioUI);
        audioModifier.addTimeStamp(Timestamp);
        audioModifier.setPage(page);
        audioModifier.modifyMedia();
    }

    public MediaHyperlink createHyperlink(String text, String source)   {
        TextModifier textModifier = new TextModifier();
        return textModifier.createAudioTimestamp(text, source);
    }

    public void drawShape(Point2D point1, Point2D point2, String shape) {
        ShapeCreator shapeCreator = new ShapeCreator();

        shapeCreator.useTwoPositions(point1, point2, shape);
    }

    public void select(Point2D point1, Point2D point2, String shape)
    {
        SelectionTool selectionTool = new SelectionTool();
        selectionTool.useTwoPositions(point1, point2, shape);
    }

    public void tag() //TODO: add neccessary input
    {
        Tagger tagger = new Tagger();
        tagger.interact();
    }
}
