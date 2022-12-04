package app.controllers;

import app.MediaCommunicator;
import app.draweables.SelectionTool;
import app.draweables.ShapeCreator;
import app.interaction_managers.Tagger;
import app.media.MediaPlayable;
import app.media.MediaHyperlink;
import app.media_managers.PlayableModifier;
import app.media_managers.ImageModifier;
import app.media_managers.TextModifier;
import javafx.geometry.Bounds;
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

    public void insertAudio(MediaCommunicator communicator, Bounds bounds) throws Exception {
        PlayableModifier playableModifier = new PlayableModifier();
        String[] types = new String[]{"*.mp3", "*.wav"};
        String fileType = "Audio";

        double x = bounds.getCenterX();
        double y = bounds.getCenterY();

        playableModifier.addMedia(types, fileType, communicator, x, y);
    }

    public void insertVideo(MediaCommunicator communicator, Bounds bounds) throws Exception {
        PlayableModifier playableModifier = new PlayableModifier();
        String[] types = new String[]{"*.mp4"};
        String fileType = "Video";

        double x = bounds.getCenterX();
        double y = bounds.getCenterY();

        playableModifier.addMedia(types, fileType, communicator, x, y);
    }

    public void modifyTimestamp(MediaPlayable audio, Duration timestamp, MediaCommunicator communicator) throws Exception {
        PlayableModifier playableModifier = new PlayableModifier();
        playableModifier.modifyMedia(audio, timestamp, communicator);
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
