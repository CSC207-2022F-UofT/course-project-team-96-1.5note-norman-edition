public class MenuBarController {
    public void zoom() {
        Zoomer zoomer = new Zoomer();
        zoomer.interact();
    }

    public void destroy() {
        Destroyer destroyer = new Destroyer();
        destroyer.interact();
    }
}
