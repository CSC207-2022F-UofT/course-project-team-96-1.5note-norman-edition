public class FileLoaderWriter implements Storage {
    // TODO: Implement a file cache, which stores recently opened files to prevent consecutive reopening
    private int cacheSize = 10; // Size of the internal file cache
    private byte[][] fileCache = new byte[cacheSize][]; // Internal file cache

    public byte[] readFile(String path){
        return new byte[1]; // TODO: Return a proper array of bytes
    }

    public void writeFile(String path, Byte[] Data){

    }
}
