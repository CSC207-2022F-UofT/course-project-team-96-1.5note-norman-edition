package storage;
import javafx.stage.FileChooser;
import storage.Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileLoaderWriter implements Storage {
    // TODO: Implement a file cache, which stores recently opened files to prevent consecutive reopening
    private int cacheSize = 10; // Size of the internal file cache

    private byte[][] fileCache = new byte[cacheSize][]; // Internal file cache

    public byte[] readFile(String[] extensions){
        FileChooser fileManager = new FileChooser();
        fileManager.setTitle("Choose a File:");
        for (String extension : extensions) {
            fileManager.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension));
        }

        File chosenFile = fileManager.showOpenDialog(null);
        try {
            return Files.readAllBytes(chosenFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: temp solution
        }
    }

    public void writeFile(String path, byte[] Data){
        try {
            File newFile = new File(path);
            FileOutputStream writer = new FileOutputStream(newFile);
            writer.write(Data);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
