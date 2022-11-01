package gui.page_screen;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import gui.page.GUIPageTool;


/**
 * Interface to be implemented by tools to provide the GUI with which they are
 * configured as well as the graphic which represents them in the toolbar.
 * <p>
 * Tools provide the following:
 * <ul>
 *  <li> A "graphic" which will be displayed in the toolbar.
 *  <li> The name of the tool.
 *  <li> The GUI with which the tool is configured.
 * </ul>
 */
public interface Tool extends GUIPageTool {

    default Node getGraphic() {
        return new Text(getName());
    }

    default String getName() {
        return getClass().getName();
    }

    default FlowPane getSettingsGUI() {
        return null;
    }
}
