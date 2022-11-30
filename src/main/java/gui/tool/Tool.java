package gui.tool;

import javafx.scene.*;
import javafx.scene.control.*;

import gui.page.PageEventHandler;


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
public interface Tool extends PageEventHandler {

    /**
     * Return the "graphic" to show in the toolbar for this tool.
     */
    default Node getGraphic() {
        return new Label(getName());
    }

    /**
     * Return the name of this tool.
     */
    default String getName() {
        return getClass().getName();
    }

    /**
     * Return the GUI element which configures the settings for this tool.
     * <p>
     * The returned Node will be shown in the tool pane when this tool is
     * selected.
     */
    default Node getSettingsGUI() {
        return null;
    }
}
