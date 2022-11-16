package gui.tool;

import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.beans.property.*;
import javafx.geometry.*;


public class ColourTool implements Tool {

    private static final Color DEFAULT_COLOUR = Color.BLACK;

    private ObjectProperty<Color> colour;
    private ColourSettings settings;
    private Circle colourIndicator;

    public ColourTool() {
        colour = new SimpleObjectProperty<>();
        settings = new ColourSettings(colour);

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

    public Color getColour() {
        return colour.getValue();
    }

    public ObjectProperty<Color> colourProperty() {
        return colour;
    }
}


class ColourSettings extends FlowPane {

    private static int PADDING = 5;

    private ColorPicker colourPicker;
    private Slider hueSlider;
    private Slider saturationSlider;
    private Slider valueSlider;
    private Slider opacitySlider;
    private ObjectProperty<Color> colour;

    private ColourHistory history;

    public ColourSettings(ObjectProperty<Color> colour) {
        super(PADDING, PADDING * 3);
        this.colour = colour;

        history = new ColourHistory(colour);

        colourPicker = new ColorPicker();
        colourPicker.valueProperty().bindBidirectional(colour);

        hueSlider = new Slider(0, 360, 0);
        setHueSliderBackground();
        saturationSlider = new Slider(0, 1, 0);
        valueSlider = new Slider(0, 1, 0);
        opacitySlider = new Slider(0, 1, 0);

        hueSlider.valueProperty().addListener(o -> setColourFromSliders());
        saturationSlider.valueProperty().addListener(o -> setColourFromSliders());
        valueSlider.valueProperty().addListener(o -> setColourFromSliders());
        opacitySlider.valueProperty().addListener(o -> setColourFromSliders());
        colour.addListener(o -> setSlidersFromColour());

        hueSlider.setOnMouseReleased(e -> updateHistory());
        saturationSlider.setOnMouseReleased(e -> updateHistory());
        valueSlider.setOnMouseReleased(e -> updateHistory());
        opacitySlider.setOnMouseReleased(e -> updateHistory());
        colour.addListener(o -> updateHistory());

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

    private boolean isSliderPressed() {
        return
            hueSlider.isPressed()
            || saturationSlider.isPressed()
            || valueSlider.isPressed()
            || opacitySlider.isPressed();
    }

    private void setColourFromSliders() {
        Color sliderColour = Color.hsb(
                hueSlider.getValue(), saturationSlider.getValue(),
                valueSlider.getValue(), opacitySlider.getValue());

        if (isSliderPressed()) {
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
        double hue = c.getHue();
        double saturation = c.getSaturation();
        double value = c.getBrightness();
        double opacity = c.getOpacity();

        if (!isSliderPressed()) {
            hueSlider.setValue(c.getHue());
            saturationSlider.setValue(c.getSaturation());
            valueSlider.setValue(c.getBrightness());
            opacitySlider.setValue(c.getOpacity());
        }

        setSliderBackground(saturationSlider,
                Color.hsb(hue, 0, value), Color.hsb(hue, 1, value));
        setSliderBackground(valueSlider,
                Color.hsb(hue, saturation, 0), Color.hsb(hue, saturation, 1));
        setSliderBackground(opacitySlider,
                Color.hsb(hue, saturation, value, 0), Color.hsb(hue, saturation, value, 1));
    }

    private void updateHistory() {
        // If we update the colour on every change, even when the sliders are
        // being used, then the history will quickly fill up with very similar
        // colours.
        //
        // To fix this, we check that the sliders are not pressed before
        // updating the history. To make sure the history still gets updated
        // by the sliders, we call this method when they receive a "mouse
        // released" event, i.e. when a colour has been chosen decisively.
        if (!isSliderPressed()) {
            history.update(colour.getValue());
        }
    }
}

// Display a coloured rectangles for each used colour sorted by recency.
// Allow the user to click on one of these rectangles to select its colour.
class ColourHistory extends FlowPane {

    private static int PADDING = 5;
    private static int MAX_HISTORY = 60;

    private ObjectProperty<Color> colour;

    private static class Entry extends Rectangle {

        private static final int SIZE = 20;

        private Color colour;

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

    public void update(Color colour) {
        removeAll(colour);

        Color firstColour = null;
        if (!getChildren().isEmpty()) {
            Entry firstEntry = (Entry) getChildren().get(0);
            firstColour = firstEntry.getColour();
        }

        if (!colour.equals(firstColour)) {
            Entry entry = new Entry(colour);
            getChildren().add(0, entry);

            entry.setOnMouseClicked(e -> this.colour.setValue(entry.getColour()));

            if (getChildren().size() > MAX_HISTORY) {
                getChildren().remove(getChildren().size() - 1);
            }
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
