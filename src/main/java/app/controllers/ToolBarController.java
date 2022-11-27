package app.controllers;

import app.MediaCommunicator;
import app.interaction_managers.Tagger;
import app.media.MediaAudio;
import app.media.MediaHyperlink;
import app.media_managers.AudioModifier;
import app.media_managers.ImageModifier;
import app.media_managers.TextModifier;
import gui.media.GUIMedia;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.scene.control.TextField;

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

    public void insertAudio(MediaCommunicator communicator) throws Exception {
        AudioModifier audioModifier = new AudioModifier();
        audioModifier.setCommunicator(communicator);
        audioModifier.addMedia();
    }

    public void modifyTimestamp(MediaAudio audioUI, Duration Timestamp, MediaCommunicator communicator) throws Exception {
        AudioModifier audioModifier = new AudioModifier();
        audioModifier.setAudio(audioUI);
        audioModifier.addTimeStamp(Timestamp);
        audioModifier.setCommunicator(communicator);
        audioModifier.modifyMedia();
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
