package app.controllers;

import app.interaction_managers.Searcher;
import app.media.Media;
import javafx.scene.Node;

import java.util.ArrayList;

public class SearchBarController {
    public SearchBarController(ArrayList<Media> mediaArrayList, Node node){
        Searcher searcher = new Searcher(mediaArrayList);
        searcher.interact(node);
    }

}
