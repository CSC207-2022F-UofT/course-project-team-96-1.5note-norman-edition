package storage;

import java.sql.*;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Set;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import app.MediaStorage;
import app.media.Media;


/**
 * Implementation of MediaStorage backed by a SQLite database.
 */
public class SQLiteStorage implements MediaStorage {

    private Connection connection;
    private File file;

    /**
     * Instantiate a new SQLiteStorage backed by the given file.
     * <p>
     * If `file` is `null`, then the storage will be backed by memory instead
     * of a file.
     */
    public SQLiteStorage(File file) throws Exception {
        setFile(file);
        setupTables();
    }

    // Get a SQLite connection to the db stored in the given file. If the
    // file is null, use an in-memory database instead.
    private void setFile(File file) throws Exception {
        if (file == null) {
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        } else {
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + file.getPath());
        }

        this.file = file;
    }

    /**
     * Store the current database in the given file and switch to using that
     * file as the current database.
     */
    public void saveTo(File file) throws Exception {
        if (this.file == null || !file.getPath().equals(this.file.getPath())) {
            Statement s = connection.createStatement();
            s.executeUpdate("BACKUP TO " + file.getPath());
            setFile(file);
        }
    }

    private void setupTables() throws Exception {
        setupMediaTable();
    }

    private void setupMediaTable() throws Exception {
        Statement s = connection.createStatement();
        s.executeUpdate("""
                CREATE TABLE IF NOT EXISTS media(
                    id BIGINT PRIMARY KEY,
                    name TEXT,
                    tags TEXT,
                    x REAL, y REAL,
                    width REAL, height REAL,
                    angle REAL,
                    z_index INTEGER,
                    data BLOB
                )
                """);
    }

    /**
     * Return whether or not this DB is stored in memory rather than in a file.
     */
    public boolean isInMemory() {
        return file == null;
    }

    /**
     * Store the given media.
     */
    public void insertMedia(Media media) throws Exception {
        if (media.getID() == Media.EMPTY_ID) {
            throw new Exception("Tried to store Media with empty ID");
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        GZIPOutputStream g = new GZIPOutputStream(b);
        try (ObjectOutputStream o = new ObjectOutputStream(g)) {
            o.writeObject(media);
        } finally {
            g.finish();
            g.close();
        }

        PreparedStatement s = connection.prepareStatement("""
                INSERT OR REPLACE INTO media VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                )
                """);

        s.setLong(1, media.getID());
        s.setString(2, media.getName());
        s.setString(3, getStringFromTags(media.getTags()));
        s.setDouble(4, media.getX());
        s.setDouble(5, media.getY());
        s.setDouble(6, media.getWidth());
        s.setDouble(7, media.getHeight());
        s.setDouble(8, media.getAngle());
        s.setInt(9, media.getZindex());
        s.setBytes(10, b.toByteArray());

        s.executeUpdate();
    }

    // Write a set of tag strings into a single string such that it can be
    // losslessly restored back into a Set of String later.
    //
    // The reason for doing this is that SQLite doesn't have a "set" type, just
    // "TEXT".
    //
    // The strategy used here is to store the set as a comma-separated list and
    // then escape commas already in tags as "\,". Additionally, any
    // back-slashes already in a tag get replaced with "\\". This allows the
    // set to be restored by splitting the string over commas which are
    // preceded by an even number of back-slashes.
    private static String getStringFromTags(Set<String> tags) {
        StringBuilder builder = new StringBuilder();

        builder.append(",");

        for (String tag: tags) {
            builder.append(tag.replace("\\", "\\\\").replace(",", "\\,"));
            builder.append(",");
        }

        return builder.toString();
    }

    // Restore a set of tag strings from a string produced by the
    // `getStringFromTags` method.
    private static Set<String> getTagsFromString(String tagString) {
        Set<String> tags = new HashSet<>();

        int consecutiveBackSlashes = 0;
        int startIndex = 0;

        for (int endIndex = 0; endIndex < tagString.length(); endIndex++) {
            char c = tagString.charAt(endIndex);

            if (c == '\\') {
                consecutiveBackSlashes++;
            } else {
                // If we hit a comma, check that the number of consecutive back
                // slashes is even to make sure the comma is not escaped.
                if (c == ',' && consecutiveBackSlashes % 2 == 0) {
                    String s = tagString.substring(startIndex, endIndex);

                    // Exclude empty tag strings
                    if (!s.isEmpty()) {
                        tags.add(s.replace("\\\\", "\\"));
                    }

                    startIndex = endIndex + 1;
                }

                consecutiveBackSlashes = 0;
            }
        }

        return tags;
    }

    /**
     * Load the Media with the given unique identifier.
     */
    public Media selectMediaByID(Long id) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                SELECT data FROM media WHERE id = ?
                """);
        s.setLong(1, id);
        ResultSet r = s.executeQuery();

        if (r.next()) {
            ByteArrayInputStream b = new ByteArrayInputStream(r.getBytes(1));
            GZIPInputStream g = new GZIPInputStream(b);
            try (ObjectInputStream i = new ObjectInputStream(g)) {
                return (Media) i.readObject();
            } finally {
                g.close();
            }
        } else {
            return null;
        }
    }

    /**
     * Delete the Media with the given unqiue identifier.
     */
    public void deleteMediaByID(Long id) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                DELETE FROM media WHERE id = ?
                """);
        s.setLong(1, id);
        s.executeUpdate();
    }

    /**
     * Return all the IDs currently stored
     */
    public Set<Long> selectAllIDs() throws Exception {
        Statement s = connection.createStatement();

        ResultSet r = s.executeQuery("""
                SELECT id FROM media
                """);

        Set<Long> ids = new HashSet<>();

        while (r.next()) {
            ids.add(r.getLong(1));
        }

        return ids;
    }

    /**
     * Return the IDs of media within the given rectangular region.
     * @param x The X-coordinate of the top-left corner of the rectangular region
     * @param y The Y-coordinate of the top-left corner of the rectangular region
     * @param w The width of the rectangular region
     * @param h The height of the rectangular region
     */
    public Set<Long> selectIDsWithin(
            double x, double y, double w, double h) throws Exception
    {
        PreparedStatement s = connection.prepareStatement("""
            SELECT id FROM media WHERE
            x BETWEEN ? - width AND ? + width
            AND y BETWEEN ? - height AND ? + height
            """);
        s.setDouble(1, x);
        s.setDouble(2, x + w);
        s.setDouble(3, y);
        s.setDouble(4, y + h);

        ResultSet r = s.executeQuery();

        Set<Long> ids = new HashSet<>();

        while (r.next()) {
            ids.add(r.getLong(1));
        }

        return ids;
    }

    /**
     * Return whether or not Media with the given ID is stored.
     */
    public boolean contains(Long id) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                SELECT id FROM media WHERE id = ?
                """);
        s.setLong(1, id);
        ResultSet r = s.executeQuery();

        return r.next();
    }

    public void close() throws Exception {
        connection.close();
    }


    /*
     * Implementation of app.MediaStorage interface
     */

    @Override
    public void deleteMedia(long id) throws Exception {
        deleteMediaByID(id);
    }

    @Override
    public void saveMedia(Media media) throws Exception {
        insertMedia(media);
    }

    @Override
    public Media loadMedia(long id) throws Exception {
        return selectMediaByID(id);
    }

    @Override
    public Set<Long> getIDsWithin(
            double x, double y, double w, double h) throws Exception
    {
        return selectIDsWithin(x, y, w, h);
    }

    @Override
    public boolean isIDtaken(long id) throws Exception {
        return contains(id);
    }
}
