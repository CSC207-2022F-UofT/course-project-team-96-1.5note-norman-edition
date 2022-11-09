package gui.page_screen;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.beans.value.*;

import gui.tool.Tool;


/**
 * GUI element which contains the settings for the selected tool.
 * <p>
 * This class implements the observer pattern w.r.t. the selected tool. It
 * automatically displays the relevant settings GUI when the selected tool
 * changes.
 */
class ToolPane extends BorderPane {

    private static final int PADDING = 5;
    private static final int PANE_SIZE = 300;

    private static final String HORIZONTAL_STYLE_CLASS = "horizontal";
    private static final String VERTICAL_STYLE_CLASS = "vertical";

    private VBox toolPane;
    private ToolPaneTitleBar titleBar;
    private ScrollPane settingsPane;
    private FlowPane toolSettingsGUI;
    private ToggleButton showHideButton;

    public ToolPane(ObservableValue<Tool> selectedTool) {
        // Don't block mouse clicks/other input
        setPickOnBounds(false);

        showHideButton = new ShowHideButton();
        showHideButton.selectedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                hideToolPane();
            } else {
                showToolPane();
            }
        });

        titleBar = new ToolPaneTitleBar(showHideButton);
        settingsPane = new ScrollPane();
        settingsPane.setFitToWidth(true);
        settingsPane.setFitToHeight(true);
        VBox.setVgrow(settingsPane, Priority.ALWAYS);


        toolPane = new VBox(titleBar, settingsPane);
        toolPane.getStyleClass().add("tool-pane");

        toolPane.setPrefWidth(PANE_SIZE);
        toolPane.setPrefHeight(PANE_SIZE);

        setOrientationForCurrentSize();

        widthProperty().addListener(w -> setOrientationForCurrentSize());
        heightProperty().addListener(h -> setOrientationForCurrentSize());

        setTool(selectedTool.getValue());
        selectedTool.addListener((o, oldVal, newVal) -> setTool(newVal));
    }

    // Change the position/orientation of the tool pane to better fit the
    // available screen space.
    private void setOrientationForCurrentSize() {
        double width = getWidth();
        double height = getHeight();

        if (width - PANE_SIZE < height) {
            setOrientation(Orientation.HORIZONTAL);
        } else {
            setOrientation(Orientation.VERTICAL);
        }
    }

    private void setOrientation(Orientation o) {
        toolPane.getStyleClass().removeAll(
                VERTICAL_STYLE_CLASS, HORIZONTAL_STYLE_CLASS);

        switch (o) {
            case HORIZONTAL:
                setLeft(null);
                setBottom(toolPane);
                toolPane.getStyleClass().add(HORIZONTAL_STYLE_CLASS);
                break;
            case VERTICAL:
                setBottom(null);
                setLeft(toolPane);
                toolPane.getStyleClass().add(VERTICAL_STYLE_CLASS);
                break;
        }
    }

    private void setTool(Tool tool) {
        settingsPane.setContent(null);

        titleBar.setTitle(tool.getName());
        toolSettingsGUI = tool.getSettingsGUI();

        if (toolSettingsGUI != null) {
            settingsPane.setContent(toolSettingsGUI);
            toolSettingsGUI.paddingProperty().setValue(new Insets(PADDING));
            toolSettingsGUI.setHgap(PADDING);
            toolSettingsGUI.setVgap(PADDING);
        }
    }

    private void hideToolPane() {
        toolPane.setMaxHeight(titleBar.getHeight());
        settingsPane.setMinHeight(0);
        settingsPane.setDisable(true);
    }

    private void showToolPane() {
        toolPane.setMaxHeight(Double.MAX_VALUE);
        settingsPane.setDisable(false);
    }
}

class ToolPaneTitleBar extends HBox {

    private static final int PADDING = 2;
    private static final int LEFT_PADDING = 5;

    private Text titleText;

    public ToolPaneTitleBar(Node... contents) {
        super(PADDING);

        titleText = new Text();

        setAlignment(Pos.CENTER_LEFT);
        paddingProperty().setValue(new Insets(PADDING, PADDING, PADDING, LEFT_PADDING));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(titleText, spacer);
        getChildren().addAll(contents);
    }

    public void setTitle(String title) {
        titleText.setText(title + " Settings");
    }
}

class ShowHideButton extends ToggleButton {

    private static String SHOW_TEXT = "Show";
    private static String HIDE_TEXT = "Hide";

    public ShowHideButton() {
        updateText();
        selectedProperty().addListener(o -> updateText());
    }

    private void updateText() {
        if (isSelected()) {
            setText(SHOW_TEXT);
        } else {
            setText(HIDE_TEXT);
        }
    }
}
