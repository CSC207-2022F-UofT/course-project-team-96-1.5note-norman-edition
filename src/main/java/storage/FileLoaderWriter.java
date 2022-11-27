package storage;
import gui.error_window.ErrorWindow;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

public class FileLoaderWriter implements Storage{
    // TODO: Implement a file cache, which stores recently opened files to prevent consecutive reopening
    private int cacheSize = 10; // Size of the internal file cache

    private byte[][] fileCache = new byte[cacheSize][]; // Internal file cache

    public byte[] readFile(String[] extensions, String description) throws Exception {
        FileChooser fileManager = new FileChooser();
        fileManager.setTitle("Choose a File:");
        for (String extension : extensions) {
            fileManager.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));
        }

        File chosenFile = fileManager.showOpenDialog(null);
        if (chosenFile == null) {
            return null;
        }   else {
            return Files.readAllBytes(chosenFile.toPath());
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
