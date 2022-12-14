package app.interaction_managers;

import app.media.Media;
import app.media.MediaText;

import java.util.ArrayList;

public class Searcher implements InteractionManager {

    private String userSearch;
    private final ArrayList<Media> mediaList;
    public Searcher(ArrayList<Media> mediaArrayList){
        this.mediaList = mediaArrayList;
    }
    public ArrayList<Media> getResults(){
        ArrayList<Media> results = new ArrayList<>();
        for (Media media : mediaList){
            if(media.getTags().contains(userSearch) || media.getName().contains(userSearch)){
                results.add(media);
            }
            else if(media instanceof MediaText){
                if (((MediaText) media).getText().contains(userSearch)){
                    results.add(media);
                }
            }

        }
        return results;
    }

    @Override
    public void interact(String node) {
        this.userSearch = node;

    }
}
