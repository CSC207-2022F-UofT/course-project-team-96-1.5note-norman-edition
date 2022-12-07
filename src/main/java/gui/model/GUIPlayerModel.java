package gui.model;
import gui.media.Playable;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


/**
 * Use case class that changes the view of a GUIAudio based on how the user interacts with it
 * */
public class GUIPlayerModel {
    private final Playable associatedPlayer;
    private Duration totalDuration;

    public GUIPlayerModel(Playable associatedPlayer, Duration totalDuration)    {
        this.associatedPlayer = associatedPlayer;
        this.totalDuration = totalDuration;
    }

    /** Defines what occurs when the play button is pressed.
     * <p>
     * If the button currently reads "Play", then it will play the associated player. If it reads "Pause", it will pause
     * the associated player
     * @param currentText the current text on the GUIAudio play button
     */
    public void firedPlayButton(String currentText)   {
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

    /** Adjusts where the associated GUIAudio will play from when the user interacts with the playback slider
     * <p>
     * Precondition: 0 &lt;= value &lt;= 1
     * @param value what % of the full duration of associated audio to play from
     * @param mediaStatus current status of the associated GUIAudio
     */
    public void changePlayTime(double value, MediaPlayer.Status mediaStatus)    {
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

    public void changePlaybackSlider(double value)  {
        associatedPlayer.setPlaybackSliderValue(value);
    }

    public void updatePlaybackText(Duration newDuration)    {
        //Changes what the current playback text reads
        associatedPlayer.setPlaybackText(formatTime(newDuration));
    }

    /** Formats a duration into a more readable time format
     * @param time Duration value to convert to a time format
     * @return A time format of Duration parameter time
     */
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
