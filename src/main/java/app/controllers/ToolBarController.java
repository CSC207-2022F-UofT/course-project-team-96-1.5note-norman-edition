package app.controllers;

import app.MediaCommunicator;
import app.interaction_managers.Tagger;
import app.media.MediaAudio;
import app.media.MediaHyperlink;
import app.media_managers.AudioModifier;
import app.media_managers.TextModifier;
import app.media_managers.VideoModifier;
import javafx.geometry.Bounds;
import gui.media.GUIMedia;
import javafx.util.Duration;
import javafx.scene.control.TextField;

public class ToolBarController {

    /*
    A lot of these reference undefined methods and have unclear jobs. None make sense to return anything right now
    though maybe later they will return booleans to signify if they completed their job or failed.
     */

    public void insertAudio(MediaCommunicator communicator, Bounds bounds) throws Exception {
        AudioModifier audioModifier = new AudioModifier();

        double x = bounds.getCenterX();
        double y = bounds.getCenterY();

        audioModifier.addMedia(communicator, x, y);
    }

    public void insertVideo(MediaCommunicator communicator, Bounds bounds) throws Exception {
        VideoModifier videoModifier = new VideoModifier();

        double x = bounds.getCenterX();
        double y = bounds.getCenterY();

        videoModifier.addMedia(communicator, x, y);
    }

    public void modifyTimestamp(MediaAudio audio, Duration timestamp, MediaCommunicator communicator) throws Exception {
        AudioModifier audioModifier = new AudioModifier();
        audioModifier.modifyMedia(audio, timestamp, communicator);
    }

    public MediaHyperlink createHyperlink(String text, String source)   {
        TextModifier textModifier = new TextModifier();
        return textModifier.createAudioTimestamp(text, source);
    }

    public void tag(TextField node, GUIMedia<?> guiMedia)
    {
        Tagger tagger = new Tagger();
        tagger.interact(node);
        tagger.addTag(guiMedia.getMedia());
    }
}
