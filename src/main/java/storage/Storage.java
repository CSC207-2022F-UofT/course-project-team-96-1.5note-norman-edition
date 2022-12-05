package storage;

import java.net.URI;
import java.util.HashMap;

public interface Storage {
    public HashMap<String, byte[]> readFile(String[] extensions, String description) throws Exception;
    public URI writeFile(String name, byte[] Data, String extension) throws Exception;
}
