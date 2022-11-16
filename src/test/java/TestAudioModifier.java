import app.MediaCommunicator;
import app.media.MediaAudio;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.scene.media.Media;
import javafx.util.Duration;
import org.junit.*;
import storage.FileLoaderWriter;
import storage.SQLiteStorage;
import storage.Storage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;

/** NOTE: because junit doesnt like working with javafx, this test class uses modified versions of methods
 * actually found in AudioModifier. The logic is more or less the exact same, just the methods of doing things have been altered.
 * Also these tests are based on files on my PC, if you want to run them youd need to modify paths for some of your own
 */


public class TestAudioModifier {
    private Duration TimeStamp;
    private MediaAudio audio;
    private Page page;

    @Test
    public void testAddingAudio_mp3()  {
        //Testing that an audioGUI can be made using a mp3 input, and that the javafx.Media can be initialized
        createPage();
        GUIAudio audioGUI = addMedia("src\\test\\java\\1.17 Axe to Grind.mp3");
        initializeMediaPlayer(audioGUI.getMedia());
    }

    @Test
    public void testAddingAudio_wav()  {
        //Testing that an audioGUI can be made using a wav input, and that the javafx.Media can be initialized
        createPage();
        GUIAudio audioGUI = addMedia("src\\test\\java\\1.17 Axe to Grind.wav");
        initializeMediaPlayer(audioGUI.getMedia());
    }

    public void createPage()    {
        try {
            SQLiteStorage storage = new SQLiteStorage(null);
            MediaCommunicator mediaCommunicator = new MediaCommunicator(storage);
            Page page = new Page(mediaCommunicator);
            this.page = page;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GUIAudio addMedia(String source) {
        //Loading raw audio data based on user selection
        byte[] rawData = readFile(source);

        try {
            MediaAudio audio = new MediaAudio("", 0, 0, 0, 0, rawData, new ArrayList<Duration>(),
                    0); //Temp Constructor
            GUIAudio audioGUI = new GUIAudio(audio);
            this.page.updateMedia(audioGUI);
            this.page.addMedia(audioGUI);
            return audioGUI; //returned in this version for testing purposes
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readFile(String path) {
        File chosenFile = new File(path);
        try {
            return Files.readAllBytes(chosenFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: temp solution
        }
    }

    public void initializeMediaPlayer(MediaAudio audio)  {
        String path = "temp\\id" + Double.toString(audio.getID());
        Storage fw = new FileLoaderWriter();
        URI tempFile = fw.writeFile(path, audio.getRawData()); //Creating temp file for use by javafx.Media Class
        Media audioMedia = new Media(tempFile.toString());
    }



}
