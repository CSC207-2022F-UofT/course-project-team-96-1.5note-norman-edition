package gui.model;
import gui.media.GUIAudio;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class GUIPlayerModel {
    //Defines controls for changing the view of a GUIAudio
    private GUIAudio associatedPlayer;
    private Duration totalDuration;

    public GUIPlayerModel(GUIAudio associatedPlayer, Duration totalDuration)    {
        this.associatedPlayer = associatedPlayer;
        this.totalDuration = totalDuration;
    }

    public void firedPlayButton(String currentText)   {
        //Defines what occurs when the play button is pressed
        if (currentText.equals("Play"))  {
            //Checking if the player is at the end of the audio
            if(associatedPlayer.getCurrentDuration().equals(totalDuration)) {
                associatedPlayer.setPlayerDuration(new Duration(0));
            }
            associatedPlayer.play();
            associatedPlayer.setPlayButtonText("Pause");
        }   else {
            //If the player is playing, we can pause it instead
            associatedPlayer.pause();
            associatedPlayer.setPlayButtonText("Play");
        }
    }

    //These 2 methods seem roundabout, but they allow for actually testing the GUIAudio
    public void changePlayRate(double value)    {
        associatedPlayer.setPlaybackRate(value);
    }

    public void changeVolume(double value)    {
        associatedPlayer.setPlayerVolume(value);
    }

    public void playbackSliderAdjusted(double value, MediaPlayer.Status mediaStatus)    {
        //Defines what occurs when the playback slider is pressed by the user
        //Precondition: 0 <= value <= 1
        Duration newTime = new Duration(value * totalDuration.toMillis());
        //When status is ready, the audio player currentTime property does not update until the player plays
        if (mediaStatus == MediaPlayer.Status.READY)  {
            updatePlaybackText(newTime);
            associatedPlayer.play();
            associatedPlayer.setPlayerDuration(newTime);
            associatedPlayer.pause();
        }   else {
            associatedPlayer.setPlayerDuration(newTime);
        }
    }

    public void updatePlaybackText(Duration newDuration)    {
        //Changes what the current playback text reads
        associatedPlayer.setPlaybackText(formatTime(newDuration));
    }

    public String formatTime(Duration time) {
        //Formats a duration into a readable format
        int seconds = (int) time.toSeconds() % 60;
        int minutes = (int) time.toMinutes() % 60;
        int hours = (int) time.toHours();
        String[] timeProperties = {Integer.toString(seconds), Integer.toString(minutes), Integer.toString(hours)};
        for (int i = 0; i < timeProperties.length; i++)   {
            if (timeProperties[i].length() == 1) {
                timeProperties[i] = "0" + timeProperties[i];
            }}
        return timeProperties[2] + ":" + timeProperties[1] + ":" + timeProperties[0];
    }

    //Method exists for testing
    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }
}
