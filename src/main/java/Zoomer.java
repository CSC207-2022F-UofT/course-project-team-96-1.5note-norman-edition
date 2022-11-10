public class Zoomer implements InteractionManager{

    private Page toZoom;
    private double target;

    // 1. user types in a percentage
    // 2. user presses buttons increase by 10%
    // i have 1, how to distinguish between the 2? could maybe pass a string that is either % or # of button clicks,
    // where pos is zoom in and neg is zoom out
    // 3, find a way to take in the area
    @Override
    public void interact(javafx.scene.Node userInput) {
        double currentSize = this.toZoom.getViewSize();
        //1.
        double percentage = Double.parseDouble(userInput.toString()); //TODO don't know what format input will be in
        // yet... (assuming percentage is in 50%, 75%, etc. format
        this.target = percentage /100 * (currentSize);
        this.zoom(target);

        /*
        2.
        double change = Double.parseDouble(userInput.toString()) * 0.1;
        this.target = currentSize + (change * currentSize);
        this.zoom(target)
        */
    }

    public void zoom(double viewSize){
        this.toZoom.setViewSize(viewSize);
    }
    // TODO probably have to do something in Page or PageGUI with viewSize
}
