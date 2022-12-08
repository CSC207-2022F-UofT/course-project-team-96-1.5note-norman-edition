package app.controllers;

import app.MediaCommunicator;
import app.interaction_managers.Tagger;
import app.media.MediaPlayable;
import app.media.MediaHyperlink;
import app.media_managers.PlayableModifier;
import app.media_managers.TextModifier;
import javafx.geometry.Bounds;
import gui.media.GUIMedia;
import javafx.util.Duration;

public class ToolBarController {

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

    public void tag(String node, GUIMedia<?> guiMedia)
    {
        Tagger tagger = new Tagger();
        tagger.interact(node);
        tagger.addTag(guiMedia.getMedia());
    }

    public void removeTag(String node, GUIMedia<?> guiMedia){
        Tagger tagger = new Tagger();
        tagger.interact(node);
        tagger.removeTag(guiMedia.getMedia());
    }

}
