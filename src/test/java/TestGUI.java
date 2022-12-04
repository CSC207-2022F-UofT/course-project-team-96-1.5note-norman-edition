import javafx.application.Platform;

import app.MediaCommunicator;
import gui.page.Page;
import storage.SQLiteStorage;


/**
 * Utility class for instantiating GUI classes for testing purposes.
 */
public final class TestGUI {

    private TestGUI() {}

    private static void initJavaFX() {
        try {
            Platform.startup(null);
        } catch (IllegalStateException e) {}
    }

    public static Page createPage() throws Exception {
        initJavaFX();

        SQLiteStorage s = new SQLiteStorage(null);
        MediaCommunicator c = new MediaCommunicator(s);
        return new Page(c);
    }
}
