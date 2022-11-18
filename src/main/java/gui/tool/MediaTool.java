package gui.tool;

import java.util.Set;
import java.util.HashSet;

import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;
import javafx.event.*;
import javafx.geometry.*;

import app.media.Media;
import gui.media.GUIMedia;
import gui.page.Page;
import gui.page.PageEventHandler;
import gui.page.PageEventHandler.HandlerMethod;


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

    private Set<GUIMedia> selection;
    private Page page;
    private HandlerMethod[] handlers;

    private Point2D boxSelectStart;
    private Point2D boxSelectEnd;
    private BoxSelectRectangle boxSelectRectangle;

    private static final Color SELECTED_COLOUR = Color.DODGERBLUE;
    private Effect selectedEffect;
    
    private Point2D prevDragPosition;

    private Action action;

    private MediaSettings settings;

    public MediaTool() {
        selection = new HashSet<>();
        boxSelectRectangle = new BoxSelectRectangle();
        selectedEffect = new DropShadow(
                BlurType.ONE_PASS_BOX,
                SELECTED_COLOUR,
                10, 1, 0, 0);
        action = Action.NONE;

        handlers = new HandlerMethod[] {
            new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::mouseClicked),
            new HandlerMethod<>(MouseEvent.MOUSE_PRESSED, this::mousePressed),
            new HandlerMethod<>(MouseEvent.MOUSE_RELEASED, this::mouseReleased),
            new HandlerMethod<>(MouseEvent.MOUSE_DRAGGED, this::mouseDragged),
        };

        settings = new MediaSettings();
        settings.update(selection, page);
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public void disabledFor(Page page) {
        clearSelection();
        hideBoxSelectRectangle();

        this.page = null;
    }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public String getName() {
        return "Media";
    }

    @Override
    public Node getSettingsGUI() {
        return settings;
    }

    private void selectMedia(GUIMedia media) {
        selection.add(media);
        media.setEffect(selectedEffect);
        settings.update(getValidSelection(), page);
    }

    private void unSelectMedia(GUIMedia media) {
        selection.remove(media);
        media.setEffect(null);
        settings.update(getValidSelection(), page);
    }

    private void clearSelection() {
        for (GUIMedia media: selection) {
            media.setEffect(null);
        }

        selection.clear();
        settings.update(getValidSelection(), page);
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
            if (target instanceof GUIMedia) {
                GUIMedia media = (GUIMedia) target;

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
                    && selection.contains((GUIMedia) target))
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

            if (action == Action.DRAGGING) {
                endDrag();
            } else if (action == Action.SELECTING) {
                endBoxSelect(e);
            }

            action = Action.NONE;
        }
    }

    private void mouseDragged(MouseEvent e) {
        e.consume();

        if (e.getButton() == MouseButton.PRIMARY) {
            if (action == Action.DRAGGING) {
                drag(e);
            } else if (action == Action.SELECTING) {
                boxSelect(e);
            }
        }
    }

    // Return the intersection of the selected GUIMedia with the GUIMedia that
    // are currently in the page
    private Set<GUIMedia> getValidSelection() {
        Set<GUIMedia> validSelection = new HashSet<>(page.getAllMedia());
        validSelection.retainAll(selection);

        return validSelection;
    }

    private void beginDrag(MouseEvent e) {
        prevDragPosition = page.getMouseCoords(e);
    }

    private void drag(MouseEvent e) {
        Point2D dragPosition = page.getMouseCoords(e);
        Point2D dragDisplacement = dragPosition.subtract(prevDragPosition);

        for (GUIMedia selected: selection) {
            Media media = selected.getMedia();
            media.setX(media.getX() + dragDisplacement.getX());
            media.setY(media.getY() + dragDisplacement.getY());
        }

        prevDragPosition = dragPosition;
    }

    private void endDrag() {
        for (GUIMedia media: getValidSelection()) {
            page.updateMedia(media);
        }
    }

    private void beginBoxSelect(MouseEvent e) {
        boxSelectStart = page.getMouseCoords(e);
        boxSelectEnd = boxSelectStart;
        updateBoxSelectRectangle();
        showBoxSelectRectangle();
    }

    private void boxSelect(MouseEvent e) {
        boxSelectEnd = page.getMouseCoords(e);
        updateBoxSelectRectangle();
    }

    private void endBoxSelect(MouseEvent e) {
        if (!boxSelectStart.equals(boxSelectEnd)) {
            double minX = Math.min(boxSelectEnd.getX(), boxSelectStart.getX());
            double minY = Math.min(boxSelectEnd.getY(), boxSelectStart.getY());
            double width = Math.abs(boxSelectEnd.getX() - boxSelectStart.getX());
            double height = Math.abs(boxSelectEnd.getY() - boxSelectStart.getY());

            Bounds selectBox = new BoundingBox(minX, minY, width, height);

            if (!e.isShiftDown()) {
                clearSelection();
            }

            for (GUIMedia media: page.getAllMedia()) {
                if (selectBox.intersects(media.getBoundsInParent())) {
                    selectMedia(media);
                }
            }
        }

        hideBoxSelectRectangle();
    }
}


class MediaSettings extends FlowPane {

    private static int PADDING = 5;

    private Label numSelectedLabel;
    private TextField nameField;
    private Slider angleSlider;
    private Spinner<Integer> zIndexSpinner;
    private Button deleteButton;

    private Set<GUIMedia> selection;
    private Page page;

    public MediaSettings() {
        selection = new HashSet<>();

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

        deleteButton = new Button("Delete All Selected Media");

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

    /*
     * When the selection changes, the settings GUI is updated according to the
     * selection.
     *
     * - The size of the selection is dislayed in the `numSelectedLabel`
     *
     * - If the selected media all have the same name, that name is shown in
     *   the `nameField` text field. Otherwise, the field is empty.
     *
     * - The angle slider is set to the average angle of rotation of the
     *   selected media.
     *
     * - The Z-Index spinner is set to the maximum Z-Index among the selected
     *   media.
     */
    public void update(Set<GUIMedia> selection, Page page) {
        setDisable(selection.isEmpty());
        this.selection = selection;
        this.page = page;

        numSelectedLabel.setText(Integer.valueOf(selection.size()).toString());

        String name = null;
        double angle = 0;
        int zIndex = 0;

        for (GUIMedia selected: selection) {
            Media media = selected.getMedia();

            if (name == null || media.getName().equals(name)) {
                name = media.getName();
            } else {
                name = "";
            }

            if (media.getZindex() > zIndex) {
                zIndex = media.getZindex();
            }

            angle += media.getAngle() / selection.size();
        }

        nameField.setText(name);
        angleSlider.setValue(angle);
        zIndexSpinner.getValueFactory().setValue(zIndex);
    }

    private void deleteSelection() {
        for (GUIMedia media: selection) {
            page.getCommunicator().deleteMedia(media.getID());
        }

        selection.clear();
        update(selection, page);
    }

    private void renameSelection() {
        for (GUIMedia media: selection) {
            media.getMedia().setName(nameField.getText());
            page.updateMedia(media);
        }
    }

    private void rotateSelection() {
        if (angleSlider.isFocused()) {
            for (GUIMedia media: selection) {
                media.getMedia().setAngle(angleSlider.getValue());
                page.updateMedia(media);
            }
        }
    }

    private void setSelectionZindex() {
        if (zIndexSpinner.isFocused()) {
            for (GUIMedia media: selection) {
                media.getMedia().setZindex(zIndexSpinner.getValue());
                page.updateMedia(media);
            }
        }
    }
}
