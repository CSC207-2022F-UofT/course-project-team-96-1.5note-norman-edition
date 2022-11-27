import app.media.MediaAudio;
import app.media_managers.AudioModifier;
import gui.media.GUIAudio;
import gui.media.GUIHyperlink;
import gui.page.Page;
import gui.view_controllers.MediaPlayerController;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAudioHyperlinks {
    /**Tests systems involved with creating clickable timestamps
     * Processes do involve classes previously tested, but test suite is seperate for the sake of seperation
     */

    private Duration TimeStamp;
    private MediaAudio audio;
    private Page page;
    private static Boolean init = false;
    private static TestAudioModifier tam = new TestAudioModifier();
    private static GUIAudio audioGUI;
    private static MediaPlayerController controller;

    @BeforeClass
    public static void initJfxRuntime() throws Exception {
        Platform.startup(() -> {});
        tam.createPage();
        audioGUI = tam.addMedia("src\\test\\java\\test_files\\1.17 Axe to Grind.mp3");
        audioGUI.getAudioPlayer().setMute(true); //Preventing audio jumpscares

        //Because the setOnReady call gets skipped, these need to be manually initialized

        audioGUI.createInterface();
        controller = new MediaPlayerController(audioGUI, audioGUI.getAudioPlayer().getTotalDuration());
        audioGUI.setController(controller);
        controller.getAssociatedModel().setTotalDuration(new Duration(194324.897959));

        //This transitions MediaPlayer state from READY to PAUSED, which can cause issues exclusively when testing
        //because tests happen way too quickly for things to process
        controller.firePlayButton("Play");
        Thread.sleep(2000);
        controller.firePlayButton("Pause");
        audioGUI.getAudioPlayer().seek(new Duration(0));
    }

    public void addTimestamp(Duration duration) throws Exception {
        AudioModifier am = new AudioModifier();
        am.setAudio(audioGUI.getMedia());
        am.addTimeStamp(duration);
        am.setCommunicator(tam.page.getCommunicator());

        am.modifyMedia();
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
    public void testAddTimestampEmpty() throws Exception {
        //Basic test that timestamps can be added to a MediaAudio with no timestamps
        clearTimestamps();
        addTimestamp(new Duration(0));

        assertTrue(audioGUI.getMedia().getTimestamps().contains(new Duration(0)));
        assertEquals("00:00:00",
                ((GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0)).getHyperlink().getText()) ;

    }

    @Test
    public void testAddTimestampOne() throws Exception {
        //Test that timestamps can be added to a MediaAudio with 1 timestamp
        setUpOne();
        addTimestamp(new Duration(1000));

        assertTrue(audioGUI.getMedia().getTimestamps().get(1).equals(new Duration(1000)));
        assertEquals("00:00:01",
                ((GUIHyperlink) audioGUI.getTimestamps().getChildren().get(1)).getHyperlink().getText()) ;
    }

    @Test
    public void testAddTimestampMultiple() throws Exception {
        //Test that timestamps can be added to a MediaAudio with multiple timestamps
        setUpMultiple();

        addTimestamp(new Duration(5000));
        assertTrue(audioGUI.getMedia().getTimestamps().get(4).equals(new Duration(5000)));
        assertEquals("00:00:05",
                ((GUIHyperlink) audioGUI.getTimestamps().getChildren().get(4)).getHyperlink().getText()) ;
    }

    @Test
    public void testRemoveTimestampOne() throws Exception {
        //Basic test that timestamp can be removed when there is only one
        setUpOne();

        addTimestamp(new Duration(0));
        assertEquals(0, audioGUI.getMedia().getTimestamps().size());
        assertEquals(0, audioGUI.getTimestamps().getChildren().size());
    }

    @Test
    public void testRemoveTimestampMultiple_start() throws Exception {
        //Basic test that timestamps at the start can be removed when there are multiple
        setUpMultiple();

        addTimestamp(new Duration(0));
        assertEquals(3, audioGUI.getMedia().getTimestamps().size());
        assertEquals(3, audioGUI.getTimestamps().getChildren().size());

    }

    @Test
    public void testRemoveTimestampMultiple_middle() throws Exception {
        //Basic test that timestamps at the middle can be removed when there are multiple
        setUpMultiple();

        addTimestamp(new Duration(2000));
        assertEquals(3, audioGUI.getMedia().getTimestamps().size());
        assertEquals(3, audioGUI.getTimestamps().getChildren().size());

    }

    @Test
    public void testRemoveTimestampMultiple_end() throws Exception {
        //Basic test that timestamps at the end can be removed when there are multiple
        setUpMultiple();

        addTimestamp(new Duration(3000));
        assertEquals(3, audioGUI.getMedia().getTimestamps().size());
        assertEquals(3, audioGUI.getTimestamps().getChildren().size());
    }

    @Test
    public void testAddTimestamp_largeDuration() throws Exception {
        clearTimestamps();
        addTimestamp(new Duration(100000000));
        assertTrue(audioGUI.getMedia().getTimestamps().get(0).equals(new Duration(100000000)));
        assertEquals("27:46:40",
                ((GUIHyperlink) audioGUI.getTimestamps().getChildren().get(0)).getHyperlink().getText()) ;
    }

    //Testing functionality of timestamps
    //It is assumed that timestamps will be for valid points in the associated audio

    @Test
    public void testClickTimestamp_startClip() throws Exception {
        //Test that a timestamp linked to the start of the player plays said part
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
}
