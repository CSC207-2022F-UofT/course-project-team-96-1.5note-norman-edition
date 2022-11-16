public class MenuBarController {

    // TODO string vs node input, have to figure out how UI stuff works when that's there
    // percentageZoom is when they select a percentage, input will contain that percentage
    public void percentageZoom(Page page, javafx.scene.Node input) {
        Zoomer zoomer = new Zoomer(page);
        zoomer.interact(input);
    }

//    //incrementZoom is when they click the button, input will be button click? whether it's pos or neg i guess. + or - 1
//    public void incrementZoom(Page page, javafx.scene.Node input) {
//        Zoomer zoomer = new Zoomer(page);
//        zoomer.interact(input);
//    }

    public void destroy(javafx.scene.Node userInput) {
        Destroyer destroyer = new Destroyer();
        destroyer.interact(userInput);
    }
}
