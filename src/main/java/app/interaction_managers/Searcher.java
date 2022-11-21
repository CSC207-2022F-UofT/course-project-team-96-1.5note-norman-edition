package app.interaction_managers;

import app.MediaCommunicator;
import app.interaction_managers.InteractionManager;
import app.media.Media;
import app.media.MediaText;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Searcher implements InteractionManager {

    private String userSearch;
    private final ArrayList<Media> mediaList;
    public Searcher(ArrayList<Media> mediaArrayList){
        this.mediaList = mediaArrayList;
    }
    public ArrayList<Double> getXResults(){
        ArrayList<Double> xResults = new ArrayList<>();
        for (Media media : mediaList){
            if(media.getTags().contains(userSearch)){
                xResults.add(media.getX());
            }
            else if(media instanceof MediaText){
                if (((MediaText) media).getText().equals(userSearch)){
                    xResults.add(media.getX());
                }
            }

        }
        return xResults;
    }
    public ArrayList<Double> getYResults(){
        ArrayList<Double> yResults = new ArrayList<>();
        for (Media media : mediaList){
            if(media.getTags().contains(userSearch)){
                yResults.add(media.getY());
            }
            else if(media instanceof MediaText){
                if (((MediaText) media).getText().equals(userSearch)){
                    yResults.add(media.getY());
                }
            }

        }
        return yResults;
    }

    @Override
    public void interact(TextField node) {
        this.userSearch = node.getText();

    }
}
