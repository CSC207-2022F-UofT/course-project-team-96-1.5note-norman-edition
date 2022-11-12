import javafx.util.Duration;

public class ToolBarController {
    private InteractionManager interactionTool;
    private MediaManager mediaTool;
    private Drawable drawTool;



    /*
    A lot of these reference undefined methods and have unclear jobs. None make sense to return anything right now
    though maybe later they will return booleans to signify if they completed their job or failed.
     */

    public void insertText() {
        mediaTool = new TextModifier();
        mediaTool.addMedia();
    }

    public void insertImage() {
        mediaTool = new ImageModifier();
        mediaTool.addMedia();
    }

    public void insertAudio() {
        mediaTool = new AudioModifier();
        mediaTool.addMedia();
    }

    public void createTimeStamp(MediaAudio audio, Duration timestamp)   {
        AudioModifier audioTool = new AudioModifier();
        audioTool.addTimeStamp(timestamp);
        audioTool.addMedia(audio);
        audioTool.modifyMedia();
    }


    public void drawShape() {
        drawTool = new ShapeCreator();
        drawTool.useTwoPositions(new double[]{}, new double[]{});
    }

    public void select() {
        drawTool = new SelectionTool();
        drawTool.useTwoPositions(new double[]{}, new double[]{});
    }

    public void tag() {
        interactionTool = new Tagger();
        interactionTool.interact();
    }
}
