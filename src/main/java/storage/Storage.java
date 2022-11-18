package storage;

import java.net.URI;

public interface Storage {
    public byte[] readFile(String[] extensions, String description);
    public URI writeFile(String name, byte[] Data);
}
