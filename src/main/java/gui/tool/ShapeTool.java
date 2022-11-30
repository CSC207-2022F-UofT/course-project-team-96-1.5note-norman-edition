package gui.tool;

import gui.media.GUIEllipse;
import gui.media.GUIPolygon;
import gui.media.GUIRectangle;
import gui.media.GUIShape;
import gui.page.Page;
import gui.ResourceLoader;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * An implementation of Tool that allows the user to create various shapes
 */
public class ShapeTool implements Tool {
    private final HandlerMethod[] handlers;
    private Page page;

    private final ShapeSettings settings;

    /**
     * The currently selected color
     */
    private final ObjectProperty<Color> colour;

    /**
     * The currently selected hape
     */
    private GUIShape currentShape;

    /**
     * The point which the shape will be anchored to while drawn
     */
    private Point2D point1;

    /**
     * The point which defines the shape's dimensions
     */
    private Point2D point2;

    public ShapeTool(ObjectProperty<Color> colour) {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_PRESSED, this::startShapeMouse),
                new HandlerMethod<>(MouseEvent.MOUSE_RELEASED, this::endShapeMouse),
                new HandlerMethod<>(MouseEvent.MOUSE_DRAGGED, this::updateShapeMouse)
        };
        this.handlers = handlers;

        this.colour = colour;
        settings = new ShapeSettings();
    }

    @Override
    public String getName() {
        return "Shape";
    }

    @Override
    public Node getGraphic() {
        return new Label(
                getName(), ResourceLoader.loadSVGicon("icons/shape.svg", 15, 15));
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
        this.page = null;
    }

    @Override
    public FlowPane getSettingsGUI() {
        return settings;
    }

    public GUIShape getCurrentShape() {
        return currentShape;
    }

    /**
     * JavaFX mouse event wrapper that calls {@link #startShape(Point2D, Point2D, Color, ShapeType)}
     */
    private void startShapeMouse(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            point1 = page.getMouseCoords(e);

            startShape(point1, point1, colour.getValue(), settings.getShapeType());
        }
    }

    /**
     * JavaFX mouse event wrapper that calls {@link #updateShape(Point2D, Point2D, boolean)}
     */
    private void updateShapeMouse(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            point2 = page.getMouseCoords(e);
            updateShape(point1, point2, e.isShiftDown());
        }
    }

    /**
     * JavaFX mouse event wrapper that calls {@link #endShape()}
     */
    private void endShapeMouse(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            endShape();
        }
    }

    /**
     * Initializes a shape given two positions, its color and its type.
     * @param p1 The point to create the shape
     * @param p2 The first point that defines the shape
     * @param c1 The second point that defines the shape
     * @param type The enum representing the shape's type
     */
    public void startShape(Point2D p1, Point2D p2, Color c1, ShapeType type) {
        switch (type) {
            case RECTANGLE -> currentShape = new GUIRectangle(p1, p2, c1);
            case ELLIPSE -> currentShape = new GUIEllipse(p1, p2, c1);
            case POLYGON -> currentShape = new GUIPolygon(p1, p2, c1, settings.getPolySideCount());
        }
        page.addMedia(currentShape);
    }

    /**
     * Modifies and updates a shape to fit within the bounding box defined by two points.
     * @param p1 The first point that defines the shape
     * @param p2 The second point that defines the shape
     * @param sameSides Whether the shape should have the same width and height (e.x. making a square)
     */
    public void updateShape(Point2D p1, Point2D p2, boolean sameSides) {
        if (currentShape != null) {
            currentShape.update(p1, p2, sameSides);
        }
    }

    /**
     * Updates the page with the currently drawn shape
     */
    public void endShape() {
        if (currentShape != null) {
            page.updateMedia(currentShape);
            currentShape = null;
        }
    }
}

class ShapeSettings extends FlowPane {

    private static final int PADDING = 5;

    private static final int MIN_SIDE_COUNT = 1;
    private static final int MAX_SIDE_COUNT = 10;
    private static final int DEFAULT_SIDE_COUNT = 3;


    private ShapeType selectedShapeType = ShapeType.RECTANGLE;

    private final ObjectProperty<Double> sideCountProperty;

    public ShapeSettings(){
        // Draws the GUI for the settings panel

        // Setup Combobox representing the shape selector
        ComboBox<String> shapeSelector = new ComboBox<>();
        for(ShapeType value: ShapeType.values()){ // Initialize the ComboBox's values from the Shape Type Enums
            String name = value.name();
            name = name.charAt(0) + name.substring(1).toLowerCase();
            shapeSelector.getItems().add(name);
        }

        // Updating the stored shape enum when the ComboBox is selected
        shapeSelector.setOnAction((event) -> {
            String selectedItem = shapeSelector.getSelectionModel().getSelectedItem();
            selectedShapeType = ShapeType.valueOf(selectedItem.toUpperCase());
        });

        // Ensure initial values are selected
        shapeSelector.getSelectionModel().selectFirst();

        // Slider and Spinner for polygon side count
        Slider sideCountSlider = new Slider(MIN_SIDE_COUNT, MAX_SIDE_COUNT, DEFAULT_SIDE_COUNT);
        sideCountSlider.setBlockIncrement(1);
        sideCountSlider.setMajorTickUnit(1);
        sideCountSlider.setMinorTickCount(0);
        sideCountSlider.setSnapToTicks(true);

        Spinner<Double> sideCountSpinner = new Spinner<>();
        sideCountSpinner.setPrefWidth(75);
        sideCountSpinner.setEditable(true);

        sideCountProperty = sideCountSlider.valueProperty().asObject();

        // Necessary to bind the two elements together
        SpinnerValueFactory.DoubleSpinnerValueFactory spinnerFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(MIN_SIDE_COUNT, MAX_SIDE_COUNT, DEFAULT_SIDE_COUNT);
        sideCountSpinner.setValueFactory(spinnerFactory);
        spinnerFactory.valueProperty().bindBidirectional(sideCountProperty);
        spinnerFactory.setAmountToStepBy(1.0);

        // Create the Hbox containing all the polygon settings
        HBox sideCountBox = new HBox(PADDING, sideCountSpinner, sideCountSlider);
        sideCountBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(sideCountBox, Priority.ALWAYS);

        // Create the Vbox containing all the settings, and add to the page
        VBox shapeTypeList = new VBox(PADDING, new Label("Shape Type:"), shapeSelector, new Label("Polygon Settings:"), sideCountBox);
        shapeTypeList.setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(shapeTypeList);
    }

    public ShapeType getShapeType() {
        return selectedShapeType;
    }

    public int getPolySideCount() {
        return sideCountProperty.getValue().intValue();
    }
}
