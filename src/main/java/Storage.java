public interface Storage {
    public byte[] readFile(String path);
    public void writeFile(String path, byte[] Data);
}
