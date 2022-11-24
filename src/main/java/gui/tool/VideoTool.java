package gui.tool;

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
    public String getName() {return "Manage Video";}


}
