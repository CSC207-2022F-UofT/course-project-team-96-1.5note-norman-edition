package gui;

import gui.page.Page;
import javafx.scene.control.ScrollPane;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

public class ZoomableScrollPane extends ScrollPane implements Zoomable {
//    private Group toZoom;
    private Scale scale;
    private Page page;
    // TODO fix whwatever
    private double scaleFactor = 1.0;

    public ZoomableScrollPane(Page content) {
        page = content;
//        toZoom = new Group(contents);
        setContent(page);
        scale = new Scale(scaleFactor, scaleFactor, page.getWidth()/2, page.getHeight()/2);
        page.getTransforms().add(scale);

//        setViewportBounds(page.getVisibleBounds());

        page.setOnScroll(new ZoomHandler());
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    /** Given a factor to scale the Page, scale in x and y directions by that factor. no pivot
     *
     * @param factor the factor by which to scale toZoom, > 0
     */
    public void zoomToFactor(double factor) {
        scaleFactor = factor;

        scale.setPivotX(page.getWidth()/2);
        scale.setPivotY(page.getHeight()/2);
        scale.setX(scaleFactor);
        scale.setY(scaleFactor);
    }

    /** Scale toZoom by jumping to the next smallest/largest (depending on the value of inOrOut) double in zoomOptions
     *
     * @param inOrOut "In" to zoom in, "Out" to zoom out
     */
    public void zoomInOrOut(String inOrOut){
        double[] zoomOptions = {0.1, 0.25, 1.0/3.0, 0.5, 2.0/3.0, 0.75, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0,
                9.0, 10.0};
        double currentFactor = getScaleFactor();
        if (currentFactor == 0.1 && inOrOut.equals("Out")) {
            return;
        } else if (currentFactor == 10.0 && inOrOut.equals("In")) {
            return;
        }
        int i;
        if (inOrOut.equals("In")) {
            // once the loop ends, we have the index of a factor that is greater than the current one
            i = 0;
            while (i < zoomOptions.length && zoomOptions[i] <= currentFactor) {
                i++;
            }
            // if we zoom in we can then plug this factor right into our zoomToFactor function
        } else {
            i = zoomOptions.length - 1;
            while (i >= 0 && zoomOptions[i] >= currentFactor) {
                i--;
            }
            // once the loop ends, we have the index of a factor that is less than the current one
            // if we zoom out we can then plug this factor right into our zoomToFactor function
        }
        this.zoomToFactor(zoomOptions[i]);
    }

//    /**
//     *
//     * @param minimizeOnly
//     *            If the content fits already into the viewport, then we don't
//     *            zoom if this parameter is true.
//     */
//    public void zoomToFit(boolean minimizeOnly) {
//
//        double scaleX = getViewportBounds().getWidth() / getContent().getBoundsInLocal().getWidth();
//        double scaleY = getViewportBounds().getHeight() / getContent().getBoundsInLocal().getHeight();
//
//        // consider current scale (in content calculation)
//        scaleX *= scaleValue;
//        scaleY *= scaleValue;
//
//        // distorted zoom: we don't want it => we search the minimum scale
//        // factor and apply it
//        double scale = Math.min(scaleX, scaleY);
//
//        // check precondition
//        if (minimizeOnly) {
//
//            // check if zoom factor would be an enlargement and if so, just set
//            // it to 1
//            if (Double.compare(scale, 1) > 0) {
//                scale = 1;
//            }
//        }
//
//        // apply zoom
//        zoomTo(scale);
//
//    }

    private class ZoomHandler implements EventHandler<ScrollEvent> {

        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.isControlDown())
            {

                if (scrollEvent.getDeltaY() < 0) {
                    scaleFactor -= 0.1;
                } else {
                    scaleFactor += 0.1;
                }

                zoomToFactor(scaleFactor);

                scrollEvent.consume();
            }
        }
    }
}
