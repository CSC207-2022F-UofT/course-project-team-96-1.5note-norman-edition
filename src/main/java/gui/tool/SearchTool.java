package gui.tool;

import app.controllers.SearchBarController;
import app.controllers.ToolBarController;
import gui.media.GUIMedia;
import gui.page.Page;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.event.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

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
        public SearchSettings(){

            Button searchButton = new Button("Search");
            String resultStatement = "Results Found:";
            Label results = new Label(resultStatement);
            TextField searchBar = new TextField();


            searchButton.setOnAction(e->{
                SearchBarController sb = new SearchBarController(page.getMedia(), searchBar);
                results.setText(resultStatement + " " + sb.results);

            });


            int PADDING = 5;

            HBox searching = new HBox(PADDING, new Label(""), searchBar, searchButton);
            HBox searchResult = new HBox(PADDING, new Label(""), results);
            HBox rows[] = new HBox[]{searching, searchResult};

            for (HBox row: rows) {
                ((Label) row.getChildren().get(0)).setMinWidth(0);
                HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);
                row.setSpacing(PADDING);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPrefWidth(270);
            }
            getChildren().addAll(new VBox(PADDING, rows));

        }

    }
}
