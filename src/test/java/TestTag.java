import app.controllers.ToolBarController;
import app.interaction_managers.Tagger;
import app.media.*;
import gui.media.GUIMedia;
import javafx.util.Duration;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class TestTag {

    @Test
    public void testInitialTags(){
        Media media = new Media(1, "", 0, 0, 0, 0);

        assertEquals(0, media.getTags().size());
    }

    @Test
    public void testAddingTags(){
        Media media = new Media(1, "", 0, 0, 0, 0);

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(media);
        assertTrue(media.getTags().contains("TEST"));
    }

    @Test
    public void testTagText(){
        MediaText text = new MediaText(0, 0, "","content");

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(text);

        assertTrue(text.getTags().contains("TEST"));
    }

    @Test
    public void testTagAudio(){
        byte[] testByte = new byte[] {};
        ArrayList<Duration> testDuration = new ArrayList<>();

        MediaAudio audio = new MediaAudio("", 0, 0, 0, 0, testByte, testDuration);

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(audio);

        assertTrue(audio.getTags().contains("TEST"));
    }

    @Test
    public void testTagImage(){
        byte[] testByte = new byte[] {};

        MediaImage image = new MediaImage("", 0, 0, 0, 0, testByte);

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(image);

        assertTrue(image.getTags().contains("TEST"));
    }

    @Test
    public void testTagHyperlink(){
        MediaHyperlink hyperlink =
                new MediaHyperlink("", 1, 1, 1, 1, "content", "link");

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(hyperlink);

        assertTrue(hyperlink.getTags().contains("TEST"));
    }

    @Test
    public void testTagPenStroke(){
        PenStroke penStroke = new PenStroke(1, 1, 1, "colour");

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(penStroke);

        assertTrue(penStroke.getTags().contains("TEST"));
    }

    @Test
    public void testAddingMoreThanOne(){
        Media media = new Media(1, "", 0, 0, 0, 0);

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(media);

        tagger.interact("TEST2");
        tagger.addTag(media);

        assertTrue(media.getTags().contains("TEST") && media.getTags().contains("TEST2"));
        assertEquals(2, media.getTags().size());
    }

    @Test
    public void testToolBarControllerTag(){
        byte[] testByte = new byte[] {};
        ArrayList<Duration> testDuration = new ArrayList<>();
        MediaAudio audio = new MediaAudio("", 0, 0, 0, 0, testByte, testDuration);
        GUIMedia<?> guiMedia = new GUIMedia<>(audio);

        ToolBarController toolBarController = new ToolBarController();
        toolBarController.tag("TEST", guiMedia);

        assert audio.getTags().contains("TEST");

    }

    @Test
    public void testRemoveTag(){
        Media media = new Media(1, "", 0, 0, 0, 0);

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(media);
        assertTrue(media.getTags().contains("TEST"));

        tagger.removeTag(media);
        assertTrue(media.getTags().isEmpty());
    }

}
