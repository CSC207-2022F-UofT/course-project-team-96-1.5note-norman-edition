public class MenuBarController {

    public byte[] loadFile(String path) {
        FileLoaderWriter.readFile(path);
    }

    public byte[] writeFile(String path, byte[] data) {
        FileLoaderWriter.writeFile(path, data);
    }

    public void zoom(String input) {
        Zoomer.interact(input);
    }

    public void destroy(String input) {
        Destroyer.interact(input);
    }
}
