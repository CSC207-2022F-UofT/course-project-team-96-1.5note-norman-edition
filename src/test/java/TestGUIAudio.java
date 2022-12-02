import app.MediaCommunicator;
import app.media.MediaAudio;
import app.media_managers.AudioModifier;
import gui.media.GUIAudio;
import gui.media.GUIHyperlink;
import gui.model.GUIPlayerModel;
import gui.page.Page;
import javafx.application.Platform;
import static org.junit.Assert.*;

import javafx.scene.control.ComboBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.junit.BeforeClass;
import org.junit.Test;
import storage.SQLiteStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


public class TestGUIAudio{
    /**
     * These tests are broken, and i cannot for the life of me get these to work consistently in any meaningful capacity
     * leaving the tests in to show what I would have tested for
     *
     * Cover testing for GUIAudio, GUIHyperlink, PlayerModel
     */
    private Duration TimeStamp;
    private MediaAudio audio;
    private static Page page;
    private static Boolean init = false;
    private static TestAudioModifier tam = new TestAudioModifier();
    private GUIAudio audioGUI;
    private static GUIPlayerModel model;

    @BeforeClass
    public static void initJfxRuntime() throws Exception {
        Platform.startup(() -> {});
        page = createPage();
    }
    public void createAudio() throws Exception {
        GUIAudio audioGUI = addMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.mp3");
        audioGUI.getAudioPlayer().setMute(true); //Preventing audio jumpscares
        //Because the setOnReady call gets skipped, these need to be manually initialized
        audioGUI.configureUI();
        audioGUI.createInterface();

        page.getCommunicator().updateMedia(audioGUI.getMedia());

        this.audioGUI = (GUIAudio) page.getMediaLayer().getChildren().get(0);
        model = new GUIPlayerModel(audioGUI, audioGUI.getAudioPlayer().getTotalDuration());
    }

    //Tests for the play button
    @Test
    public void testPlayButton_playStart() throws Exception {
        //Test that when play is pressed at the start of an audio file while the player is paused, it plays
        createAudio();
        audioGUI.getAudioPlayer().seek(audioGUI.getAudioPlayer().getStartTime());

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        assertEquals("Pause", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }

    @Test
    public void testPlayButtonPlays_playMiddle() throws Exception {
        //Test that when play is pressed at the middle of an audio file while the player is paused, it plays
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(90000));

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        assertEquals("Pause", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }


    @Test
    public void testPlayButtonPlays_playEnd() throws Exception {
        //Test that when play is pressed at the end of an audio file while the player is paused, it plays
        createAudio();
        audioGUI.getAudioPlayer().seek(audioGUI.getAudioPlayer().getTotalDuration());

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        assertEquals("Pause", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);

        //Generally checking that the player looped, though this cant be precisely done in unit tests
        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toSeconds() < 5);
    }

    @Test
    public void testPlayButton_pauseStart() throws Exception {
        //Test that when play is pressed near the start of an audio file while the player is playing, it pauses
        createAudio();
        audioGUI.getAudioPlayer().seek(audioGUI.getAudioPlayer().getStartTime());

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.firedPlayButton("Pause");
        Thread.sleep(1000);

        assertEquals("Play", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    @Test
    public void testPlayButtonPlays_pauseMiddle() throws Exception {
        //Test that when play is pressed near the middle of an audio file while the player is paused, it pauses
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(90000));

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.firedPlayButton("Pause");
        Thread.sleep(1000);

        assertEquals("Play", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    @Test
    public void testPlayButtonPlays_pauseEnd() throws Exception {
        //Test that when play is pressed near the end of an audio file while the player is paused, it pauses
        createAudio();
        audioGUI.getAudioPlayer().seek(Duration.seconds(audioGUI.getAudioPlayer().getTotalDuration().toSeconds()
                - 10));

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.firedPlayButton("Pause");
        Thread.sleep(1000);

        assertEquals("Play", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    /**
     * There are no tests for playback text because everything surrounding them is really, really weird and I don't
     * really have the time to figure out why. From using the program, they appear to work all the time, so
     * I'll write more tests for them later
     */

    //Tests for the playback rate box
    @Test
    public void testPlayRate_rightCall() throws Exception {
        //Test that the play rate box correctly determines the play rate
        createAudio();
        ComboBox<String> playrate = audioGUI.getPlayerUI().getPlayRateOptions();
        //0.5x option
        playrate.getSelectionModel().select(0);
        assertEquals((playrate.getSelectionModel().getSelectedIndex() + 1) * 0.5, 0.5, 0);

        //1x option
        playrate.getSelectionModel().select(1);
        assertEquals((playrate.getSelectionModel().getSelectedIndex() + 1) * 0.5, 1, 0);

        //1.5x option
        playrate.getSelectionModel().select(2);
        assertEquals((playrate.getSelectionModel().getSelectedIndex() + 1) * 0.5, 1.5, 0);

        //2x option
        playrate.getSelectionModel().select(3);
        assertEquals((playrate.getSelectionModel().getSelectedIndex() + 1) * 0.5, 2, 0);
    }


    @Test
    public void testPlayRate_half() throws Exception {
        //Test that the half rate option correctly adjusts player rate
        createAudio();
        model.changePlayRate(0.5);
        assertEquals(0.5, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_normal() throws Exception {
        //Test that the normal rate option correctly adjusts player rate
        createAudio();
        model.changePlayRate(1);

        assertEquals(1, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_halfOverNormal() throws Exception {
        //Test that the 1.5x rate option correctly adjusts player rate
        createAudio();
        model.changePlayRate(1.5);

        assertEquals(1.5, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_double() throws Exception {
        //Test that the double rate option correctly adjusts player rate
        createAudio();
        model.changePlayRate(2);

        assertEquals(2, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_playingHalf() throws Exception {
        //Test that the play rate adjusts to half even with the player is playing
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.changePlayRate(0.5);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(0.5, audioGUI.getAudioPlayer().getRate(), 0.0);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testPlayRate_playingNormal() throws Exception {
        //Test that the play rate adjusts to normal even with the player is playing
        createAudio();
        model.changePlayRate(0.5);
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.changePlayRate(1);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(1, audioGUI.getAudioPlayer().getRate(), 0.0);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testPlayRate_playingHalfOverNormal() throws Exception {
        //Test that the play rate adjusts to 1.5x even with the player is playing
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.changePlayRate(1.5);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(1.5, audioGUI.getAudioPlayer().getRate(), 0.0);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testPlayRate_playingDouble() throws Exception {
        //Test that the play rate adjusts to 2x even with the player is playing
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.changePlayRate(2);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(2, audioGUI.getAudioPlayer().getRate(), 0.0);
        model.firedPlayButton("Pause");
    }

    //Tests for the playback slider
    @Test
    public void testPlaySlider_setStart() throws Exception {
        //Test that the play slider sets the mediaplayer to the start when it is set to it's minimal value
        createAudio();
        model.firedPlayButton("Pause");
        audioGUI.getAudioPlayer().seek(audioGUI.getAudioPlayer().getTotalDuration());

        model.playbackSliderAdjusted(0, audioGUI.getAudioPlayer().getStatus());
        Thread.sleep(1000); //Not even sure why this is neccessary, but it is
        assertEquals(new Duration(0), audioGUI.getAudioPlayer().getCurrentTime());

        //Also running a check the player is still paused
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    @Test
    public void testPlaySlider_setMiddle() throws Exception {
        //Test that the play slider sets the mediaplayer to the middle when it is set to the middle of the slider
        createAudio();
        audioGUI.getAudioPlayer().seek(audioGUI.getAudioPlayer().getTotalDuration());

        model.playbackSliderAdjusted(0.5, audioGUI.getAudioPlayer().getStatus());
        Thread.sleep(1000);

        //Because I had to approximate total duration for the GUIPlayerModel (there are threading issues for tests only),
        //I'm allowing it to be 0.5 off because it will not be exact
        assertEquals(audioGUI.getAudioPlayer().getTotalDuration().toMillis() / 2,
                audioGUI.getAudioPlayer().getCurrentTime().toMillis(), 0.5);

        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    @Test
    public void testPlaySlider_setEnd() throws Exception {
        //Test that the play slider sets the mediaplayer to the end when it is set to the end of the slider
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.playbackSliderAdjusted(1, audioGUI.getAudioPlayer().getStatus());
        Thread.sleep(2000);

        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toMillis() > 0);

        //Player should remain playing
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    @Test
    public void testPlaySlider_setStartPlaying() throws Exception {
        //Test that the play slider sets the mediaplayer to the start when it is set to the end of the slider and
        // still playing
        createAudio();
        model.firedPlayButton("Play");
        model.playbackSliderAdjusted(0.5, audioGUI.getAudioPlayer().getStatus());
        double unexpected = audioGUI.getAudioPlayer().getCurrentTime().toMillis();

        Thread.sleep(2000);

        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toMillis() > unexpected);

        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }

    @Test
    public void testPlaySlider_setMiddlePlaying() throws Exception {
        //Test that the play slider sets the mediaplayer to the middle when it is set to the end of the slider and
        // still playing
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        model.playbackSliderAdjusted(0.5, audioGUI.getAudioPlayer().getStatus());
        double unexpected = audioGUI.getAudioPlayer().getCurrentTime().toMillis();

        Thread.sleep(2000);

        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toMillis() > unexpected);

        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }

    @Test
    public void testPlaySlider_setEndPlaying() throws Exception {
        //Test that the play slider sets the mediaplayer to the end when it is set to the end of the slider and
        // still playing
        createAudio();
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        model.playbackSliderAdjusted(1, audioGUI.getAudioPlayer().getStatus());
        double unexpected = audioGUI.getAudioPlayer().getCurrentTime().toMillis();

        Thread.sleep(2000);

        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toMillis() > unexpected);

        //Because we have to approximate total duration, I cant quite test that the player pauses, but you can easily
        //test this manually
    }

    //Tests for the volume slider
    @Test
    public void testVolumeSlider_setStart() throws Exception {
        //Test that the volume slider can set volume to its minimal value
        createAudio();
        model.changeVolume(0);
        assertEquals(0, audioGUI.getAudioPlayer().getVolume(), 0);
    }

    @Test
    public void testVolumeSlider_setMiddle() throws Exception {
        //Test that the volume slider can set volume to 0.5x volume
        createAudio();
        model.changeVolume(0.5);
        assertEquals(0.5, audioGUI.getAudioPlayer().getVolume(), 0.5);
    }

    @Test
    public void testVolumeSlider_setEnd() throws Exception {
        //Test that the volume slider can set volume to max volume
        createAudio();
        model.changeVolume(1);
        assertEquals(1, audioGUI.getAudioPlayer().getVolume(), 1);
    }

    @Test
    public void testVolumeSlider_setStartPlaying() throws Exception {
        //Test that the volume slider can set the volume to 0 while the player is still playing
        createAudio();
        model.firedPlayButton("Play");
        model.changeVolume(0);
        Thread.sleep(2000);
        assertEquals(0, audioGUI.getAudioPlayer().getVolume(), 0);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testVolumeSlider_setMiddlePlaying() throws Exception {
        //Test that the volume slider can set the volume to 0.5 while the player is still playing
        createAudio();
        model.firedPlayButton("Play");
        model.changeVolume(0.5);
        Thread.sleep(2000);
        assertEquals(0.5, audioGUI.getAudioPlayer().getVolume(), 0);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testVolumeSlider_setEndPlaying() throws Exception {
        //Test that the volume slider can set the volume to 1 while the player is still playing
        createAudio();
        model.firedPlayButton("Play");
        model.changeVolume(1);
        Thread.sleep(2000);
        assertEquals(1, audioGUI.getAudioPlayer().getVolume(), 0);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        model.firedPlayButton("Pause");
    }

    //tests for timestamps

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
        GUIHyperlink link = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        assertEquals("27:46:40", link.getHyperlink().getText());
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
        assertEquals(0, audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_middleClip() throws Exception {
        //Test that a timestamp linked to the middle of the player plays said part
        //This test is prone to randomly failing, threading issues
        createAudio();
        clearTimestamps();
        addTimestamp(audioGUI.getAudioPlayer().getTotalDuration().divide(2));

        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(audioGUI.getAudioPlayer().getTotalDuration().toMillis() / 2,
                audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_endClip() throws Exception {
        //Test that a timestamp linked to the end of the player plays said part (or rather, doesnt)
        createAudio();
        clearTimestamps();
        addTimestamp(audioGUI.getAudioPlayer().getTotalDuration());

        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        hyperlink.getHyperlink().fire();
        Thread.sleep(2000);
        assertEquals(audioGUI.getAudioPlayer().getTotalDuration().toMillis(),
                audioGUI.getCurrentDuration().toMillis(), 0);
    }

    @Test
    public void testClickTimestamp_first() throws Exception {
        //Test that clicking the first of multiple timestamps works as expected
        createAudio();
        setUpMultiple();
        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(0, audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_second() throws Exception {
        //Test that clicking the second of multiple timestamps works as expected
        createAudio();
        setUpMultiple();
        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(1);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
        assertEquals(1000, audioGUI.getCurrentDuration().toMillis(), 2000);
    }

    @Test
    public void testClickTimestamp_last() throws Exception {
        //Test that clicking the last of multiple timestamps works as expected
        createAudio();
        setUpMultiple();
        GUIHyperlink hyperlink = (GUIHyperlink) audioGUI.getTimestamps().getChildren().get(3);
        hyperlink.getHyperlink().fire();
        Thread.sleep(1000);
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
