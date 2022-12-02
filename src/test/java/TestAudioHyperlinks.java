import app.MediaCommunicator;
import app.media.MediaAudio;
import app.media_managers.AudioModifier;
import gui.media.GUIAudio;
import gui.media.GUIHyperlink;
import gui.page.Page;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.junit.BeforeClass;
import org.junit.Test;
import storage.SQLiteStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestAudioHyperlinks {
    /**Tests systems involved with interacting with Timestamps
     * This assumes that removal / creation of timestamps works as intended
     */
    private static Page page;

    private GUIAudio audioGUI;

    @BeforeClass
    public static void initJfxRuntime() throws Exception {
        Platform.startup(() -> {});
        page = createPage();
    }

    public void createAudio() throws Exception {
        GUIAudio audioGUI = addMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.mp3");
        audioGUI.getAudioPlayer().setMute(true); //Preventing audio jumpscares

        page.getCommunicator().updateMedia(audioGUI.getMedia());

        //Because the setOnReady call gets skipped, these need to be manually initialized

        audioGUI.configureUI();
        audioGUI.createInterface();
        this.audioGUI = audioGUI;
    }

    public void addTimestamp(Duration duration) throws Exception {
        AudioModifier am = new AudioModifier();
        am.modifyMedia(audioGUI.getMedia(), duration, page.getCommunicator());
    }

    public void clearTimestamps()   {
        audioGUI.getMedia().getTimestamps().clear();
    }

    public void setUpOne() throws Exception {
        clearTimestamps();
        addTimestamp(new Duration(0));
    }

    public void setUpMultiple() throws Exception {
        clearTimestamps();

        addTimestamp(new Duration(0));
        addTimestamp(new Duration(1000));
        addTimestamp(new Duration(2000));
        addTimestamp(new Duration(3000));
    }

    //Testing addition/removal of timestamps at both the GUI and Core Data level

    @Test
    public void testAddTimestamp_largeDuration() throws Exception {
        createAudio();
        clearTimestamps();
        addTimestamp(new Duration(100000000));
        assertEquals(audioGUI.getMedia().getTimestamps().get(0), new Duration(100000000));
        assertEquals("27:46:40",
                ((GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0)).getHyperlink().getText()) ;
    }

    //Testing functionality of timestamps
    //It is assumed that timestamps will be for valid points in the associated audio

    @Test
    public void testClickTimestamp_startClip() throws Exception {
        //Test that a timestamp linked to the start of the player plays said part
        createAudio();
        setUpOne();
        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(0, audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_middleClip() throws Exception {
        //Test that a timestamp linked to the middle of the player plays said part
        //This test is prone to randomly failing, threading issues

       clearTimestamps();
       addTimestamp(audioGUI.getAudioPlayer().getTotalDuration().divide(2));

        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(audioGUI.getAudioPlayer().getTotalDuration().toMillis() / 2,
                audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_endClip() throws Exception {
        //Test that a timestamp linked to the end of the player plays said part (or rather, doesnt)

        clearTimestamps();
        addTimestamp(audioGUI.getAudioPlayer().getTotalDuration());

        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        hyperlink.getHyperlink().fire();
        Thread.sleep(2000);
        assertEquals( MediaPlayer.Status.PLAYING, audioGUI.getAudioPlayer().getStatus()); //This is weirdly a feature
        assertEquals(audioGUI.getAudioPlayer().getTotalDuration().toMillis(),
                audioGUI.getCurrentDuration().toMillis(), 0);
    }

    @Test
    public void testClickTimestamp_first() throws Exception {
        //Test that clicking the first of multiple timestamps works as expected
        setUpMultiple();
        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(0, audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_second() throws Exception {
        //Test that clicking the second of multiple timestamps works as expected
        setUpMultiple();
        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(1);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(1000, audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_last() throws Exception {
        //Test that clicking the last of multiple timestamps works as expected
        setUpMultiple();
        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(3);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(3000, audioGUI.getCurrentDuration().toMillis(), 2000);
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
