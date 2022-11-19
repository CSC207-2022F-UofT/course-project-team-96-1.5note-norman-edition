package storage;
import gui.error_window.ErrorWindow;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashMap;

public class FileLoaderWriter implements Storage{
    // TODO: Implement a file cache, which stores recently opened files to prevent consecutive reopening
    private int cacheSize = 10; // Size of the internal file cache

    private byte[][] fileCache = new byte[cacheSize][]; // Internal file cache

    public HashMap<String, byte[]> readFile(String[] extensions, String description) throws Exception {
        FileChooser fileManager = new FileChooser();
        fileManager.setTitle("Choose a File:");
        for (String extension : extensions) {
            fileManager.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));
        }

        File chosenFile = fileManager.showOpenDialog(null);
        if (chosenFile == null) {
            return null;
        }   else {
            HashMap<String, byte[]> file = new HashMap<>();
            file.put(chosenFile.getName().substring(0, chosenFile.getName().length() - 4),
                    Files.readAllBytes(chosenFile.toPath()));
            return file;
        }
    }

    public URI writeFile(String name, byte[] Data) throws Exception{
        File newFile = File.createTempFile(name, ".mp3");
        FileOutputStream writer = new FileOutputStream(newFile);
        writer.write(Data);
        writer.close();
        return newFile.toURI();
    }
}
