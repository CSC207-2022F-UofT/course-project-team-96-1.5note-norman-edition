package gui.tool;

import app.media.EllipseShape;
import gui.media.GUIEllipse;
import gui.media.GUIPolygon;
import gui.media.GUIRectangle;
import gui.media.GUIShape;
import gui.page.Page;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ShapeTool implements Tool {
    private HandlerMethod[] handlers;
    private Page page;

    private ShapeSettings settings;

    private ObjectProperty<Color> colour;

    private GUIShape currentShape;

    private Point2D point1;

    private Point2D point2;

    public ShapeTool(ObjectProperty<Color> colour) {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_PRESSED, this::startShape),
                new HandlerMethod<>(MouseEvent.MOUSE_RELEASED, this::endShape),
                new HandlerMethod<>(MouseEvent.MOUSE_DRAGGED, this::updateShape)
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

    private void startShape(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            point1 = page.getMouseCoords(e);

            if (settings.getShapeType() != null) {
                ShapeType shapeType = settings.getShapeType();
                switch (shapeType) {
                    case RECTANGLE -> {
                        System.out.println("GUI Rectangle");
                        currentShape = new GUIRectangle(point1, point1, colour.getValue());
                    }
                    case ELLIPSE -> {
                        System.out.println("GUI Ellipse");
                        currentShape = new GUIEllipse(point1, point1, colour.getValue());
                    }
                    case POLYGON -> {
                        System.out.println("GUI Polygon");
                        currentShape = new GUIPolygon(point1, point1, colour.getValue(), settings.getPolySideCount());
                    }
                }
                page.addMedia(currentShape);
            }
        }
    }

    private void updateShape(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            if (currentShape != null) {
                point2 = page.getMouseCoords(e);
                currentShape.update(point1, point2, e.isShiftDown());
            }
        }
    }

    private void endShape(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            finishShape();
        }
    }

    private void finishShape() {
        if (currentShape != null) {
            System.out.println(currentShape.getMedia());
            System.out.println(point1+" "+point2);
            page.updateMedia(currentShape);
            currentShape = null;
        }
    }
}

enum ShapeType{
    RECTANGLE,
    ELLIPSE,
    POLYGON
}

class ShapeSettings extends FlowPane {

    private static final int PADDING = 5;

    private static final int MIN_SIDE_COUNT = 1;
    private static final int MAX_SIDE_COUNT = 10;
    private static final int DEFAULT_SIDE_COUNT = 3;


    private static ShapeType selectedShapeType = ShapeType.RECTANGLE;

    private Slider sideCountSlider;
    private Spinner<Double> sideCountSpinner;
    private ObjectProperty<Double> sideCountProperty;

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
        sideCountSlider = new Slider(MIN_SIDE_COUNT, MAX_SIDE_COUNT, DEFAULT_SIDE_COUNT);
        sideCountSlider.setBlockIncrement(1);
        sideCountSlider.setMajorTickUnit(1);
        sideCountSlider.setMinorTickCount(0);
        sideCountSlider.setSnapToTicks(true);

        sideCountSpinner = new Spinner<>();
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