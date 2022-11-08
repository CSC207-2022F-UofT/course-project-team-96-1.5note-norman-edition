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
                    name TEXT PRIMARY KEY,
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
        insertMedia(media, true);
    }

    public void insertMedia(Media media, boolean updateData) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                INSERT OR REPLACE INTO media (
                    name, tags, x, y, width, height, z_index, angle)
                VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?
                )
                """);

        s.setString(1, media.getName());
        s.setString(2, String.join(",", media.getTags()));
        s.setDouble(3, media.getX());
        s.setDouble(4, media.getY());
        s.setDouble(5, media.getWidth());
        s.setDouble(6, media.getHeight());
        s.setDouble(7, media.getAngle());
        s.setInt(8, media.getZIndex());

        s.executeUpdate();

        if (updateData) {
            insertMediaDataBytes(media);
        }
    }

    private void insertMediaDataBytes(Media media) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                UPDATE media SET data = ? WHERE name = ?
                """);

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(media);

        s.setBytes(1, b.toByteArray());
        s.setString(2, media.getName());

        s.executeUpdate();
    };

    public Media selectMedia(String name) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                SELECT data FROM media WHERE name = ?
                """);
        s.setString(1, name);
        ResultSet r = s.executeQuery();

        if (r.next()) {
            ByteArrayInputStream b = new ByteArrayInputStream(r.getBytes(1));
            ObjectInputStream i = new ObjectInputStream(b);
            return (Media) i.readObject();
        } else {
            return null;
        }
    }

    public void deleteMedia(String name) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                DELETE FROM media WHERE name = ?
                """);
        s.setString(1, name);
        s.executeUpdate();
    }

    public Set<String> selectAllNames() throws Exception {
        Statement s = connection.createStatement();

        ResultSet r = s.executeQuery("""
                SELECT name FROM media
                """);

        Set<String> names = new HashSet<>();

        while (r.next()) {
            names.add(r.getString(1));
        }

        return names;
    }

    public Set<String> selectNamesWithin(
            double x, double y, double w, double h) throws Exception
    {
        PreparedStatement s = connection.prepareStatement("""
            SELECT name FROM media WHERE
            x BETWEEN ? - width AND ?
            AND y BETWEEN ? - height AND ?
            """);
        s.setDouble(1, x);
        s.setDouble(2, x + w);
        s.setDouble(3, y);
        s.setDouble(4, y + h);

        ResultSet r = s.executeQuery();

        Set<String> names = new HashSet<>();

        while (r.next()) {
            names.add(r.getString(1));
        }

        return names;
    }

    public boolean contains(String name) throws Exception {
        PreparedStatement s = connection.prepareStatement("""
                SELECT name FROM media WHERE name = ?
                """);
        s.setString(1, name);
        ResultSet r = s.executeQuery();

        return r.next();
    }
}
