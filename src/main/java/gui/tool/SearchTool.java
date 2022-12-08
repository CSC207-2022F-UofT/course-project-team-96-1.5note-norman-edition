package gui.tool;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import app.controllers.SearchBarController;
import app.media.Media;
import gui.ResourceLoader;
import gui.page.Page;
import gui.media.GUIMedia;

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
    public Node getGraphic() {
        return new Label(
                getName(), ResourceLoader.loadSVGicon("icons/search.svg", 15, 15));
    }

    @Override
    public FlowPane getSettingsGUI(){
        return settings;
    }

    class SearchSettings extends FlowPane{
        private ArrayList<Double> xCoords;
        private ArrayList<Double> yCoords;
        private int currentIndex;

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
                ArrayList<Media> pageMedia = new ArrayList<>();
                for (GUIMedia<?> media: page.getAllMedia()) {
                    pageMedia.add(media.getMedia());
                }
                SearchBarController sb = new SearchBarController(pageMedia, searchBar.getText());
                results.setText(resultStatement + " " + sb.results);
                if (sb.results != 0){
                    findButton.setDisable(false);
                    currentIndex = 0;
                    xCoords = sb.xPos;
                    yCoords = sb.yPos;
                }
            });

            findButton.setOnAction(e->{
                page.jumpToCenter(xCoords.get(currentIndex), yCoords.get(currentIndex));

                currentIndex += 1;
                if (currentIndex == xCoords.size()){
                    currentIndex = 0;
                }
            });


            int PADDING = 5;

            // Visually placing the searchbar, button and result statement
            HBox searching = new HBox(PADDING, searchBar, searchButton, findButton);

            searching.setSpacing(PADDING);
            searching.setAlignment(Pos.CENTER_LEFT);
            searching.setPrefWidth(280);

            // Adds to the GUI
            getChildren().add(new VBox(PADDING, searching, results));

        }

    }
}
