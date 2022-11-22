import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Set;

import app.media.Media;
import storage.SQLiteStorage;


public class TestSQLiteStorage {

    @Test
    public void testSQLiteInMemory() throws Exception {
        // Test saving a Media object to an in-memory SQLite db and restoring it.

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media(1, "foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = s.selectMediaByID(m1.getID());
        assertEquals(m1.getID(), m2.getID());
    }

    @Test
    public void testSQLiteInFile() throws Exception {
        // Test saving a Media object to a file-backed SQLite db and restoring it.

        File dbFile = File.createTempFile("test", ".sqlite");
        dbFile.deleteOnExit();
        assertTrue(dbFile.exists());

        SQLiteStorage s = new SQLiteStorage(dbFile);

        Media m1 = new Media(1, "bar", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = s.selectMediaByID(m1.getID());
        assertEquals(m1.getID(), m2.getID());
    }

    @Test
    public void testSQLiteDelete() throws Exception {
        // Test that Media can be properly deleted from the db

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media(1, "foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = s.selectMediaByID(m1.getID());
        assertNotNull(m2);

        s.deleteMediaByID(m1.getID());

        Media m3 = s.selectMediaByID(m1.getID());
        assertNull(m3);
    }

    @Test
    public void testSQLiteReplace() throws Exception {
        // Test that Media gets overwritten properly

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media(1, "foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = new Media(1, "foo", 1, 0, 0, 0);
        s.insertMedia(m2);

        Media m3 = s.selectMediaByID(m1.getID());
        assertEquals(m3.getX(), m2.getX(), 0.00001);
    }

    @Test
    public void testSQLiteSelectAll() throws Exception {
        // Test that all stored media names are properly reported

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media(1, "foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = new Media(2, "bar", 0, 0, 0, 0);
        s.insertMedia(m2);

        Set<Long> names = s.selectAllIDs();

        assertTrue(names.contains(m1.getID()));
        assertTrue(names.contains(m2.getID()));
    }

    @Test
    public void testSQLiteSelectWithin() throws Exception {
        // Test selecting media within a rectangular region

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media(1, "foo", 0, 0, 2, 2);
        s.insertMedia(m1);

        Media m2 = new Media(2, "bar", 10, 10, 2, 2);
        s.insertMedia(m2);

        Set<Long> ids1 = s.selectIDsWithin(0, 0, 5, 5);
        Set<Long> ids2 = s.selectIDsWithin(9, 9, 5, 5);

        assertTrue(ids1.contains(m1.getID()));
        assertFalse(ids1.contains(m2.getID()));

        assertTrue(ids2.contains(m2.getID()));
        assertFalse(ids2.contains(m1.getID()));
    }

    @Test
    public void testSQLiteSelectBase() throws Exception {
        // Test loading base media class from db columns instead of
        // deserializing

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media(1, "foo", 0, 0, 0, 0);
        m1.getTags().add("tag 1 aaaaaa");
        m1.getTags().add("tag,,\\,,2,");
        m1.getTags().add("");
        // Little Bobby tables
        m1.getTags().add("Robert'); DROP TABLE media;--");

        s.insertMedia(m1);

        Media m2 = s.selectBaseMediaByID(m1.getID());

        assertEquals(m1.getID(), m2.getID());
        assertEquals(m1.getName(), m2.getName());
        assertEquals(m1.getX(), m2.getX(), 0.01);
        assertEquals(m1.getY(), m2.getY(), 0.01);
        assertEquals(m1.getWidth(), m2.getWidth(), 0.01);
        assertEquals(m1.getHeight(), m2.getHeight(), 0.01);
        assertEquals(m1.getAngle(), m2.getAngle(), 0.01);
        assertEquals(m1.getZIndex(), m2.getZIndex());
        assertEquals(m1.getTags(), m2.getTags());
    }
}
