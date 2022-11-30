import app.MediaCommunicator;
import app.media.MediaAudio;
import app.media_managers.AudioModifier;
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
    public static Page page;
    private static Boolean init = false;


    @BeforeClass
    public static void initJfxRuntime(){
        Platform.startup(() -> {});
        page = createPage();
    }

    public void configureAudio(GUIAudio audioGUI) throws Exception {
        //Because the setOnReady call gets skipped, these need to be manually initialized
        audioGUI.configureUI();
        audioGUI.createInterface();
    }

    @Test
    public void testAddingAudio_mp3()  {
        //Testing that a MediaAudio can be made using a generic mp3 input, and that the javafx.Media can be initialized
        addMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.mp3");
    }

    @Test
    public void testAddingAudio_wav()  {
        //Testing that a MediaAudio  can be made using a generic wav input, and that the javafx.Media can be initialized
        addMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.wav");
    }

    @Test
    public void testAddingAudio_mp3_long()  {
        //Testing that a MediaAudio can be made using a long mp3 input, and that the javafx.Media can be initialized
        addMedia("src\\test\\java\\test_files\\NieR Automata OST with Rain.mp3");
    }

    //No test for long WAVs because they're evidently not compressed and way too big for java to handle

    public GUIAudio createAudio() throws Exception {
        GUIAudio audio = addMedia("src\\test\\java\\test_files\\NieR Automata OST with Rain.mp3");
        configureAudio(audio);
        return audio;
    }

    @Test
    public void testAddingHyperlink_start() throws Exception {
        //testing that a hyperlink can be added for the beginning of the media
        GUIAudio audio = addMedia("src\\test\\java\\test_files\\NieR Automata OST with Rain.mp3");

        AudioModifier am = new AudioModifier();
        am.modifyMedia(audio.getMedia(), new Duration(0), page.getCommunicator());

        assert audio.getMedia().getTimestamps().size() == 1;
        assert audio.getMedia().getTimestamps().get(0).equals(new Duration(0));
    }

    @Test
    public void testAddingHyperlink_middle() throws Exception {
        //testing that a hyperlink can be added for the middle of the media
        GUIAudio audio = createAudio();
        AudioModifier am = new AudioModifier();
        Duration middle = new Duration(audio.getAudioPlayer().getTotalDuration().divide(2).toMillis());
        am.modifyMedia(audio.getMedia(), middle, page.getCommunicator());

        assert audio.getMedia().getTimestamps().size() == 1;
        assert audio.getMedia().getTimestamps().get(0).equals(middle);
    }

    @Test
    public void testAddingHyperlink_end() throws Exception {
        //testing that a hyperlink can be added for the end of the media
        GUIAudio audio = createAudio();

        AudioModifier am = new AudioModifier();
        Duration end = new Duration(audio.getAudioPlayer().getTotalDuration().toMillis());
        am.modifyMedia(audio.getMedia(), end, page.getCommunicator());

        assert audio.getMedia().getTimestamps().size() == 1;
        assert audio.getMedia().getTimestamps().get(0).equals(end);
    }

    @Test
    public void testAddingHyperlink_one() throws Exception {
        //Testing that a hyperlink can be added when one already exists
        GUIAudio audio = createAudio();

        AudioModifier am = new AudioModifier();
        am.modifyMedia(audio.getMedia(), new Duration(0), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(100), page.getCommunicator());

        assert audio.getMedia().getTimestamps().size() == 2;
        assert audio.getMedia().getTimestamps().get(0).equals(new Duration(0));
        assert audio.getMedia().getTimestamps().get(1).equals(new Duration(100));
    }

    @Test
    public void testAddingHyperlink_many() throws Exception {
        //Testing that a hyperlink can be added when many already exists
        GUIAudio audio = createAudio();

        AudioModifier am = new AudioModifier();
        am.modifyMedia(audio.getMedia(), new Duration(0), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(100), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(200), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(300), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(400), page.getCommunicator());

        assert audio.getMedia().getTimestamps().size() == 5;
        assert audio.getMedia().getTimestamps().get(4).equals(new Duration(400));
    }

    @Test
    public void testRemoveHyperlink_one() throws Exception {
        //Testing that a hyperlink can be removed when only one already exists
        GUIAudio audio = createAudio();

        AudioModifier am = new AudioModifier();
        am.modifyMedia(audio.getMedia(), new Duration(0), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(0), page.getCommunicator());

        assert audio.getMedia().getTimestamps().size() == 0;
    }

    @Test
    public void testRemoveHyperlink_many() throws Exception {
        //Testing that a hyperlink can be removed when many already exists
        GUIAudio audio = createAudio();

        AudioModifier am = new AudioModifier();
        am.modifyMedia(audio.getMedia(), new Duration(0), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(100), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(200), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(300), page.getCommunicator());
        am.modifyMedia(audio.getMedia(), new Duration(400), page.getCommunicator());

        am.modifyMedia(audio.getMedia(), new Duration(0), page.getCommunicator());

        assert audio.getMedia().getTimestamps().size() == 4;
        assert audio.getMedia().getTimestamps().get(0).equals(new Duration(100));
    }



    public static Page createPage()    {
        try {
            SQLiteStorage storage = new SQLiteStorage(null);
            MediaCommunicator mediaCommunicator = new MediaCommunicator(storage);
            return new Page(mediaCommunicator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GUIAudio addMedia(String source) {
        //Loading raw audio data based on user selection
        byte[] rawData = readFile(source);

        try {
            MediaAudio audio = new MediaAudio("", 0, 0, 0, 0, rawData, new ArrayList<Duration>(),
                    "Audio"); //Temp Constructor
            page.getCommunicator().updateMedia(audio);
            GUIAudio audioUI = new GUIAudio(audio);
            return audioUI;
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
