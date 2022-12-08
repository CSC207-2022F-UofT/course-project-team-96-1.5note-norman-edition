package gui.tool;

import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;
import javafx.event.*;
import javafx.beans.*;
import javafx.geometry.*;

import gui.ResourceLoader;
import gui.media.GUIMedia;
import gui.page.Page;
import gui.page.Selection;


// Enum for the current action. Used for determining which strategy to use when
// handling user input.
enum Action {
    DRAGGING,
    SELECTING,
    NONE
}

/**
 * The Media tool allows selecting multiple media and modifying the properties
 * common to all media types.
 * <p>
 * The properties which can be modified are:
 * <ul>
 * <li> Position
 * <li> Rotation
 * <li> Name
 * <li> Z-Index
 * </ul>
 * <p>
 * The tool also allows the deletion of media.
 * <p>
 * Media can be selected by clicking, or by clicking and dragging a box
 * surrounding the media to select.
 */
public class MediaTool implements Tool {

    private Selection selection;
    private Page page;
    private final HandlerMethod<?>[] handlers;

    private Point2D boxSelectStart;
    private Point2D boxSelectEnd;
    private final BoxSelectRectangle boxSelectRectangle;

    private static final Color SELECTED_COLOUR = Color.DODGERBLUE;
    private final Effect selectedEffect;
    
    private Point2D prevDragPosition;

    private Action action;

    private final MediaSettings settings;

    public MediaTool() {
        boxSelectRectangle = new BoxSelectRectangle();
        selectedEffect = new DropShadow(20, SELECTED_COLOUR);
        action = Action.NONE;

        handlers = new HandlerMethod[] {
            new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::mouseClicked),
            new HandlerMethod<>(MouseEvent.MOUSE_PRESSED, this::mousePressed),
            new HandlerMethod<>(MouseEvent.MOUSE_RELEASED, this::mouseReleased),
            new HandlerMethod<>(MouseEvent.MOUSE_DRAGGED, this::mouseDragged),
        };

        settings = new MediaSettings();
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
        selection = new Selection(page, selectedEffect);
        settings.setSelection(selection);
    }

    @Override
    public void disabledFor(Page page) {
        clearSelection();
        hideBoxSelectRectangle();
        settings.setSelection(null);

        selection = null;
        this.page = null;
    }

    @Override
    public HandlerMethod<?>[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public String getName() {
        return "Media";
    }

    @Override
    public Node getGraphic() {
        return new Label(
                getName(), ResourceLoader.loadSVGicon("icons/media.svg", 15, 15));
    }

    @Override
    public Node getSettingsGUI() {
        return settings;
    }

    public void selectMedia(GUIMedia<?> media) {
        selection.addMedia(media);
    }

    public void unSelectMedia(GUIMedia<?> media) {
        selection.removeMedia(media);
    }

    public void clearSelection() {
        selection.removeAllMedia();
    }

    private void showBoxSelectRectangle() {
        hideBoxSelectRectangle();
        page.setUIlayer(boxSelectRectangle);
    }

    private void hideBoxSelectRectangle() {
        page.setUIlayer(null);
    }

    private void updateBoxSelectRectangle() {
        boxSelectRectangle.update(
                page.getCoordsInv(boxSelectStart),
                page.getCoordsInv(boxSelectEnd));
    }

    private void mouseClicked(MouseEvent e) {
        if (
                e.getButton() == MouseButton.PRIMARY && action == Action.NONE
                && e.isStillSincePress())
        {
            e.consume();

            if (!e.isShiftDown()) {
                clearSelection();
            }

            EventTarget target = e.getTarget();
            if (target instanceof GUIMedia<?> media) {
                if (selection.contains(media)) {
                    unSelectMedia(media);
                } else {
                    selectMedia(media);
                }
            }
        }
    }

    private void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            page.requestFocus();
            e.consume();

            EventTarget target = e.getTarget();
            if (
                    target instanceof GUIMedia
                    && selection.contains((GUIMedia<?>) target))
            {
                action = Action.DRAGGING;
                beginDrag(e);
            } else {
                action = Action.SELECTING;
                beginBoxSelect(e);
            }
        }
    }

    private void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            if (action == Action.SELECTING) {
                endBoxSelect(e);
            }

            action = Action.NONE;
        }
    }

    private void mouseDragged(MouseEvent e) {
        e.consume();

        if (e.getButton() == MouseButton.PRIMARY) {
            if (action == Action.DRAGGING) {
                updateDrag(e);
            } else if (action == Action.SELECTING) {
                updateBoxSelect(e);
            }
        }
    }

    private void beginDrag(MouseEvent e) {
        prevDragPosition = page.getMouseCoords(e);
    }

    private void updateDrag(MouseEvent e) {
        Point2D dragPosition = page.getMouseCoords(e);
        Point2D dragDisplacement = dragPosition.subtract(prevDragPosition);

        moveSelectedMedia(dragDisplacement);

        prevDragPosition = dragPosition;
    }

    public void moveSelectedMedia(Point2D displacement) {
        selection.move(displacement);
    }

    private void beginBoxSelect(MouseEvent e) {
        boxSelectStart = page.getMouseCoords(e);
        boxSelectEnd = boxSelectStart;
        updateBoxSelectRectangle();
        showBoxSelectRectangle();
    }

    private void updateBoxSelect(MouseEvent e) {
        boxSelectEnd = page.getMouseCoords(e);
        updateBoxSelectRectangle();
    }

    private void endBoxSelect(MouseEvent e) {
        boxSelect(boxSelectStart, boxSelectEnd, !e.isShiftDown());
        hideBoxSelectRectangle();
    }

    private void boxSelect(
            Point2D boxSelectStart,
            Point2D boxSelectEnd,
            boolean clearSelection)
    {
        if (!boxSelectStart.equals(boxSelectEnd)) {
            double minX = Math.min(boxSelectEnd.getX(), boxSelectStart.getX());
            double minY = Math.min(boxSelectEnd.getY(), boxSelectStart.getY());
            double width = Math.abs(boxSelectEnd.getX() - boxSelectStart.getX());
            double height = Math.abs(boxSelectEnd.getY() - boxSelectStart.getY());

            Bounds selectBox = new BoundingBox(minX, minY, width, height);

            if (clearSelection) {
                clearSelection();
            }

            for (GUIMedia<?> media: page.getAllMedia()) {
                if (selectBox.intersects(media.getBoundsInParent())) {
                    selectMedia(media);
                }
            }
        }
    }
}


class MediaSettings extends FlowPane {

    private static final int PADDING = 5;

    private final Label numSelectedLabel;
    private final TextField nameField;
    private final Slider angleSlider;
    private final Spinner<Integer> zIndexSpinner;

    private Selection selection;

    public MediaSettings() {
        VBox settingsBox = new VBox(PADDING);

        numSelectedLabel = new Label("0");
        HBox numSelectedRow = new HBox(
                numSelectedLabel, new Label(" Media Selected"));
        settingsBox.getChildren().add(numSelectedRow);
        settingsBox.getChildren().add(new Separator());

        nameField = new TextField();
        HBox.setHgrow(nameField, Priority.ALWAYS);
        Button setNameButton = new Button("Apply");
        setNameButton.setOnAction(e -> {
            nameField.commitValue();
            renameSelection();
        });

        angleSlider = new Slider(0.0, 360.0, 0.5);
        HBox.setHgrow(angleSlider, Priority.ALWAYS);

        zIndexSpinner = new Spinner<>(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);

        Button deleteButton = new Button("Delete All Selected Media");

        HBox[] rows = new HBox[] {
            new HBox(new Label("Name:"), nameField, setNameButton),
            new HBox(new Label("Rotation:"), angleSlider),
            new HBox(new Label("Z-Index:"), zIndexSpinner),
        };

        for (HBox row: rows) {
            ((Label) row.getChildren().get(0)).setMinWidth(60);
            row.setAlignment(Pos.CENTER_LEFT);
        }

        settingsBox.getChildren().addAll(rows);
        settingsBox.getChildren().add(new Separator());
        settingsBox.getChildren().add(deleteButton);

        getChildren().add(settingsBox);

        deleteButton.setOnAction(e -> deleteSelection());
        nameField.setOnAction(e -> renameSelection());
        angleSlider.valueProperty().addListener(o -> rotateSelection());
        zIndexSpinner.valueProperty().addListener(o -> setSelectionZindex());
    }

    public void setSelection(Selection selection) {
        this.selection = selection;

        if (selection != null) {
            ((Observable) selection.getMedia()).addListener(o -> update());
            update();
        }
    }

    /*
     * When the selection changes, the settings GUI is updated according to the
     * selection.
     */
    private void update() {
        setDisable(selection.getMedia().isEmpty());
        numSelectedLabel.setText(Integer.valueOf(selection.getMedia().size()).toString());
        nameField.setText(selection.getName());
        angleSlider.setValue(selection.getAngle());
        zIndexSpinner.getValueFactory().setValue(selection.getZindex());
    }

    private void deleteSelection() {
        selection.delete();
    }

    private void renameSelection() {
        selection.rename(nameField.getText());
    }

    private void rotateSelection() {
        if (angleSlider.isFocused()) {
            selection.rotate(angleSlider.getValue());
        }
    }

    private void setSelectionZindex() {
        if (zIndexSpinner.isFocused()) {
            selection.setZindex(zIndexSpinner.getValue());
        }
    }
}
