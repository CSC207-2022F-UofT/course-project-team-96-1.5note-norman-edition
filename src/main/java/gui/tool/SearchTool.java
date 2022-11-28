package gui.tool;

import app.controllers.SearchBarController;
import gui.page.Page;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class SearchTool implements Tool{

    private Page page;
    private final SearchSettings settings;

    public SearchTool(){
        settings = new SearchSettings();
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public void disabledFor(Page page) {
        this.page = null;
    }

    @Override
    public String getName(){
        return "Search";
    }

    @Override
    public FlowPane getSettingsGUI(){
        return settings;
    }

    class SearchSettings extends FlowPane{
        private ArrayList<Double> xCoords;
        private ArrayList<Double> yCoords;
        private int currentIndex;

        private int offsetX = 800;
        private int offsetY = 200;
        public SearchSettings(){
            // Creating the textfield for user input and button for searching
            Button searchButton = new Button("Search");
            String resultStatement = "Results Found:";
            Label results = new Label(resultStatement);
            TextField searchBar = new TextField();

            Button findButton = new Button("Find");
            findButton.setDisable(true);

            // When the search button is pressed, the text within the textfield is searched by using the
            // SearchBarController to call the searcher method
            searchButton.setOnAction(e->{
                SearchBarController sb = new SearchBarController(page.getMedia(), searchBar);
                results.setText(resultStatement + " " + sb.results);
                if (sb.results != 0){
                    findButton.setDisable(false);
                    currentIndex = 0;
                    xCoords = sb.xPos;
                    yCoords = sb.yPos;
                }
            });

            findButton.setOnAction(e->{
                page.jumpToPoint(xCoords.get(currentIndex) - offsetX, yCoords.get(currentIndex) - offsetY);
                currentIndex += 1;
                if (currentIndex == xCoords.size()){
                    currentIndex = 0;
                }
            });


            int PADDING = 5;

            // Visually placing the searchbar, button and result statement
            HBox searching = new HBox(PADDING, new Label(""), searchBar, searchButton, findButton);
            HBox searchResult = new HBox(PADDING, new Label(""), results);
            HBox[] rows = new HBox[]{searching, searchResult};

            for (HBox row: rows) {
                ((Label) row.getChildren().get(0)).setMinWidth(0);
                HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);
                row.setSpacing(PADDING);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPrefWidth(270);
            }
            // Adds to the GUI
            getChildren().addAll(new VBox(PADDING, rows));

        }

    }
}
