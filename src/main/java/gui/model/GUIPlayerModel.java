package gui.model;
import gui.media.GUIAudio;
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
        //Precondition: 0 <= value <= 1
        Duration newTime = new Duration(value * totalDuration.toMillis());
        associatedPlayer.setPlayerDuration(newTime);
        //When status is ready, the audio player currentTime property does not update until the player plays
        if (mediaStatus == MediaPlayer.Status.READY)  {
            updatePlaybackText(newTime);
        }
    }

    public void updatePlaybackText(Duration newDuration)    {
        int seconds = (int) newDuration.toSeconds() % 60;
        int minutes = (int) newDuration.toMinutes() % 60;
        int hours = (int) newDuration.toHours();
        String[] timeProperties = {Integer.toString(seconds), Integer.toString(minutes), Integer.toString(hours)};
        for (int i = 0; i < timeProperties.length; i++)   {
            if (timeProperties[i].length() == 1) {
                timeProperties[i] = "0" + timeProperties[i];
            }}
        associatedPlayer.setPlaybackText(timeProperties[2] + ":" + timeProperties[1] + ":" + timeProperties[0]);
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }
}
