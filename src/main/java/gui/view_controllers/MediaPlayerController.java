package gui.view_controllers;

import gui.media.GUIAudio;
import gui.model.GUIPlayerModel;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Defines controls for different elements of a GUIAudio
 */
public class MediaPlayerController {
    private final GUIPlayerModel associatedModel;

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

    public String createFormattedTime(Duration time)    {return associatedModel.formatTime(time);}

    public GUIPlayerModel getAssociatedModel() {
        return associatedModel;
    }
}
