public class ToolBarController {

    /*
    A lot of these reference undefined methods and have unclear jobs. None make sense to return anything right now
    though maybe later they will return booleans to signify if they completed their job or failed.
     */

    public void insertText() {
        TextModifier.addMedia();
    }

    public void insertImage() {
        ImageModifier.addMedia();
    }

    public void insertAudio() {
        AudioModifier.addMedia();
    }

    public void drawShape() {
        ShapeCreator.useTwoPositions();
    }

    // TODO have to figure out how UI stuff works
    public void select(double[] first, double[] second) {
        SelectionTool selectionTool = new SelectionTool();
        selectionTool.useTwoPositions(first, second);
    }

    public void tag(String input) {
        Tagger.interact(input);
    }
}
