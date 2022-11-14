package gui.tool;

import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
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

    public ColourSettings(ObjectProperty<Color> colour) {
        super(PADDING, PADDING);
        this.colour = colour;

        colourPicker = new ColorPicker();
        colourPicker.valueProperty().bindBidirectional(colour);

        hueSlider = new Slider(0, 360, 0);
        saturationSlider = new Slider(0, 1, 0);
        valueSlider = new Slider(0, 1, 0);
        opacitySlider = new Slider(0, 1, 0);

        hueSlider.valueProperty().addListener(o -> setColourFromSliders());
        saturationSlider.valueProperty().addListener(o -> setColourFromSliders());
        valueSlider.valueProperty().addListener(o -> setColourFromSliders());
        opacitySlider.valueProperty().addListener(o -> setColourFromSliders());
        colour.addListener(o -> setSlidersFromColour());

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
            row.setSpacing(PADDING);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setMinWidth(200);
            getChildren().add(row);
        }
    }

    private void setColourFromSliders() {
        Color sliderColour = Color.hsb(
                hueSlider.getValue(), saturationSlider.getValue(),
                valueSlider.getValue(), opacitySlider.getValue());

        if (
                hueSlider.isPressed() || saturationSlider.isPressed()
                || valueSlider.isPressed() || opacitySlider.isPressed())
        {
            colour.setValue(sliderColour);
        }
    }

    private void setSlidersFromColour() {
        Color c = colour.getValue();

        if (
                !hueSlider.isPressed() && !saturationSlider.isPressed()
                && !valueSlider.isPressed() && !opacitySlider.isPressed())
        {
            hueSlider.setValue(c.getHue());
            saturationSlider.setValue(c.getSaturation());
            valueSlider.setValue(c.getBrightness());
            opacitySlider.setValue(c.getOpacity());
        }
    }
}
