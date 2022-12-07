package gui.view_controllers;
import gui.media.Playable;
import gui.model.GUIPlayerModel;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class GUIPlayerController {
    private final GUIPlayerModel associatedModel;

    public GUIPlayerController(Playable playerInterface, Duration totalDuration)    {
        this.associatedModel = new GUIPlayerModel(playerInterface, totalDuration);
    }

    public void adjustPlayTime(Double value, MediaPlayer.Status status) {
        associatedModel.changePlayTime(value, status);
    }

    public void firePlay(String text)    {
        associatedModel.firedPlayButton(text);
    }

    public void changePlayRate(double value)    {
        associatedModel.changePlayRate(value);
    }

    public void changeVolume(double value)  {
        associatedModel.changeVolume(value);
    }

    public String formatTime(Duration duration) {
        return associatedModel.formatTime(duration);
    }

    public void changePlaybackSlider(double value)  {
        associatedModel.changePlaybackSlider(value);
    }

    public void updatePlaybackText(Duration time)   {
        associatedModel.updatePlaybackText(time);
    }
}
