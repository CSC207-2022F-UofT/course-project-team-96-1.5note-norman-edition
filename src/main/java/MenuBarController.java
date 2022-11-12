public class MenuBarController {

    public byte[] loadFile(String path) {
        FileLoaderWriter fileManager = new FileLoaderWriter();
        byte[] data = fileManager.readFile(path);
        return data;
    }

    public void writeFile(String path, byte[] data) {
        FileLoaderWriter fileManager = new FileLoaderWriter();
        fileManager.writeFile(path, data);
    }

    public void zoom() {
        Zoomer zoomer = new Zoomer();
        zoomer.interact();
    }

    public void destroy(String input) {
        Destroyer destroyer = new Destroyer();
        destroyer.interact();
    }
}
