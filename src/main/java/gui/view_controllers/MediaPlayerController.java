package gui.view_controllers;

import gui.media.GUIAudio;
import gui.model.GUIPlayerModel;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaPlayerController {
    //Controller for Playable GUIMedia types
    private GUIPlayerModel associatedModel;

    public MediaPlayerController(GUIAudio associatedPlayer, Duration totalDuration)  {
        this.associatedModel = new GUIPlayerModel(associatedPlayer, totalDuration);
    }

    public void firePlayButton(String currentText)  {
        associatedModel.firedPlayButton(currentText);
    }

    public void changePlayRate(double value)    {
        associatedModel.changePlayRate(value);
    }

    public void changePlayVolume(double value)    {
        associatedModel.changeVolume(value);
    }

    public void changePlayback(double value, MediaPlayer.Status mediaStatus)    {
        associatedModel.playbackSliderAdjusted(value, mediaStatus);
    }

    public void changePlaybackText(Duration newTime)    {
        associatedModel.updatePlaybackText(newTime);
    }

    public GUIPlayerModel getAssociatedModel() {
        return associatedModel;
    }
}
