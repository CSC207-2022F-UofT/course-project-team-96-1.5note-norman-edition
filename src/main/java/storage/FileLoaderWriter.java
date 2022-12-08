package storage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashMap;

public class FileLoaderWriter implements Storage{
    // TODO: Implement a file cache, which stores recently opened files to prevent consecutive reopening
    private final int cacheSize = 10; // Size of the internal file cache

    private final byte[][] fileCache = new byte[cacheSize][]; // Internal file cache

    /**
     * Allows the user to choose media to be added to the Page
     *
     * @param extensions file extensions accepted by the file chooser
     * @param description text to be displayed in the file chooser
     * @return A map of the name of the read file to the raw bytes stored by it
     * @throws Exception when an error occurs reading the file
     */
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
            file.put(chosenFile.getName(),
                    Files.readAllBytes(chosenFile.toPath()));
            return file;
        }
    }

    /**
     * Writes a temp file for use by FileMedia
     * @param name what the temp file will be called
     * @param Data the raw data to be written
     * @param extension the extension type the written temp file will be. Must be in form '.extensionType'
     * @return a URI indentfying the temp file that was written
     * @throws Exception when writing fails for any reason
     */
    public URI writeFile(String name, byte[] Data, String extension) throws Exception{
        File newFile = File.createTempFile(name, extension);
        FileOutputStream writer = new FileOutputStream(newFile);
        writer.write(Data);
        writer.close();
        return newFile.toURI();
    }
}
