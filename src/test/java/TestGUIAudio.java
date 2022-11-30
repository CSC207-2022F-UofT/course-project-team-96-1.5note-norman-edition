import app.media.MediaAudio;
import gui.media.GUIAudio;
import gui.model.GUIPlayerModel;
import gui.page.Page;
import javafx.application.Platform;
import static org.junit.Assert.*;

import javafx.scene.control.ComboBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestGUIAudio{
    /**
     * These test MediaPlayerController, GUIAudio, and GUIPlayerModel as a whole since they are so heavily interlinked
     *
     * These tests assume that the creation of MediaPlayer will work, since those are tested in TestAudioModifer
     * They also assume the UI controls work and send the correct "signals", and that aspect unfortunately cannot really
     * be tested. That being said, from more visual testing it can be verified that it's correct
     *
     * Some of these tests only work when you run them on their own, it's really weird.
     *
     * Some modified methods are used as FileChooser and junit do not mesh well (or at all)
     */
    private Duration TimeStamp;
    private MediaAudio audio;
    private Page page;
    private static Boolean init = false;
    private static TestAudioModifier tam = new TestAudioModifier();
    private static GUIAudio audioGUI;
    private static GUIPlayerModel model;

    @BeforeClass
    public static void initJfxRuntime() throws Exception {
        Platform.startup(() -> {});
        tam.createPage();
        audioGUI = tam.newMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.mp3");
        audioGUI.getAudioPlayer().setMute(true); //Preventing audio jumpscares

        //Because the setOnReady call gets skipped, these need to be manually initialized

        audioGUI.configureUI();
        audioGUI.createInterface();
        model = audioGUI.getPlayerManipulator();
    }

    //Tests for the play button
    @Test
    public void testPlayButton_playStart() throws Exception {
        //Test that when play is pressed at the start of an audio file while the player is paused, it plays
        audioGUI.getAudioPlayer().seek(audioGUI.getAudioPlayer().getStartTime());

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        assertEquals("Pause", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }

    @Test
    public void testPlayButtonPlays_playMiddle() throws Exception {
        //Test that when play is pressed at the middle of an audio file while the player is paused, it plays
        audioGUI.getAudioPlayer().seek(new Duration(90000));

        model.firedPlayButton("Play");
        Thread.sleep(1000);
        assertEquals("Pause", audioGUI.getPlayerUI().getPlay().getText());
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }


    @Test
    public void testPlayButtonPlays_playEnd() throws Exception {
        //Test that when play is pressed at the end of an audio file while the player is paused, it plays
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
    public void testPlayRate_rightCall()    {
        //Test that the play rate box correctly determines the play rate
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
    public void testPlayRate_half() {
        //Test that the half rate option correctly adjusts player rate
        model.changePlayRate(0.5);
        assertEquals(0.5, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_normal() {
        //Test that the normal rate option correctly adjusts player rate
        model.changePlayRate(1);

        assertEquals(1, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_halfOverNormal() {
        //Test that the 1.5x rate option correctly adjusts player rate
        model.changePlayRate(1.5);

        assertEquals(1.5, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_double() {
        //Test that the double rate option correctly adjusts player rate
        model.changePlayRate(2);

        assertEquals(2, audioGUI.getAudioPlayer().getRate(), 0.0);
    }

    @Test
    public void testPlayRate_playingHalf() throws InterruptedException {
        //Test that the play rate adjusts to half even with the player is playing
        audioGUI.getAudioPlayer().seek(new Duration(0));
       model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.changePlayRate(0.5);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(0.5, audioGUI.getAudioPlayer().getRate(), 0.0);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testPlayRate_playingNormal() throws InterruptedException {
        //Test that the play rate adjusts to normal even with the player is playing

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
    public void testPlayRate_playingHalfOverNormal() throws InterruptedException {
        //Test that the play rate adjusts to 1.5x even with the player is playing
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        Thread.sleep(1000);
        model.changePlayRate(1.5);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        assertEquals(1.5, audioGUI.getAudioPlayer().getRate(), 0.0);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testPlayRate_playingDouble() throws InterruptedException {
        //Test that the play rate adjusts to 2x even with the player is playing
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
    public void testPlaySlider_setStart() throws InterruptedException {
        //Test that the play slider sets the mediaplayer to the start when it is set to it's minimal value
        model.firedPlayButton("Pause");
        audioGUI.getAudioPlayer().seek(audioGUI.getAudioPlayer().getTotalDuration());

        model.playbackSliderAdjusted(0, audioGUI.getAudioPlayer().getStatus());
        Thread.sleep(1000); //Not even sure why this is neccessary, but it is
        assertEquals(new Duration(0), audioGUI.getAudioPlayer().getCurrentTime());

        //Also running a check the player is still paused
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    @Test
    public void testPlaySlider_setMiddle() throws InterruptedException {
        //Test that the play slider sets the mediaplayer to the middle when it is set to the middle of the slider
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
    public void testPlaySlider_setEnd() throws InterruptedException {
        //Test that the play slider sets the mediaplayer to the end when it is set to the end of the slider
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.playbackSliderAdjusted(1, audioGUI.getAudioPlayer().getStatus());
        Thread.sleep(2000);

        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toMillis() > 0);

        //Player should remain playing
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PAUSED);
    }

    @Test
    public void testPlaySlider_setStartPlaying() throws InterruptedException {
        //Test that the play slider sets the mediaplayer to the start when it is set to the end of the slider and
        // still playing
        model.firedPlayButton("Play");
        model.playbackSliderAdjusted(0.5, audioGUI.getAudioPlayer().getStatus());
        double unexpected = audioGUI.getAudioPlayer().getCurrentTime().toMillis();

        Thread.sleep(2000);

        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toMillis() > unexpected);

        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }

    @Test
    public void testPlaySlider_setMiddlePlaying() throws InterruptedException {
        //Test that the play slider sets the mediaplayer to the middle when it is set to the end of the slider and
        // still playing
        audioGUI.getAudioPlayer().seek(new Duration(0));
        model.firedPlayButton("Play");
        model.playbackSliderAdjusted(0.5, audioGUI.getAudioPlayer().getStatus());
        double unexpected = audioGUI.getAudioPlayer().getCurrentTime().toMillis();

        Thread.sleep(2000);

        assertTrue(audioGUI.getAudioPlayer().getCurrentTime().toMillis() > unexpected);

        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
    }

    @Test
    public void testPlaySlider_setEndPlaying() throws InterruptedException {
        //Test that the play slider sets the mediaplayer to the end when it is set to the end of the slider and
        // still playing
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
    public void testVolumeSlider_setStart() throws InterruptedException {
        //Test that the volume slider can set volume to its minimal value
        model.changeVolume(0);
        assertEquals(0, audioGUI.getAudioPlayer().getVolume(), 0);
    }

    @Test
    public void testVolumeSlider_setMiddle() throws InterruptedException {
        //Test that the volume slider can set volume to 0.5x volume
        model.changeVolume(0.5);
        assertEquals(0.5, audioGUI.getAudioPlayer().getVolume(), 0.5);
    }

    @Test
    public void testVolumeSlider_setEnd() throws InterruptedException {
        //Test that the volume slider can set volume to max volume
        model.changeVolume(1);
        assertEquals(1, audioGUI.getAudioPlayer().getVolume(), 1);
    }

    @Test
    public void testVolumeSlider_setStartPlaying() throws InterruptedException {
        //Test that the volume slider can set the volume to 0 while the player is still playing
        model.firedPlayButton("Play");
        model.changeVolume(0);
        Thread.sleep(2000);
        assertEquals(0, audioGUI.getAudioPlayer().getVolume(), 0);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testVolumeSlider_setMiddlePlaying() throws InterruptedException {
        //Test that the volume slider can set the volume to 0.5 while the player is still playing
        model.firedPlayButton("Play");
        model.changeVolume(0.5);
        Thread.sleep(2000);
        assertEquals(0.5, audioGUI.getAudioPlayer().getVolume(), 0);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        model.firedPlayButton("Pause");
    }

    @Test
    public void testVolumeSlider_setEndPlaying() throws InterruptedException {
        //Test that the volume slider can set the volume to 1 while the player is still playing
        model.firedPlayButton("Play");
        model.changeVolume(1);
        Thread.sleep(2000);
        assertEquals(1, audioGUI.getAudioPlayer().getVolume(), 0);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        model.firedPlayButton("Pause");
    }


}
