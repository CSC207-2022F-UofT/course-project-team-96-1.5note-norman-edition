import javafx.scene.media.Media;

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
        String path = communicator.findFile();
        byte[] rawData = communicator.readFile(path);

        //Creating related MediaAudio object
        Media audioUI = new Media("");
        MediaAudio audio = new MediaAudio(new double[]{0, 0}, new double[]{0, 0}, 0, 0, "", "",
                audioUI, rawData, new MediaText[]{});

        communicator.addToPage(audio);
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
