import app.media.MediaAudio;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.application.Platform;
import javafx.scene.control.Button;
import static org.junit.Assert.*;

import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.robot.Robot;
import javafx.util.Duration;
import org.junit.Test;


public class TestGUIAudio{
    /**
     * These tests assume that the creation of MediaPlayer will work, since those are tested in TestAudioModifer
     * Again, these specific tests would only pass on my PC just because of mp3 files I use to test.
     * Some modified methods are used as FileChooser and junit do not mesh well (or at all)
     */
    private Duration TimeStamp;
    private MediaAudio audio;
    private Page page;
    private static Boolean init = false;
    private TestAudioModifier tam = new TestAudioModifier();

    static void initJfxRuntime() {
        if(!init)   {
            Platform.startup(() -> {});
            init = true;
        }
    }

    @Test
    public void testPlayButtonPlays() throws Exception {
        //Test that when play is pressed at the start of an audio file, it plays correctly
        initJfxRuntime();
        tam.createPage();
        GUIAudio audioGUI = tam.addMedia("src\\test\\java\\1.17 Axe to Grind.mp3");
        audioGUI.createPlayButton();
        Button playButton = audioGUI.getPlayButton();

        playButton.fire();
        assertEquals("Pause", playButton.getText());
        Thread.sleep(1000);
        assertEquals(audioGUI.getAudioPlayer().getStatus(), MediaPlayer.Status.PLAYING);
        playButton.fire();
    }
}
