public class MenuBarController {

    public byte[] loadFile(String path) {
        FileLoaderWriter.readFile(path);
    }

    public byte[] writeFile(String path, byte[] data) {
        FileLoaderWriter.writeFile(path, data);
    }

    // TODO string vs node input, have to figure out how UI stuff works when that's there
    public void zoom(String input) {
        Zoomer zoomer = new Zoomer();
        zoomer.interact(input);
    }

    public void destroy(String input) {
        Destroyer.interact(input);
    }
}
