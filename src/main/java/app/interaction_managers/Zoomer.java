package app.interaction_managers;

import app.interaction_managers.InteractionManager;

public class Zoomer implements InteractionManager {

    private final app.media.Page TOZOOM;

    public Zoomer(app.media.Page page) {
        this.TOZOOM = page;
    }

    @Override
    public void interact(javafx.scene.Node userInput) {
        double currentSize = this.TOZOOM.getViewSize();
        double targetZoom = userInput.getScaleX();
        if (targetZoom == 0.1 || targetZoom == -0.1) {
            double targetPercentage = currentSize + targetZoom;
            this.zoom(targetPercentage);
        } else {
            this.zoom(targetZoom);
        }
//        // userInput represents either pressing the zoom in (+1) or zoom out (-1) button
//        if (Integer.parseInt(userInput.toString()) == 1 || Integer.parseInt(userInput.toString()) == -1) {
//            int inOrOut = Integer.parseInt(userInput.toString());
//            double targetPercentage = currentSize + inOrOut * .1;
//            this.zoom(targetPercentage);
//        } else {
//            // userInput represents a percentage selection from the drop down menu
//            double percentage = Double.parseDouble(userInput.toString());
//            this.zoom(percentage/100);
//        }
    }

    public void zoom(double zoomFactor){
        this.TOZOOM.setViewSize(zoomFactor);
        zoomerController ZoomerController =
    }
    // TODO probably have to do something in app.media.Page or PageGUI with zoomFactor, rn only changing backend
}
