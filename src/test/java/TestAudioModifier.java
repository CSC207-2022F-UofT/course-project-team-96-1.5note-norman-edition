import app.MediaCommunicator;
import app.media.MediaAudio;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.util.Duration;
import org.junit.*;
import storage.SQLiteStorage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import javafx.application.Platform;

/** NOTE: because junit doesnt like working with javafx, this test class uses modified versions of methods
 * actually found in AudioModifier. The logic is more or less the exact same, just the methods of doing things have been altered.
 */


public class TestAudioModifier {
    private Duration TimeStamp;
    private MediaAudio audio;
    private Page page;
    private static Boolean init = false;


    static void initJfxRuntime() {
        if(!init)   {
            Platform.startup(() -> {});
            init = true;
        }
    }

    @Test
    public void testAddingAudio_mp3()  {
        //Testing that a MediaAudio can be made using a generic mp3 input, and that the javafx.Media can be initialized
        initJfxRuntime();
        createPage();
        GUIAudio audioGUI = addMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.mp3");
    }

    @Test
    public void testAddingAudio_wav()  {
        //Testing that a MediaAudio  can be made using a generic wav input, and that the javafx.Media can be initialized
        initJfxRuntime();
        createPage();
        GUIAudio audioGUI = addMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.wav");
    }

    @Test
    public void testAddingAudio_mp3_empty()  {
        //Testing that a MediaAudio can be made using an "empty" mp3 input, and that the javafx.Media can be initialized
        initJfxRuntime();
        createPage();
        GUIAudio audioGUI = addMedia("src\\test\\java\\test_files\\empty.mp3");
    }

    @Test
    public void testAddingAudio_wav_empty()  {
        //Testing that a MediaAudio can be made using an "empty" wav input, and that the javafx.Media can be initialized
        initJfxRuntime();
        createPage();
        GUIAudio audioGUI = addMedia("src\\test\\java\\test_files\\empty_1c.wav");
    }

    @Test
    public void testAddingAudio_mp3_long()  {
        //Testing that a MediaAudio can be made using a long mp3 input, and that the javafx.Media can be initialized
        initJfxRuntime();
        createPage();
        GUIAudio audioGUI = addMedia("src\\test\\java\\test_files\\NieR Automata OST with Rain.mp3");
    }

    //No test for long WAVs because they're evidently not compressed and way too big for java to handle

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
            MediaAudio audio = new MediaAudio("", 0, 0, 0, 0, rawData, new ArrayList<Duration>()); //Temp Constructor
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
            throw new RuntimeException(e);
        }
    }




}
