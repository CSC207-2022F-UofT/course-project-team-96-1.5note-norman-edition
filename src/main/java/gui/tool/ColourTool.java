package gui.tool;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.beans.property.*;
import javafx.geometry.*;


/**
 * Provides a GUI for choosing a colour. The selected colour can be used by
 * other tools.
 */
public class ColourTool implements Tool {

    private static final Color DEFAULT_COLOUR = Color.BLACK;

    private final ObjectProperty<Color> colour;
    private final ColourSettings settings;
    private final Circle colourIndicator;

    public ColourTool() {
        colour = new SimpleObjectProperty<>();
        settings = new ColourSettings(colour);

        // "indicator" circle which changes colour to match the currently
        // selected colour. This way, the current colour can be seen without
        // having to select the colour tool.
        colourIndicator = new Circle(7);
        colourIndicator.fillProperty().bind(colour);
        colourIndicator.setStrokeWidth(1);
        colour.addListener(o -> {
            Color c = colour.getValue();
            if (c.getBrightness() > 0.8) {
                colourIndicator.setStroke(c.darker());
            } else {
                colourIndicator.setStroke(c.brighter());
            }
        });

        colour.setValue(DEFAULT_COLOUR);
    }

    // Implementation of Tool interface:

    @Override
    public String getName() {
        return "Colour";
    }

    @Override
    public Node getGraphic() {
        return colourIndicator;
    }

    @Override
    public FlowPane getSettingsGUI() {
        return settings;
    }

    /**
     * Return an ObjectProperty wrapping the currently selected colour. This
     * allows users of this class to listen for changes to the currently
     * selected colour.
     */
    public ObjectProperty<Color> colourProperty() {
        return colour;
    }
}


// Settings GUI for the colour tool
class ColourSettings extends FlowPane {

    private static final int PADDING = 5;

    private final Slider hueSlider;
    private final Slider saturationSlider;
    private final Slider valueSlider;
    private final Slider opacitySlider;
    private final ObjectProperty<Color> colour;

    private final ColourHistory history;

    public ColourSettings(ObjectProperty<Color> colour) {
        super(PADDING, PADDING * 3);
        this.colour = colour;

        history = new ColourHistory(colour);

        ColorPicker colourPicker = new ColorPicker();
        colourPicker.valueProperty().bindBidirectional(colour);

        hueSlider = new Slider(0, 360, 0);
        setHueSliderBackground();
        saturationSlider = new Slider(0, 1, 0);
        saturationSlider.setBlockIncrement(0.1);
        valueSlider = new Slider(0, 1, 0);
        valueSlider.setBlockIncrement(0.1);
        opacitySlider = new Slider(0, 1, 0);
        opacitySlider.setBlockIncrement(0.1);

        Slider[] sliders = new Slider[] {
            hueSlider,
            saturationSlider,
            valueSlider,
            opacitySlider
        };

        for (Slider slider: sliders) {
            slider.valueProperty().addListener(o -> setColourFromSliders());
            slider.setOnMouseReleased(e -> updateHistory(false));
            slider.setOnKeyReleased(e -> updateHistory(false));
        }

        colour.addListener(o -> setSlidersFromColour());
        colour.addListener(o -> updateHistory(true));

        HBox colourPickerRow = new HBox(new Label("Colour Picker:"), colourPicker);
        HBox hueRow = new HBox(new Label("Hue:"), hueSlider);
        HBox saturationRow = new HBox(new Label("Saturation:"), saturationSlider);
        HBox valueRow = new HBox(new Label("Value:"), valueSlider);
        HBox opacityRow = new HBox(new Label("Opacity:"), opacitySlider);

        HBox[] rows = new HBox[] {
            colourPickerRow, hueRow, saturationRow, valueRow, opacityRow
        };

        for (HBox row: rows) {
            ((Label) row.getChildren().get(0)).setMinWidth(70);
            HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);
            row.setSpacing(PADDING);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPrefWidth(270);
        }

        VBox historyBox = new VBox(PADDING, new Label("History:"), history);
        historyBox.setPrefWidth(270);

        getChildren().addAll(new VBox(PADDING, rows), historyBox);

        setRowValignment(VPos.TOP);
    }

    private boolean isSliderFocused() {
        return
            hueSlider.isFocused()
            || saturationSlider.isFocused()
            || valueSlider.isFocused()
            || opacitySlider.isFocused();
    }

    private void setColourFromSliders() {
        Color sliderColour = Color.hsb(
                hueSlider.getValue(), saturationSlider.getValue(),
                valueSlider.getValue(), opacitySlider.getValue());

        if (isSliderFocused()) {
            colour.setValue(sliderColour);
        }
    }

    private static String cssColourString(Color colour) {
        return colour.toString().replaceFirst("0x", "#");
    }

    // Set the hue slider's background to a linear gradient of all hues
    private void setHueSliderBackground() {
        final double HUE_INCREMENT = 0.1;

        StringBuilder builder = new StringBuilder();
        builder.append("-fx-background-color: linear-gradient(to right");

        for (double hue = 0.0; hue <= 1.0; hue += HUE_INCREMENT) {
            builder.append(',');
            builder.append(cssColourString(Color.hsb(360 * hue, 1, 1)));
        }

        builder.append(");");

        hueSlider.setStyle(builder.toString());
    }

    // Set the given slider's background to a linear gradient from `start` to
    // `end`
    private void setSliderBackground(Slider s, Color start, Color end) {
        s.setStyle(
                "-fx-background-color: linear-gradient(to right,"
                + cssColourString(start) + "," + cssColourString(end)
                + ");");
    }

    private void setSlidersFromColour() {
        Color c = colour.getValue();

        if (!isSliderFocused()) {
            hueSlider.setValue(c.getHue());
            saturationSlider.setValue(c.getSaturation());
            valueSlider.setValue(c.getBrightness());
            opacitySlider.setValue(c.getOpacity());
        }

        double hue = hueSlider.getValue();
        double saturation = c.getSaturation();
        double value = c.getBrightness();

        setSliderBackground(saturationSlider,
                Color.hsb(hue, 0, value), Color.hsb(hue, 1, value));
        setSliderBackground(valueSlider,
                Color.hsb(hue, saturation, 0), Color.hsb(hue, saturation, 1));
        setSliderBackground(opacitySlider,
                Color.hsb(hue, saturation, value, 0), Color.hsb(hue, saturation, value, 1));
    }

    private void updateHistory(boolean onlyIfNotFocused) {
        // If we update the colour on every change, even when the sliders are
        // being used, then the history will quickly fill up with very similar
        // colours.
        //
        // To fix this, we check that the sliders are not pressed before
        // updating the history. To make sure the history still gets updated
        // by the sliders, we call this method when they receive a "mouse
        // released" event, i.e. when a colour has been chosen decisively.
        if (!onlyIfNotFocused || !isSliderFocused()) {
            history.update(colour.getValue());
        }
    }
}

// Display a coloured rectangles for each used colour sorted by recency.
// Allow the user to click on one of these rectangles to select its colour.
class ColourHistory extends FlowPane {

    private static final int PADDING = 5;
    private static final int MAX_HISTORY = 60;

    private final ObjectProperty<Color> colour;

    // GUI compontent for individual entries in the colour history.
    private static class Entry extends Rectangle {

        private static final int SIZE = 20;

        private final Color colour;

        public Entry(Color colour) {
            super(SIZE, SIZE, colour);
            this.colour = colour;
            setStroke(colour.darker());
        }

        public Color getColour() {
            return colour;
        }
    }

    public ColourHistory(ObjectProperty<Color> colour) {
        super(PADDING, PADDING);
        this.colour = colour;
        setAlignment(Pos.TOP_CENTER);
    }

    // Add a new colour to the history. If the colour is already in the history,
    // it is removed to avoid duplicates. If the history length exceeds the
    // maximum, the last colour is removed to make room for the new one.
    public void update(Color colour) {
        removeAll(colour);

        Entry entry = new Entry(colour);
        getChildren().add(0, entry);

        entry.setOnMouseClicked(e -> this.colour.setValue(entry.getColour()));

        if (getChildren().size() > MAX_HISTORY) {
            getChildren().remove(getChildren().size() - 1);
        }
    }

    // Remove all instances of the given colour in the history
    private void removeAll(Color colour) {
        getChildren().removeIf(child -> {
            Entry entry = (Entry) child;
            return entry.getColour().equals(colour);
        });
    }
}
