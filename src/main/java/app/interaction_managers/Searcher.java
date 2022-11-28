package app.interaction_managers;

import app.media.Media;
import app.media.MediaText;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Searcher implements InteractionManager {

    private String userSearch;
    private final ArrayList<Media> mediaList;
    public ArrayList<Double> xCoordinates;
    public ArrayList<Double> yCoordinates;
    public Searcher(ArrayList<Media> mediaArrayList){
        this.mediaList = mediaArrayList;
    }
    public void getCoordinates(){
        ArrayList<Double> xResults = new ArrayList<>();
        ArrayList<Double> yResults = new ArrayList<>();
        for (Media media : mediaList){
            if(media.getTags().contains(userSearch)){
                xResults.add(media.getX());
                yResults.add(media.getY());

            }
            else if(media instanceof MediaText){
                if (((MediaText) media).getText().equals(userSearch)){
                    xResults.add(media.getX());
                    yResults.add(media.getY());
                }
            }

        }
        this.xCoordinates = xResults;
        this.yCoordinates = yResults;
    }

    @Override
    public void interact(String input) {
        this.userSearch = input;

    }
}
