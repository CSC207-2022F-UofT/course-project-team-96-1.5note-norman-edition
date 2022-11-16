package gui.tool;

import app.controllers.ToolBarController;
import gui.page.Page;
import javafx.scene.Node;
import javafx.scene.control.Button;

//TODO: temp class for testing purposes
public class AudioTool implements Tool{
    private Page page;
    public AudioTool()  {

    }
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public String getName() {return "Import Audio";}

    @Override
    public Node getSettingsGUI()    {
        Button add = new Button("Add");
        add.setOnAction(e ->    {
            ToolBarController tbc = new ToolBarController();
            tbc.insertAudio(this.page);
        });
        return add;
    }
}
