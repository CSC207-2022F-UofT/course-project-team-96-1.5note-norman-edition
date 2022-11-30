package gui.tool.app.controllers;

import gui.tool.app.interaction_managers.Searcher;
import gui.tool.app.media.Media;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class SearchBarController {

    public ArrayList<Double> xPos;
    public ArrayList<Double> yPos;

    public int results;
    public SearchBarController(ArrayList<Media> mediaArrayList, TextField searchPrompt){
        Searcher searcher = new Searcher(mediaArrayList);
        searcher.interact(searchPrompt);
        this.xPos = searcher.getXResults();
        this.yPos = searcher.getYResults();
        this.results = xPos.size();
    }
}
