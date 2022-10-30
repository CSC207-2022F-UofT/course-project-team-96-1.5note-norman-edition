package gui.page;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;


/**
 * Interface to be implemented by tools.
 * <p>
 * Tools provide the following:
 * <ul>
 *  <li> A "graphic" which will be displayed in the toolbar.
 *  <li> The mouse cursor to use for this tool.
 *  <li> The name of the tool.
 *  <li> The GUI with which the tool is configured.
 *  <li> The functionality to perform in response to user interaction with the page.
 * </ul>
 */
public interface GUIPageTool {

    default Node getGraphic() {
        return new Text(getName());
    }

    default Cursor getCursor() {
        return null;
    }

    default String getName() {
        return getClass().getName();
    }

    default FlowPane getSettingsGUI() {
        return null;
    }
}
