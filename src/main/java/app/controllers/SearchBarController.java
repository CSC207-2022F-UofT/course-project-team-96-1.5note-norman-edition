package app.controllers;

import app.interaction_managers.Searcher;
import app.media.Media;

import java.util.ArrayList;

public class SearchBarController {

    public ArrayList<Double> xPos = new ArrayList<>();
    public ArrayList<Double> yPos = new ArrayList<>();

    public int results;

    public SearchBarController(ArrayList<Media> mediaArrayList, String searchPrompt){
        Searcher searcher = new Searcher(mediaArrayList);
        searcher.interact(searchPrompt);

        for (Media media: searcher.getResults()){
            this.xPos.add(media.getX());
            this.yPos.add(media.getY());
        }
        this.results = xPos.size();
    }
}
