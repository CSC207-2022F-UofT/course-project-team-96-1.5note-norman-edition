import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

import javafx.geometry.Point2D;

import gui.page.Page;
import gui.page.Selection;
import gui.media.GUIMedia;
import gui.tool.app.media.Media;


public class TestSelection {

    private static Page page;

    @BeforeClass
    public static void createPage() throws Exception {
        page = TestGUI.createPage();
    }

    private static void populatePage() {
        String[] names = new String[] {
            "foobar",
            "barfoo",
            "baz",
            "fhqwhgads",
            "a somewhat longer name than the others",
            "e"
        };

        for (String name: names) {
            // Make all the media have arbitrary positions...
            // In this case, we give them positions based on their names, but
            // the only thing that matters is that they are all different so
            // that testing moving media is more likely to catch any problems.
            Media m = new Media(name, name.length(), 20 - name.length(), 0, 0);

            // same as above for other properties:

            m.setZindex(name.length());

            m.setAngle(name.length() / 2.0);


            GUIMedia<Media> guiMedia = new GUIMedia<>(m);
            page.addMedia(guiMedia);
            page.updateMedia(guiMedia);
        }
    }

    @Test
    public void testSelectionAdd() {
        populatePage();

        Selection s = new Selection(page, null);

        for (GUIMedia media: page.getAllMedia()) {
            s.addMedia(media);
        }

        assertTrue(page.getAllMedia().equals(s.getMedia()));
    }

    @Test
    public void testSelectionDelete() {
        populatePage();

        Selection s = new Selection(page, null);

        for (GUIMedia media: page.getAllMedia()) {
            s.addMedia(media);
        }

        s.delete();

        assertTrue(page.getAllMedia().isEmpty());
    }

    @Test
    public void testSelectionMove() {
        populatePage();

        HashMap<GUIMedia, Point2D> initialPositions = new HashMap<>();
        for (GUIMedia media: page.getAllMedia()) {
            initialPositions.put(
                    media,
                    new Point2D(media.getMedia().getX(), media.getMedia().getY()));
        }

        Selection s = new Selection(page, null);

        for (GUIMedia media: page.getAllMedia()) {
            s.addMedia(media);
        }

        Point2D displacement = new Point2D(10, 15);

        s.move(displacement);

        for (GUIMedia media: page.getAllMedia()) {
            Point2D pos = new Point2D(
                    media.getMedia().getX(), media.getMedia().getY());

            assertEquals(
                    displacement,
                    pos.subtract(initialPositions.get(media)));
        }
    }

    @Test
    public void testSelectionRename() {
        populatePage();

        Selection s = new Selection(page, null);

        for (GUIMedia media: page.getAllMedia()) {
            s.addMedia(media);
        }

        assertEquals(s.getName(), "");

        final String NAME = "something completely different";

        s.rename(NAME);

        for (GUIMedia media: page.getAllMedia()) {
            assertEquals(media.getName(), NAME);
        }

        assertEquals(s.getName(), NAME);
    }

    @Test
    public void testSelectionRotate() {
        populatePage();

        Selection s = new Selection(page, null);

        for (GUIMedia media: page.getAllMedia()) {
            s.addMedia(media);
        }

        final double ANGLE = 37;

        assertNotEquals(s.getAngle(), ANGLE, 0.001);

        s.rotate(ANGLE);

        for (GUIMedia media: page.getAllMedia()) {
            assertEquals(media.getMedia().getAngle(), ANGLE, 0.001);
        }

        assertEquals(s.getAngle(), ANGLE, 0.001);
    }

    @Test
    public void testSelectionSetZindex() {
        populatePage();

        Selection s = new Selection(page, null);

        for (GUIMedia media: page.getAllMedia()) {
            s.addMedia(media);
        }

        final int Z_INDEX = 43;

        assertNotEquals(s.getZindex(), Z_INDEX);

        s.setZindex(Z_INDEX);

        for (GUIMedia media: page.getAllMedia()) {
            assertEquals(media.getMedia().getZindex(), Z_INDEX);
        }

        assertEquals(s.getZindex(), Z_INDEX);
    }
}
