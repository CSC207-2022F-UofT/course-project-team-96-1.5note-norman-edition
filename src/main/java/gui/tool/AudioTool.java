package gui.tool;

import app.controllers.ToolBarController;
import gui.media.GUIAudio;
import gui.media.GUIMedia;
import gui.page.Page;
import gui.page.PageEventHandler;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.w3c.dom.events.EventTarget;

import java.util.ArrayList;

public class AudioTool implements Tool{
    private Page page;
    private HandlerMethod[] handlers;

    private GUIAudio selectedPlayer;
    private Button timestampButton, deleteButton;

    private ComboBox<String> timestamps;

    public AudioTool()  {
        this.handlers = new HandlerMethod[]{new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::setSelectedAudio)};
    }

    public void setSelectedAudio(MouseEvent e) {
        e.consume();
        javafx.event.EventTarget target = e.getTarget();
        if (target instanceof  GUIAudio || ((Node) target).getParent() instanceof GUIAudio) {

            selectedPlayer = (GUIAudio) target;

            ArrayList<String> playerTimestamps = selectedPlayer.getTimestamps();

            timestamps.getItems().setAll(timestamps.getItems().get(0));
            timestamps.getItems().addAll(playerTimestamps);
            timestamps.setDisable(playerTimestamps.isEmpty());

            timestampButton.setDisable(playerTimestamps.contains(selectedPlayer.getPlaybackText().getText()));

            deleteButton.setDisable(true);
        }   else {
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
    public String getName() {return "Import Audio";}

    @Override
    public Node getSettingsGUI()    {

        ToolBarController tbc = new ToolBarController();

        Button add = new Button("Add");
        add.setOnAction(e ->    {
            tbc.insertAudio(this.page);
        });

        timestampButton = new Button("Create timestamp");
        timestampButton.setDisable(true);

        timestampButton.setOnAction(e ->    {
            tbc.modifyTimestamp(selectedPlayer.getMedia(), selectedPlayer.getCurrentDuration(), page);
            simulateCLick();
        });

        timestamps = new ComboBox<>();
        timestamps.getItems().add("Select a timestamp:");
        timestamps.setDisable(true);
        timestamps.getSelectionModel().select(0);

        timestamps.setOnAction(e -> {
            deleteButton.setDisable(timestamps.getSelectionModel().getSelectedIndex() == 0);
        });

        deleteButton = new Button("Delete Selected Timestamp");
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e ->   {
            int selectedTime = timestamps.getSelectionModel().getSelectedIndex() - 1;
            Duration selectedDuration = selectedPlayer.getMedia().getTimestamps().get(selectedTime);
            tbc.modifyTimestamp(selectedPlayer.getMedia(), selectedDuration, page);
            simulateCLick();
        });

        VBox layout = new VBox();
        layout.getChildren().addAll(add, timestampButton, timestamps, deleteButton);
        return layout;
    }

    public void simulateCLick() {
        selectedPlayer.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                true, true, true, true, true,
                true, true, true, true, true,
                null));
    }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }
}
