package storage;

import java.sql.*;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Set;
import java.util.HashSet;

import app.MediaStorage;
import app.media.Media;


/**
 * Implementation of MediaStorage backed by a SQLite database.
 */
public class SQLiteStorage implements MediaStorage {

    private Connection connection;
    private File file;

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

    public void insertMedia(Media media) throws Exception {
        if (media.getID() == Media.EMPTY_ID) {
            throw new Exception("Tried to store Media with empty ID");
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(media);

        PreparedStatement s = connection.prepareStatement("""
                INSERT OR REPLACE INTO media VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                )
                """);

        s.setLong(1, media.getID());
        s.setString(2, media.getName());
        s.setString(3, String.join(",", media.getTags()));
        s.setDouble(4, media.getX());
        s.setDouble(5, media.getY());
        s.setDouble(6, media.getWidth());
        s.setDouble(7, media.getHeight());
        s.setDouble(8, media.getAngle());
        s.setInt(9, media.getZIndex());
        s.setBytes(10, b.toByteArray());

        s.executeUpdate();
    }

    public Media selectMediaByID(Long id) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                SELECT data FROM media WHERE id = ?
                """);
        s.setLong(1, id);
        ResultSet r = s.executeQuery();

        if (r.next()) {
            ByteArrayInputStream b = new ByteArrayInputStream(r.getBytes(1));
            ObjectInputStream i = new ObjectInputStream(b);
            return (Media) i.readObject();
        } else {
            return null;
        }
    }

    public void deleteMediaByID(Long id) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                DELETE FROM media WHERE id = ?
                """);
        s.setLong(1, id);
        s.executeUpdate();
    }

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

    public Set<Long> selectIDsWithin(
            double x, double y, double w, double h) throws Exception
    {
        PreparedStatement s = connection.prepareStatement("""
            SELECT id FROM media WHERE
            x BETWEEN ? - width AND ?
            AND y BETWEEN ? - height AND ?
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

    public boolean contains(Long id) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                SELECT id FROM media WHERE id = ?
                """);
        s.setLong(1, id);
        ResultSet r = s.executeQuery();

        return r.next();
    }


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
