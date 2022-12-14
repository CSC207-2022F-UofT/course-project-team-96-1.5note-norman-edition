package gui.page_screen;

import java.util.List;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.beans.value.*;

import gui.ResourceLoader;
import gui.tool.Tool;


/**
 * GUI element which contains the settings for the selected tool.
 * <p>
 * This class implements the observer pattern w.r.t. the selected tool. It
 * automatically displays the relevant settings GUI when the selected tool
 * changes.
 */
class ToolPane extends BorderPane {

    private static final int PANE_SIZE = 300;

    private static final String HORIZONTAL_STYLE_CLASS = "horizontal";
    private static final String VERTICAL_STYLE_CLASS = "vertical";

    private final VBox toolPane;
    private final ToolPaneTitleBar titleBar;
    private final ScrollPane settingsPane;

    public ToolPane(ObservableValue<Tool> selectedTool) {
        // Don't block mouse clicks/other input
        setPickOnBounds(false);

        ToggleButton showHideButton = new ShowHideButton();
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
        List<String> styleClass = toolPane.getStyleClass();

        switch (o) {
            case HORIZONTAL -> {
                setLeft(null);
                setBottom(toolPane);
                styleClass.remove(VERTICAL_STYLE_CLASS);
                if (!styleClass.contains(HORIZONTAL_STYLE_CLASS)) {
                    styleClass.add(HORIZONTAL_STYLE_CLASS);
                }
            } case VERTICAL -> {
                setBottom(null);
                setLeft(toolPane);
                styleClass.remove(HORIZONTAL_STYLE_CLASS);
                if (!styleClass.contains(VERTICAL_STYLE_CLASS)) {
                    styleClass.add(VERTICAL_STYLE_CLASS);
                }
            }
        }
    }

    private void setTool(Tool tool) {
        settingsPane.setContent(null);

        titleBar.setTitle(tool.getName());
        Node toolSettingsGUI = tool.getSettingsGUI();

        if (toolSettingsGUI != null) {
            settingsPane.setContent(toolSettingsGUI);
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

    private final Text titleText;

    public ToolPaneTitleBar(Node... contents) {
        super(PADDING * 2);

        titleText = new Text();

        setAlignment(Pos.CENTER_LEFT);
        paddingProperty().setValue(new Insets(PADDING, PADDING, PADDING, LEFT_PADDING));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Node icon = ResourceLoader.loadSVGicon("icons/wrench.svg", 12, 12, Color.GRAY);

        getChildren().addAll(icon, titleText, spacer);
        getChildren().addAll(contents);
    }

    public void setTitle(String title) {
        titleText.setText(title + " Tool");
    }
}

class ShowHideButton extends ToggleButton {

    private static final String SHOW_TEXT = "Show";
    private static final String HIDE_TEXT = "Hide";

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
