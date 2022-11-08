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

        Media m1 = new Media("foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = s.selectMedia(m1.getName());
        assertEquals(m1.getName(), m2.getName());
    }

    @Test
    public void testSQLiteInFile() throws Exception {
        // Test saving a Media object to a file-backed SQLite db and restoring it.

        File dbFile = File.createTempFile("test", ".sqlite");
        assertTrue(dbFile.exists());

        SQLiteStorage s = new SQLiteStorage(dbFile);

        Media m1 = new Media("bar", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = s.selectMedia(m1.getName());
        assertEquals(m1.getName(), m2.getName());
    }

    @Test
    public void testSQLiteDelete() throws Exception {
        // Test that Media can be properly deleted from the db

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media("foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = s.selectMedia(m1.getName());
        assertNotNull(m2);

        s.deleteMedia(m1.getName());

        Media m3 = s.selectMedia(m1.getName());
        assertNull(m3);
    }

    @Test
    public void testSQLiteReplace() throws Exception {
        // Test that Media gets overwritten properly

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media("foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = new Media("foo", 1, 0, 0, 0);
        s.insertMedia(m2);

        Media m3 = s.selectMedia(m1.getName());
        assertEquals(m3.getX(), m2.getX(), 0.00001);
    }

    @Test
    public void testSQLiteSelectAll() throws Exception {
        // Test that all stored media names are properly reported

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media("foo", 0, 0, 0, 0);
        s.insertMedia(m1);

        Media m2 = new Media("bar", 0, 0, 0, 0);
        s.insertMedia(m2);

        Set<String> names = s.selectAllMediaNames();

        assertTrue(names.contains(m1.getName()));
        assertTrue(names.contains(m2.getName()));
    }

    @Test
    public void testSQLiteSelectWithin() throws Exception {
        // Test selecting media within a rectangular region

        SQLiteStorage s = new SQLiteStorage(null);

        Media m1 = new Media("foo", 0, 0, 2, 2);
        s.insertMedia(m1);

        Media m2 = new Media("bar", 10, 10, 2, 2);
        s.insertMedia(m2);

        Set<String> names1 = s.selectNamesWithin(0, 0, 5, 5);
        Set<String> names2 = s.selectNamesWithin(9, 9, 5, 5);

        assertTrue(names1.contains(m1.getName()));
        assertFalse(names1.contains(m2.getName()));

        assertTrue(names2.contains(m2.getName()));
        assertFalse(names2.contains(m1.getName()));
    }
}
