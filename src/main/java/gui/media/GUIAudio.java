package gui.media;

import app.controllers.ToolBarController;
import app.media.MediaAudio;
import app.media.MediaHyperlink;
import gui.error_window.ErrorWindow;
import gui.model.GUIPlayerModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import storage.FileLoaderWriter;
import storage.Storage;
import java.net.*;
import java.util.ArrayList;

/**
 *  GUI for playing an associated MediaAudio
 *  <p>
 *  Parameter audioPlayer is the associated JavaFX.MediaPlayer - The effective backend of the interface
 */
public class GUIAudio extends GUIMedia<MediaAudio> implements Playable{

    private MediaPlayer audioPlayer;
    private VBox timestamps;

    private PlayerInterface playerUI;
    private double defaultVolume;
    private GUIPlayerModel playerManipulator;

    public GUIAudio(MediaAudio audio)   {
        super(audio);
        initializeMediaPlayer();
        //Waiting for MediaPlayer to be ready because otherwise some functions will not work
        audioPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                try {
                    playerUI = new PlayerInterface(audio.getName());
                    configureUI();
                    createInterface();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Places the associated MediaAudio's audio file into a player for playing
     */
    private void initializeMediaPlayer()  {
        //Initializes the main MediaPlayer that parts of the interface will use
        String name = "id" + Double.toString(getMedia().getID());
        Storage fw = new FileLoaderWriter();

        try {
            URI tempFile = fw.writeFile(name, getMedia().getRawData()); //Creating temp file for use by javafx.Media Class
            Media audioMedia = new Media(tempFile.toString());
            this.audioPlayer = new MediaPlayer(audioMedia);
            this.defaultVolume = this.audioPlayer.getVolume();
        } catch (Exception e) {
            new ErrorWindow(this, "Could not write temp file", "There was a runtime error while" +
                    " loading your file", e).show();
        }
    }

    /**
     * Configures all the controls for UI elements of the associated PlayerInterface
     */
    protected void configureUI()  {
        playerManipulator = new GUIPlayerModel(this, audioPlayer.getTotalDuration());

        configurePlayButtons();
        configureAudioSlider();
        configurePlaybackSlider();
        configurePlayRateOptions();

        //Makes it so when the player is playing, playback slider and playback text update accordingly
        this.audioPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                double percentElapsed = newValue.toMillis() / audioPlayer.getTotalDuration().toMillis();
                playerUI.getPlaybackSlider().setValue(percentElapsed);
                playerManipulator.updatePlaybackText(newValue);
            }
        });

        //Allow play button to restart player when it reaches the end
        this.audioPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                playerUI.getPlaybackSlider().setValue(1); //For some reason the listener won't update properly on short files
                playerUI.getPlay().setText("Play");
            }
        });
    }

    protected void createInterface() throws Exception {
        //Creates the overall interface allowing for playing MediaAudio and manipulating it
        createTimestamps();

        VBox layout = new VBox();
        layout.setSpacing(5);
        layout.getChildren().addAll(playerUI, timestamps);

        this.getChildren().setAll(layout);
    }

    /**
     * Defines actions when the rewind, play, and fast-forward buttons are played
     */
    private void configurePlayButtons()    {
        playerUI.getPlay().setOnAction(e -> {
            playerManipulator.firedPlayButton(playerUI.getPlay().getText());
            echoClick();
        });
        playerUI.getForward().setOnAction(e -> {
            //For some reason, without the following line this method just doesn't work sometimes and there isn't any
            //rhyme or reason as to why, though my readings indicate mediaPlayer.seek() can be inaccurate
            playerManipulator.playbackSliderAdjusted(1, audioPlayer.getStatus());
            playerUI.getPlaybackSlider().setValue(1);
            playerManipulator.firedPlayButton("Pause");
            echoClick();
        });

        playerUI.getRedo().setOnAction(e -> {
            playerManipulator.playbackSliderAdjusted(0, audioPlayer.getStatus());
            playerManipulator.firedPlayButton("Play"); //Ensures the player plays
            echoClick();
        });
    }

    /**
     * Defines what occurs when the play rate options box is interacted with
     */
    private void configurePlayRateOptions() {
        playerUI.getPlayRateOptions().setOnAction(e ->{
            playerManipulator.changePlayRate((playerUI.getPlayRateOptions().getSelectionModel().
                    getSelectedIndex() + 1) * 0.5);
            echoClick();
        });
    }

    /**
     * Configures playback slider of the player to change as the audio slider is interacted with
     */
    private void configureAudioSlider()   {
        playerUI.getAudioSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                playerManipulator.changeVolume(defaultVolume * (double) newValue);
                echoClick();
            }
        });
    }

    /**
     * Configures playback slider so that it can change where the player is playing from
     */
    private void configurePlaybackSlider()    {
        playerUI.getPlaybackSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(playerUI.getPlaybackSlider().isPressed())    {
                    playerManipulator.playbackSliderAdjusted((Double) newValue, audioPlayer.getStatus());
                    echoClick();
                }
            }
        });
    }

    /**
     * Creates a vertical list of timestamps that can be clicked to move where the player is playing
     * @throws Exception when GUIHyperlink is unable to be made
     */
    public void createTimestamps() throws Exception {
        VBox alignment = new VBox();
        alignment.setSpacing(5);
        ToolBarController hyperlinkMaker = new ToolBarController();

        //Creating UI for each timestamp
        for(Duration timestamp: this.getMedia().getTimestamps())   {
            MediaHyperlink newTimestamp = hyperlinkMaker.createHyperlink(playerManipulator.formatTime(timestamp),
                    timestamp.toString());
            //Although this throws an exception, it should never actually occur
            GUIHyperlink hyperlinkUI = (GUIHyperlink) GUIMediaFactory.getFor(newTimestamp);
            hyperlinkUI.getHyperlink().setOnAction(e -> {
                //Play first because of an odd bug with clicking after player ends
                playerManipulator.firedPlayButton("Play");
                playerManipulator.playbackSliderAdjusted(timestamp.toMillis() /
                                audioPlayer.getTotalDuration().toMillis(),
                        getAudioPlayer().getStatus());
                //Doing it twice because the player being in READY status causes some really bizarre bugs for no reason
                if (audioPlayer.getStatus() == MediaPlayer.Status.READY) {
                    playerManipulator.firedPlayButton("Play");
                }});
            hyperlinkUI.setManaged(true);
            alignment.getChildren().add(hyperlinkUI);
        }
        this.timestamps = alignment;
    }

    /**
     * Echoes the fact that the associated GUIAudio was clicked, since player controls seemingly consume the
     * Mouse Click event, this method allows the click to be echoed
     */
    public void echoClick() {
        this.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true,
                true, true, true, true, true,
                true, true, null));
    }


    /**
     * Notifies that the interface for timestamps must be updated
     * <p>
     * Preconditions: media is the same media represented by this GUIAudio
     * @param media Media object that was just updated
     */
    @Override
    public void mediaUpdated(app.media.Media media)   {
        //

        //Since timestamps can be both added and removed, we recreate the entire timestamp box
        try {
            createInterface();
        } catch (Exception e) {
            new ErrorWindow(this, "There was an error updating timestamps",
                    "The updates to this audio's timestamps could not be made", e);
        }
    }

    @Override
    public void removed()   {
        playerManipulator.firedPlayButton("Pause");
    }

    @Override
    public void setPlayerDuration(Duration time) {
        audioPlayer.seek(time);
    }

    @Override
    public void play() {
        audioPlayer.play();
    }

    @Override
    public void pause() {
        audioPlayer.pause();
    }

    @Override
    public Duration getCurrentDuration() {
        return audioPlayer.getCurrentTime();
    }

    @Override
    public void setPlaybackText(String text) {
        playerUI.getPlaybackText().setText(text);
    }

    @Override
    public void setPlaybackRate(double value) {
        audioPlayer.setRate(value);
    }

    @Override
    public void setPlaybackSliderValue(double value) {
        playerUI.getPlaybackSlider().setValue(value);
    }

    @Override
    public void setPlayButtonText(String text) {
        playerUI.getPlay().setText(text);
    }

    @Override
    public void setPlayerVolume(double value) {
        this.audioPlayer.setVolume(value);
    }

    public MediaPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void setPlayerUI(PlayerInterface playerUI) {
        this.playerUI = playerUI;
    }

    public ArrayList<String> getTimestampsText() {
        ArrayList<String> hyperlinks = new ArrayList<>();
        for (Node hyperlink: timestamps.getChildren()) {
            hyperlinks.add(hyperlink.toString());
        }
        return hyperlinks;
    }

    public void setPlayerManipulator(GUIPlayerModel playerManipulator) {
        this.playerManipulator = playerManipulator;
    }

    public VBox getTimestamps() {
        return timestamps;
    }

    public Text getPlaybackText() {
        return playerUI.getPlaybackText();
    }

    public PlayerInterface getPlayerUI() {
        return playerUI;
    }
}

/**
 * Represents the GUI elements compromising a media player
 */
class PlayerInterface extends VBox {
    private final Button play, redo, forward;
    private final Slider playbackSlider, audioSlider;
    private final ComboBox<String> playRateOptions;
    private final Text audioLabel, playbackText;


    public PlayerInterface(String name)    {
        //Creating visual elements related to managing play state of the mediaplayer
        this.play = new Button("Play"); //TODO: make these use assets
        this.redo = new Button("Replay");
        this.forward = new Button("Fast Forward"); //A bit useless but it's for visual effect

        //Creating visual elements related to the play state of the mediaplayer
        this.playbackSlider = new Slider(0, 1, 0);
        playbackSlider.setPrefWidth(360);
        this.playbackText = new Text("00:00:00");

        //Creating visual elements related to the settings for the mediaplayer
        this.playRateOptions = new ComboBox<>();
        playRateOptions.getItems().addAll("0.5x", "1x", "1.5x", "2x");

        //Setting the default selection to the current desired playback rate
        playRateOptions.getSelectionModel().select(1);
        playRateOptions.setPrefWidth(70);

        this.audioSlider = new Slider(0, 1, 1);
        audioSlider.setPrefWidth(80);

        this.audioLabel = new Text(name.substring(0, name.length() - 4));
        compileLayout();
    }

    /**
     * Compiles all UI elements into 1 layout
     */
    public void compileLayout() {
        HBox playManager = new HBox();
        playManager.getChildren().addAll(redo, play, forward);
        playManager.setSpacing(10);
        playManager.setAlignment(Pos.CENTER);

        HBox playSettingsBox = new HBox();
        playSettingsBox.getChildren().addAll(playbackSlider, playbackText);
        playSettingsBox.setSpacing(10);
        playSettingsBox.setAlignment(Pos.CENTER);

        VBox playBox = new VBox();
        playBox.getChildren().addAll(playManager, playSettingsBox);
        playBox.setSpacing(20);

        HBox bottomBox = new HBox();
        bottomBox.getChildren().addAll(playRateOptions, audioLabel, audioSlider);
        bottomBox.setSpacing(80);
        bottomBox.setAlignment(Pos.CENTER);

        //Overall layout of the player
        VBox playerLayout = new VBox();
        playerLayout.getChildren().addAll(playBox, bottomBox);
        playerLayout.setSpacing(7.5);
        playerLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        playerLayout.setPadding(new Insets(7, 12, 7, 12));
        getChildren().add(playerLayout);
    }

    public Text getPlaybackText() {
        return playbackText;
    }

    public Slider getPlaybackSlider() {
        return playbackSlider;
    }

    public Button getForward() {
        return forward;
    }

    public Button getPlay() {
        return play;
    }

    public Button getRedo() {
        return redo;
    }

    public ComboBox<String> getPlayRateOptions() {
        return playRateOptions;
    }

    public Slider getAudioSlider() {
        return audioSlider;
    }
}
