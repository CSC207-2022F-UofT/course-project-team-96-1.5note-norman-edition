package gui.tool;
import app.controllers.ToolBarController;
import gui.error_window.ErrorWindow;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * GUI for adding or modifying audio on the page
 */

public class AudioTool implements Tool{
    private Page page;
    private final HandlerMethod<MouseEvent>[] handlers;
    private GUIAudio selectedPlayer;
    private final AudioSettings settings;

    public AudioTool(String type)  {
        this.handlers = new HandlerMethod[]{new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::setSelectedAudio)};
        this.settings = new AudioSettings(type);
        initializeControls();
    }

    /** Defines how GUIAudio on the page can either be selected or de-selected
     * <p>
     * If any visual component of a GUIAudio is clicked on the page, it is classified as selected. If anything that
     * is not an instance of GUIAudio is clicked on the page, then no GUIAudio is considered selected
     *
     * @param e Any form of MouseEvent detecting on the associated page
     */

    public void setSelectedAudio(MouseEvent e) {
        e.consume();
        javafx.event.EventTarget target = e.getTarget();

        //Checking if the player clicked something related to a GUIAudio
        if (target instanceof  GUIAudio || ((Node) target).getParent() instanceof GUIAudio) {
            assert target instanceof GUIAudio; //This will always pass by the time we get here
            selectedPlayer = (GUIAudio) target;
            setInterface();
        }   else {
            //Disabling parts of the interface linked to timestamps
            resetInterface("Please Select an Audio Player");
        }
        //Ensures selection box is never empty
        settings.getTimestampSelection().getSelectionModel().select(0);
    }

    public void setInterface()  {
        //Enabling parts of the interface linked to timestamps and configuring them to selected player

        ArrayList<String> playerTimestamps = selectedPlayer.getTimestampsText();

        settings.getAudioLabel().setText("Managing " + selectedPlayer.getMedia().getName());

        settings.getTimestampSelection().getItems().setAll(settings.getTimestampSelection().getItems().get(0));
        settings.getTimestampSelection().getItems().addAll(playerTimestamps);
        settings.getTimestampSelection().setDisable(playerTimestamps.isEmpty());

        settings.getAddTimestamp().setDisable(playerTimestamps.contains(
                selectedPlayer.getPlaybackText().getText()));

        settings.getDeleteTimestamp().setDisable(true);
    }

    public void resetInterface(String text)    {
        setAudioLabel(text);
        selectedPlayer = null;
        settings.getAddTimestamp().setDisable(true);
        settings.getDeleteTimestamp().setDisable(true);

        settings.getTimestampSelection().setDisable(true);
    }

    private void setAudioLabel(String text)  {
        settings.getAudioLabel().setText(text);
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public String getName() {return "Manage Audio";}

    @Override
    public AudioSettings getSettingsGUI()    {
        //Creates the visual tool pane the user interacts with
        return settings;
    }

    public void setSelectedPlayer(GUIAudio selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }

    /**
     * Provides functionality to the UI elements as defined in settings
     */
    public void initializeControls()    {
        ToolBarController tbc = new ToolBarController();

        configureAddAudio(tbc);
        configureAddTimestamp(tbc);
        configureTimestampSelection(tbc);
        configureDeleteTimestamp(tbc);
    }

    protected void configureAddAudio(ToolBarController tbc)   {
        //When the addAudio button is clicked, initiate MediaAudio creation process
        settings.getAddMedia().setOnAction(e ->    {
            try {
                tbc.insertAudio(this.page.getCommunicator());
            } catch (Exception ex) {
                new ErrorWindow(this.page, "There was an error loading you file",
                        "An exception occured" + "in the process of loading your file", ex);
            }
        });
    }

    private void configureAddTimestamp(ToolBarController tbc)   {
        //Controls pertaining to creating timestamps
        //When the user clicks to add a timestamp, initiate timestamp creation process
        settings.getAddTimestamp().setOnAction(e ->    {
            try {
                tbc.modifyTimestamp(selectedPlayer.getMedia(), selectedPlayer.getCurrentDuration(),
                        page.getCommunicator());
            } catch (Exception ex) {
                new ErrorWindow(this.page, "There was an error creating the timestamp",
                        "Your media was unable to be updated", ex);
            }
            //refreshes the timestamp selection box
            selectedPlayer.echoClick();
        });
    }

    private void configureTimestampSelection(ToolBarController tbc) {
        //Whenever the timestamp selection box detects change, disable the delete timestamp button if a timestamp was
        //selected
        settings.getTimestampSelection().setOnAction(e -> {
            settings.getDeleteTimestamp().setDisable(settings.getTimestampSelection().getSelectionModel().
                    getSelectedIndex() == 0);
        });

    }

    private void configureDeleteTimestamp(ToolBarController tbc)    {
        //When the deleteTimestamp button is clicked, initiate timestamp removal process
        settings.getDeleteTimestamp().setOnAction(e ->   {
            int selectedTime = settings.getTimestampSelection().getSelectionModel().getSelectedIndex() - 1;
            Duration selectedDuration = selectedPlayer.getMedia().getTimestamps().get(selectedTime);
            try {
                tbc.modifyTimestamp(selectedPlayer.getMedia(), selectedDuration, page.getCommunicator());
            } catch (Exception ex) {
                new ErrorWindow(this.page, "There was an error creating the timestamp",
                        "Your media was unable to be updated", ex);
            }
            selectedPlayer.echoClick();
        });
    }

    @Override
    public HandlerMethod<MouseEvent>[] getHandlerMethods() {
        return handlers;
    }

    public GUIAudio getSelectedPlayer() {
        return selectedPlayer;
    }

    public Page getPage() {
        return page;
    }
}

/**
 * Stores visual interface for the audio tool
 */
class AudioSettings extends VBox{
    private final Button addMedia, addTimestamp, deleteTimestamp;
    private final Text audioLabel;
    private final ComboBox<String> timestampSelection;

    /**
     * Constructor defines the entire interface for the tool, but provides no functionality
     */
    public AudioSettings(String type)  {
        this.addMedia = new Button("Add new " + type);
        this.deleteTimestamp = new Button("Delete");
        deleteTimestamp.setDisable(true);
        this.addTimestamp = new Button("Create Timestamp");
        addTimestamp.setDisable(true);

        if(type.equals("Audio"))    {
            this.audioLabel = new Text("Please select an " + type + " Player");
        }   else {
            this.audioLabel = new Text("Please select a " + type + " Player");
        }

        this.timestampSelection = new ComboBox<>();
        timestampSelection.getItems().add("Select a timestamp:");
        timestampSelection.setDisable(true);
        timestampSelection.getSelectionModel().select(0);

        HBox deletionBox = new HBox();
        deletionBox.setSpacing(20);
        deletionBox.getChildren().addAll(timestampSelection, deleteTimestamp);

        VBox timestampLayout = new VBox();
        timestampLayout.setSpacing(20);
        timestampLayout.getChildren().addAll(audioLabel, addTimestamp, deletionBox);

        setSpacing(30);
        getChildren().addAll(addMedia, timestampLayout);
    }

    public Button getAddMedia() {
        return addMedia;
    }

    public Button getAddTimestamp() {
        return addTimestamp;
    }

    public Button getDeleteTimestamp() {
        return deleteTimestamp;
    }

    public ComboBox<String> getTimestampSelection() {
        return timestampSelection;
    }

    public Text getAudioLabel() {
        return audioLabel;
    }
}
