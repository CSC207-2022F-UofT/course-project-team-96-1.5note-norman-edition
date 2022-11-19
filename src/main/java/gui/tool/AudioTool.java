package gui.tool;

import app.controllers.ToolBarController;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

public class AudioTool implements Tool{
    private Page page;
    private HandlerMethod[] handlers;

    private GUIAudio selectedPlayer;
    private Button timestampButton, deleteButton, add;

    private ComboBox<String> timestamps;

    private Text audioLabel;

    public AudioTool()  {
        this.handlers = new HandlerMethod[]{new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::setSelectedAudio)};
    }

    public void setSelectedAudio(MouseEvent e) {
        e.consume();
        javafx.event.EventTarget target = e.getTarget();

        //Checking if the player clicked something related to a GUIAudio
        if (target instanceof  GUIAudio || ((Node) target).getParent() instanceof GUIAudio) {

            selectedPlayer = (GUIAudio) target;

            ArrayList<String> playerTimestamps = selectedPlayer.getTimestamps();

            audioLabel.setText("Managing " + selectedPlayer.getMedia().getName());

            timestamps.getItems().setAll(timestamps.getItems().get(0));
            timestamps.getItems().addAll(playerTimestamps);
            timestamps.setDisable(playerTimestamps.isEmpty());

            timestampButton.setDisable(playerTimestamps.contains(selectedPlayer.getPlaybackText().getText()));

            deleteButton.setDisable(true);
        }   else {
            audioLabel.setText("Please Select an Audio Player");

            selectedPlayer = null;
            timestampButton.setDisable(true);
            deleteButton.setDisable(true);
        }
        timestamps.getSelectionModel().select(0);
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public String getName() {return "Manage Audio";}

    @Override
    public Node getSettingsGUI()    {
        //Creates the tool pane the user interacts with
        initializeControls();

        HBox deletionBox = new HBox();
        deletionBox.setSpacing(20);
        deletionBox.getChildren().addAll(timestamps, deleteButton);

        VBox timestampLayout = new VBox();
        timestampLayout.setSpacing(20);
        timestampLayout.getChildren().addAll(audioLabel, timestampButton, deletionBox);

        VBox layout = new VBox();
        layout.setSpacing(30);

        layout.getChildren().addAll(add, timestampLayout);
        return layout;
    }

    public void initializeControls()    {
        //Initializes UI controls contained in this tool

        ToolBarController tbc = new ToolBarController();

        add = new Button("Add new Audio");
        add.setOnAction(e ->    {
            tbc.insertAudio(this.page);
        });

        //Controls pertaining to creating timestamps

        audioLabel = new Text("Please Select an Audio Player");

        timestampButton = new Button("Create timestamp");
        timestampButton.setDisable(true);

        timestampButton.setOnAction(e ->    {
            tbc.modifyTimestamp(selectedPlayer.getMedia(), selectedPlayer.getCurrentDuration(), page);
            selectedPlayer.echoClick();
        });

        timestamps = new ComboBox<>();
        timestamps.getItems().add("Select a timestamp:");
        timestamps.setDisable(true);
        timestamps.getSelectionModel().select(0);

        timestamps.setOnAction(e -> {
            deleteButton.setDisable(timestamps.getSelectionModel().getSelectedIndex() == 0);
        });

        deleteButton = new Button("Delete");
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e ->   {
            int selectedTime = timestamps.getSelectionModel().getSelectedIndex() - 1;
            Duration selectedDuration = selectedPlayer.getMedia().getTimestamps().get(selectedTime);
            tbc.modifyTimestamp(selectedPlayer.getMedia(), selectedDuration, page);
            selectedPlayer.echoClick();
        });
    }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }
}
