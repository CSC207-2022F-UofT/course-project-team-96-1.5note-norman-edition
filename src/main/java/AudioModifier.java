import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.util.ArrayList;


public class AudioModifier implements MediaManager {
    // Class defines methods to both create new MediaAudio objects and modify existing ones
    //Instance Attributes:
    //  timeStamp: A point in an audio file that an audio hyperlink will be made to
    //  audio: a MediaAudio object to be modified
    private Duration timeStamp;
    private MediaAudio audio;

    @Override
    public void addMedia() {
        // Creates a new MediaAudio object based on user selection then adds it to the Page

        //Allowing user to select a file then saving the data
        MediaCommunicator communicator = new MediaCommunicator();
        byte[] rawData = communicator.readFile("");

        // Creating a temp file which can be used by the MediaPlayer
        communicator.writeFile("temp/", rawData);

        //Creating related MediaAudio object
        Media audioUI = new Media("temp/");
        MediaPlayer mediaPlayer = new MediaPlayer(audioUI);
        MediaAudio audio = new MediaAudio(new double[]{0, 0}, new double[]{0, 0}, 0, 0, 0, "",
                mediaPlayer, rawData, new ArrayList<MediaHyperlink>());

        Page page = communicator.getPage();
        page.getElements().add(audio);
    }

    //PRECONDITION: timeStamp is a valid point in audio
    @Override
    public void modifyMedia() {
        //Calling TextModifier to create the neccessary Hyperlink
        TextModifier textCreator = new TextModifier();
        MediaHyperlink hyperlink = textCreator.createAudioHyperlink(this.timeStamp);

        //Setting the hyperlink to play associated audio at requested time point
        hyperlink.getText().setOnAction(e ->    {
            this.audio.getAudio().seek(this.timeStamp);
            this.audio.getAudio().play();
        });
        this.audio.getTimestamps().add(hyperlink);

    }

    @Override
    public void searchMedia() {

    }

    public void addTimeStamp(Duration givenTimeStamp){
        this.timeStamp = givenTimeStamp;
    }

    public void addMedia(MediaAudio audio)  {
        this.audio = audio;
    }

    public MediaAudio getMedia()    {return this.audio; }

}
