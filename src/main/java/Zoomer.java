public class Zoomer implements InteractionManager{

    private final Page TOZOOM;

    public Zoomer(Page page) {
        this.TOZOOM = page;
    }

    //TODO don't know what format userInput will be in yet... (assuming percentage is in 50%, 75%, etc. format)
    @Override
    public void interact(javafx.scene.Node userInput) {
        double currentSize = this.TOZOOM.getViewSize();
        // userInput represents either pressing the zoom in (+1) or zoom out (-1) button
        if (Integer.parseInt(userInput.toString()) == 1 || Integer.parseInt(userInput.toString()) == -1) {
            int inOrOut = Integer.parseInt(userInput.toString());
            double targetPercentage = currentSize + inOrOut * .1;
            this.zoom(targetPercentage);
        } else {
            // userInput represents a percentage selection from the drop down menu
            double percentage = Double.parseDouble(userInput.toString());
            this.zoom(percentage/100);
        }
    }

    public void zoom(double viewSize){
        this.TOZOOM.setViewSize(viewSize);
    }
    // TODO probably have to do something in Page or PageGUI with viewSize, rn only changing backend
}
