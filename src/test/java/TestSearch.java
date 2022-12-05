import app.controllers.SearchBarController;
import app.interaction_managers.Searcher;
import app.interaction_managers.Tagger;
import app.media.Media;
import javafx.util.Duration;
import org.junit.*;
import java.util.ArrayList;
import app.media.*;

import static org.junit.Assert.*;

public class TestSearch {
    @Test
    public void testSearchEmpty(){
        ArrayList<Media> testList = new ArrayList<>();
        Searcher searcher = new Searcher(testList);

        searcher.interact("TEST");

        assert searcher.getResults().isEmpty();

    }

    @Test
    public void testSearchOneTag(){
        ArrayList<Media> TestList = new ArrayList<>();
        ArrayList<Duration> testDuration = new ArrayList<>();
        byte[] testByte = new byte[] {};
        MediaAudio mediaAudio = new MediaAudio("name",0,0,0,0, testByte, testDuration);

        Tagger tagger = new Tagger();
        tagger.interact("TEST");
        tagger.addTag(mediaAudio);

        TestList.add(mediaAudio);

        Searcher searcher = new Searcher(TestList);
        searcher.interact("TEST");

        assertEquals(searcher.getResults().size(), 1);
        assert searcher.getResults().contains(mediaAudio);


    }

    @Test
    public void testFromMultipleObjects(){
        ArrayList<Media> TestList = new ArrayList<>();
        ArrayList<Duration> testDuration = new ArrayList<>();
        byte[] testByte = new byte[] {};
        Tagger tagger = new Tagger();

        MediaAudio mediaAudio = new MediaAudio("name",0,0,0,0, testByte, testDuration);
        tagger.interact("TEST");
        tagger.addTag(mediaAudio);

        MediaImage image = new MediaImage("", 0, 0, 0, 0, testByte);
        tagger.interact("TEST");
        tagger.addTag(image);

        MediaHyperlink hyperlink =
                new MediaHyperlink("", 1, 1, 1, 1, "content", "link");

        TestList.add(mediaAudio);
        TestList.add(image);
        TestList.add(hyperlink);

        Searcher searcher = new Searcher(TestList);
        searcher.interact("TEST");

        assertEquals(searcher.getResults().size(), 2);
        assert searcher.getResults().contains(mediaAudio) || searcher.getResults().contains(image);

    }

    @Test
    public void testSearchMediaText(){
        MediaText text = new MediaText(0, 0, "content","");
        ArrayList<Media> TestList = new ArrayList<>();

        TestList.add(text);

        Searcher searcher = new Searcher(TestList);
        searcher.interact("content");

        assertEquals(searcher.getResults().size(), 1);
        assert searcher.getResults().contains(text);
    }

    @Test
    public void testSearchBarController(){
        byte[] testByte = new byte[] {};
        ArrayList<Duration> testDuration = new ArrayList<>();
        MediaAudio audio = new MediaAudio("", 0, 0, 0, 0, testByte, testDuration);
        audio.getTags().add("TEST");
        ArrayList<Media> mediaArrayList = new ArrayList<>();
        mediaArrayList.add(audio);
        SearchBarController searchBarController = new SearchBarController(mediaArrayList, "TEST");

        assert searchBarController.results == 1;
    }

}
