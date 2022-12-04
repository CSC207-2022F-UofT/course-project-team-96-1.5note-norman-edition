package gui.tool;

import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.beans.property.*;

import gui.ResourceLoader;
import gui.page.PageEventHandler.*;
import gui.page.Page;
import gui.media.GUIPenStroke;


/**
 * Allows drawing pen strokes on the page.
 */
public class PenTool implements Tool {

    private HandlerMethod[] handlers;
    private Page page;
    private PenSettings settings;
    private ObjectProperty<Color> colour;

    private GUIPenStroke currentStroke;

    public PenTool(ObjectProperty<Color> colour) {
        HandlerMethod[] handlers = {
            new HandlerMethod<>(MouseEvent.MOUSE_PRESSED, this::startStroke),
            new HandlerMethod<>(MouseEvent.MOUSE_RELEASED, this::endStroke),
            new HandlerMethod<>(MouseEvent.MOUSE_DRAGGED, this::updateStroke)
        };
        this.handlers = handlers;

        this.colour = colour;
        settings = new PenSettings();
    }

    @Override
    public String getName() {
        return "Pen";
    }

    @Override
    public Node getGraphic() {
        return new Label(
                getName(), ResourceLoader.loadSVGicon("icons/pen.svg", 15, 15));
    }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public void disabledFor(Page page) {
        finishStroke();
        this.page = null;
    }

    @Override
    public FlowPane getSettingsGUI() {
        return settings;
    }

    private void startStroke(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            currentStroke = new GUIPenStroke(
                    page.getMouseCoords(e), settings.getThickness(), colour.getValue());
            page.addMedia(currentStroke);
        }
    }

    private void updateStroke(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            if (currentStroke != null) {
                currentStroke.update(page.getMouseCoords(e), settings.getThickness(), e.isShiftDown());
            }
        }
    }

    private void endStroke(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            finishStroke();
        }
    }

    private void finishStroke() {
        if (currentStroke != null) {
            currentStroke.end();
            page.updateMedia(currentStroke);
            currentStroke = null;
        }
    }
}

class PenSettings extends FlowPane {

    private static double MIN_THICKNESS = 1;
    private static double MAX_THICKNESS = 20;
    private static double DEFAULT_THICKNESS = 3;

    private static int PADDING = 5;

    private Slider thicknessSlider;
    private Spinner<Double> thicknessSpinner;
    private ObjectProperty<Double> thicknessProperty;

    public PenSettings() {
        thicknessSlider = new Slider(MIN_THICKNESS, MAX_THICKNESS, DEFAULT_THICKNESS);
        thicknessSpinner = new Spinner<>(MIN_THICKNESS, MAX_THICKNESS, DEFAULT_THICKNESS);
        thicknessSpinner.setPrefWidth(75);
        thicknessSpinner.setEditable(true);

        thicknessProperty = thicknessSlider.valueProperty().asObject();

        SpinnerValueFactory.DoubleSpinnerValueFactory spinnerFactory =
            (SpinnerValueFactory.DoubleSpinnerValueFactory) thicknessSpinner.getValueFactory();

        spinnerFactory.valueProperty().bindBidirectional(thicknessProperty);
        spinnerFactory.setAmountToStepBy(0.5);

        HBox thicknessSettings = new HBox(
                PADDING, new Label("Thickness:"), thicknessSpinner, thicknessSlider);
        thicknessSettings.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(thicknessSlider, Priority.ALWAYS);

        getChildren().addAll(thicknessSettings);
    }

    public double getThickness() {
        return thicknessProperty.getValue();
    }
}
