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
            try (Statement s = connection.createStatement()) {
                s.executeUpdate("BACKUP TO " + file.toURI().toURL());
                setFile(file);
            }
        }
    }

    private void setupTables() throws Exception {
        setupMediaTable();
    }

    private void setupMediaTable() throws Exception {
        try (Statement s = connection.createStatement()) {
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

        final String query = """
            INSERT OR REPLACE INTO media VALUES (
                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
            )
            """;

        try (PreparedStatement s = connection.prepareStatement(query)) {
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

        for (String tag: tags) {
            // Escape back-slashes and commas
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

                    // replace escaped back-slashes and commas back to their
                    // original values.
                    tags.add(s.replace("\\\\", "\\").replace("\\,", ","));

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
        final String query = """
            SELECT data FROM media WHERE id = ?
            """;

        try (PreparedStatement s = connection.prepareStatement(query)) {
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
    }

    /**
     * Load the Media with the given unique identifier.
     * <p>
     * Unlike `selectMediaByID`, this method does not deserialize the object
     * and instead instantiates the base Media class from the properties
     * common to all media types. This is useful for inspecting the position,
     * etc. of a stored Media object without having to deserialize the whole
     * object (which might be costly for some potentially large Media types
     * such as audio).
     */
    public Media selectBaseMediaByID(Long id) throws Exception {
        final String query = """
            SELECT name, tags, x, y, width, height, angle, z_index
            FROM media WHERE id = ?
            """;

        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setLong(1, id);
            ResultSet r = s.executeQuery();

            if (r.next()) {
                String name = r.getString(1);
                Set<String> tags = getTagsFromString(r.getString(2));
                double x = r.getDouble(3);
                double y = r.getDouble(4);
                double width = r.getDouble(5);
                double height = r.getDouble(6);
                double angle = r.getDouble(7);
                int zIndex = r.getInt(8);

                return new Media(
                        id, name, tags, x, y, width, height, angle, zIndex);
            } else {
                return null;
            }
        }
    }

    /**
     * Delete the Media with the given unqiue identifier.
     */
    public void deleteMediaByID(Long id) throws Exception {
        final String query = """
            DELETE FROM media WHERE id = ?
            """;

        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setLong(1, id);
            s.executeUpdate();
        }
    }

    /**
     * Return all the IDs currently stored
     */
    public Set<Long> selectAllIDs() throws Exception {
        try (Statement s = connection.createStatement()) {
            ResultSet r = s.executeQuery("""
                    SELECT id FROM media
                    """);

            Set<Long> ids = new HashSet<>();

            while (r.next()) {
                ids.add(r.getLong(1));
            }

            return ids;
        }
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
        final String query = """
            SELECT id FROM media WHERE
            x BETWEEN ? - width AND ? + width
            AND y BETWEEN ? - height AND ? + height
            """;

        try (PreparedStatement s = connection.prepareStatement(query)) {
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
    }

    /**
     * Return whether or not Media with the given ID is stored.
     */
    public boolean contains(Long id) throws Exception {
        final String query = """
            SELECT id FROM media WHERE id = ?
            """;

        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setLong(1, id);
            ResultSet r = s.executeQuery();

            return r.next();
        }
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
