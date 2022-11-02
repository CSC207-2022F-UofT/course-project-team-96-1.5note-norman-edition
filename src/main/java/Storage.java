public interface Storage {
    public byte[] readFile(String path);
    public void writeFile(String path, Byte[] Data);
}
