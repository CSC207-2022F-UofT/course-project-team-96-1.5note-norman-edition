package gui.tool;

import app.controllers.ToolBarController;
import gui.error_window.ErrorWindow;
import gui.media.GUIAudio;
import gui.media.GUIVideo;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class VideoTool extends AudioTool{
    public VideoTool(String type)  {
        super(type);
    }

    @Override
    public void setSelectedAudio(MouseEvent e) {
        e.consume();
        javafx.event.EventTarget target = e.getTarget();

        //Checking if the player clicked something related to a GUIAudio
        if (target instanceof GUIVideo || ((Node) target).getParent() instanceof GUIVideo) {
            assert target instanceof GUIVideo;  //This will always pass by the time we get here
            setSelectedPlayer((GUIVideo) target);
            setInterface();
        }   else {
            //Disabling parts of the interface linked to timestamps
            resetInterface("Please Select a Video Player");
        }
        //Ensures selection box is never empty
        super.getSettingsGUI().getTimestampSelection().getSelectionModel().select(0);
    }

    @Override
    protected void configureAddAudio(ToolBarController tbc)   {
        //When the addAudio button is clicked, initiate MediaAudio creation process
        getSettingsGUI().getAddMedia().setOnAction(e ->    {
            try {
                tbc.insertVideo(getPage().getCommunicator());
            } catch (Exception ex) {
                new ErrorWindow(getPage(), "There was an error loading you file",
                        "An exception occured" + "in the process of loading your file", ex);
            }
        });
    }

    @Override
    public String getName() {return "Manage Video";}


}
