package gui.page_screen;

import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.beans.*;
import javafx.beans.value.*;
import javafx.beans.property.*;

import java.util.Map;
import java.util.HashMap;

import gui.tool.Tool;


/**
 * GUI element which allows the user to select the active tool.
 */
class Toolbar extends FlowPane {

    private static final int PADDING = 5;

    private ToggleGroup toggles;
    // Map toggle buttons to tools, so we can get the currently selected tool
    // when the selected toggle changes.
    private Map<Toggle, Tool> toolMap;
    private ReadOnlyObjectWrapper<Tool> selectedTool;

    public Toolbar(Tool[] tools) {
        super(PADDING, PADDING);

        paddingProperty().setValue(new Insets(PADDING));

        getStyleClass().add("toolbar");

        selectedTool = new ReadOnlyObjectWrapper<>(null);

        toolMap = new HashMap<>();
        toggles = new ToggleGroup();

        toggles.selectedToggleProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && newVal != oldVal) {
                selectedTool.setValue(toolMap.get(newVal));
            }
        });

        for (Tool tool: tools) {
            addTool(tool);
        }

        if (tools.length > 0) {
            toggles.getToggles().get(0).setSelected(true);
        }
    }

    private void addTool(Tool tool) {
        ToggleButton toolButton = new ToolButton(tool);
        toggles.getToggles().add(toolButton);
        toolMap.put(toolButton, tool);

        getChildren().add(toolButton);
    }

    public ObservableValue<Tool> selectedTool() {
        return selectedTool.getReadOnlyProperty();
    }
}


class ToolButton extends ToggleButton {

    public ToolButton(Tool tool) {
        super(null, tool.getGraphic());

        // Prevent de-selecting a ToolButton by clicking it, i.e. the only way
        // to de-select a tool should be to select another tool.
        setOnAction(e -> {
            if (!isSelected()) {
                select();
            }
        });
    }

    public void select() {
        setSelected(true);
    }
}
