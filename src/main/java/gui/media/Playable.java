package gui.media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public interface Playable {

    //Set the associated player to play from the specified location
    void setPlayerDuration(Duration time);

    //Play the associated player
    void play();

    //Pause the associated player
    void pause();

    //Get the current duration of the player
    Duration getCurrentDuration();

    //Set what the playback text reads
    void setPlaybackText(String text);

    //Set the playback rate of the player
    void setPlaybackRate(double value);

    //Set the playback slider to the specified value
    void setPlaybackSliderValue(double value);

    //Sets the text read by the play button
    void setPlayButtonText(String text);

    //Set the player to play at the specified volume
    void setPlayerVolume(double value);
}
