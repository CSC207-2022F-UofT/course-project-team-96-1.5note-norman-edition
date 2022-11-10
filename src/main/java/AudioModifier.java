import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioModifier implements MediaManager {

    private String TimeStamp;

    @Override
    public void createObject() {

    }

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

        MediaAudio audio = new MediaAudio(new double[]{0, 0}, new double[]{0, 0}, 0, 0, "", "",
                mediaPlayer, rawData, new MediaText[]{});

        Page page = communicator.getPage();
        page.getElements().add(audio);
    }

    @Override
    public void modifyMedia() {

    }

    @Override
    public void searchMedia() {

    }

    public void addTimeStamp(String givenTimeStamp){

        this.TimeStamp = givenTimeStamp;
    }

}
