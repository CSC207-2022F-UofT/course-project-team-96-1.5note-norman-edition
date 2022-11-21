package app.controllers;

import app.interaction_managers.Searcher;
import app.media.Media;
import javafx.scene.Node;

import java.util.ArrayList;

public class SearchBarController {

    public ArrayList<Double> xPos;
    public ArrayList<Double> yPos;

    public int results;
    public SearchBarController(ArrayList<Media> mediaArrayList, Node searchPrompt){
        Searcher searcher = new Searcher(mediaArrayList);
        searcher.interact(searchPrompt);
        this.xPos = searcher.getXResults();
        this.yPos = searcher.getYResults();
        this.results = xPos.size();
    }

}
