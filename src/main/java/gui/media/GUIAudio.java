package gui.media;

import app.controllers.ToolBarController;
import app.media.MediaAudio;
import app.media.MediaHyperlink;
import app.media_managers.TextModifier;
import com.sun.media.jfxmedia.events.PlayerEvent;
import gui.error_window.ErrorWindow;
import gui.view_controllers.MediaPlayerController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

public class GUIAudio extends GUIMedia<MediaAudio> implements Playable{

    private URI tempFile;
    private MediaPlayer audioPlayer;
    private VBox timestamps;
    private Button playButton;
    private Slider playbackSlider;

    private Text playbackText;
    private double defaultVolume;
    private MediaPlayerController controller;
    private VBox playerLayout;


    public GUIAudio(MediaAudio audio)   {
        super(audio);

        initializeMediaPlayer();

        //Waiting for mediaplayer to be ready to call neccessary methods
        audioPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                try {
                    createInterface();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public void createInterface() throws Exception {
        //Creates the overall interface allowing for playing MediaAudio and manipulating it
        controller = new MediaPlayerController(this, audioPlayer.getTotalDuration());

        //Ensures that player is only built once
        if (playButton == null) {
            initializePlayerUI();
        }

        createTimestamps();

        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.getChildren().addAll(playerLayout, timestamps);

        this.getChildren().setAll(layout);
    }

    public void initializeMediaPlayer()  {
        //Initializes the main MediaPlayer that parts of the interface will use
        String name = "id" + Double.toString(getMedia().getID());

        Storage fw = new FileLoaderWriter();

        try {
            this.tempFile = fw.writeFile(name, getMedia().getRawData()); //Creating temp file for use by javafx.Media Class
            Media audioMedia = new Media(this.tempFile.toString());
            this.audioPlayer = new MediaPlayer(audioMedia);

            this.defaultVolume = this.audioPlayer.getVolume();
        } catch (Exception e) {
            new ErrorWindow(this, "Could not write temp file", "There was a runtime error while" +
                    " loading your file", e).show();
        }
    }

    public void configurePlayer()  {
        //Creates configuration for the audioplayer neccessary for various UI elements

        //Makes it so when the player is playing, playback slider and playback text update accordingly
        this.audioPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                double percentElapsed = newValue.toMillis() / audioPlayer.getTotalDuration().toMillis();
                playbackSlider.setValue(percentElapsed);
                controller.changePlaybackText(newValue);
            }
        });

        //Allow play button to restart player when it reaches the end
        this.audioPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                playButton.setText("Play");
            }
        });
    }

    private void initializePlayerUI() {
        //Initializes the player, which only occurs when GUIAudio is first made

        //Creating visual elements related to managing play state of the mediaplayer
        createPlayButton(); //TODO: make these use assets
        Button redo = new Button("Replay");
        redo.setOnAction(e -> {
            controller.changePlayback(0, audioPlayer.getStatus());
            controller.firePlayButton("Play"); //Ensures the player plays
        });

        Button forward = new Button("Fast Forward"); //A bit useless but it's for visual effect
        forward.setOnAction(e -> {
            //For some reason, without the following line this method just doesn't work sometimes and there isn't any
            //rhyme or reason as to why, though my readings indicate mediaPlayer.seek() can be inaccurate
            controller.changePlayback(1, audioPlayer.getStatus());
            playbackSlider.setValue(1);
            controller.firePlayButton("Pause");
        });

        HBox playManager = new HBox();
        playManager.getChildren().addAll(redo, playButton, forward);
        playManager.setSpacing(10);
        playManager.setAlignment(Pos.CENTER);

        //Creating visual elements related to the play state of the mediaplayer
        createPlaybackSlider();
        playbackSlider.setPrefWidth(360);
        this.playbackText = new Text();
        controller.changePlaybackText(new Duration(0));

        HBox playSettingsBox = new HBox();
        playSettingsBox.getChildren().addAll(playbackSlider, playbackText);
        playSettingsBox.setSpacing(10);
        playSettingsBox.setAlignment(Pos.CENTER);

        VBox play = new VBox();
        play.getChildren().addAll(playManager, playSettingsBox);
        play.setSpacing(20);

        //Creating visual elements related to the settings for the mediaplayer
        ComboBox<String> playbackRate = createPlayRateOptions();
        playbackRate.setPrefWidth(70);

        Slider volumeSlider = createAudioSlider();
        volumeSlider.setPrefWidth(80);

        Text audioLabel = new Text(getMedia().getName());

        HBox bottomBox = new HBox();
        bottomBox.getChildren().addAll(playbackRate, audioLabel, volumeSlider);
        bottomBox.setSpacing(80);
        bottomBox.setAlignment(Pos.CENTER);

        //Overall layout of the player

        VBox layout = new VBox();
        layout.getChildren().addAll(play, bottomBox);
        layout.setSpacing(7.5);
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setPadding(new Insets(7, 7, 7, 7));

        playerLayout = layout;

        configurePlayer();
    }


    public void createPlayButton()    {
        //Creates a button that allows pausing/playing the audio
        Button play = new Button("Play");
        play.setOnAction(e -> {
            controller.firePlayButton(play.getText());
            echoClick();
        });
        this.playButton = play;
    }

    public ComboBox<String> createPlayRateOptions() {
        //Creates a combobox giving user options to change playrate of audio

        ComboBox<String> playRateOptions = new ComboBox<>();
        playRateOptions.getItems().addAll("0.5x", "1x", "1.5x", "2x");

        //Setting the default selection to the current desired playback rate
        double rate = audioPlayer.getRate();
        playRateOptions.getSelectionModel().select((int) (rate / 0.5) - 1);

        playRateOptions.setOnAction(e ->{
            controller.changePlayRate((playRateOptions.getSelectionModel().getSelectedIndex() + 1) * 0.5);
            echoClick();
        });
        return playRateOptions;
    }

    public Slider createAudioSlider()   {
        //Creates a slider to adjust audio that can work as it's playing

        //Due to MediaPlayer implementation, raising volume above default is impossible
        Slider audioSlider = new Slider(0, 1, 1);

        //Allowing slider to update volume of the player in live time
        audioSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                controller.changePlayVolume(defaultVolume * (double) newValue);
                echoClick();
            }
        });
        return audioSlider;
    }

    public void createPlaybackSlider()    {
        //Creates a slider that tracks current duration of the player and can change the time it's at
        MediaPlayer audioPlayer = this.audioPlayer;
        Slider playbackSlider = new Slider(0, 1, 0);

        //Allowing slider to change where the player is playing from
        playbackSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(playbackSlider.isPressed())    {
                    controller.changePlayback((Double) newValue, audioPlayer.getStatus());
                    echoClick();
                }
            }
        });
        this.playbackSlider = playbackSlider;
    }

    public void createTimestamps() throws Exception {
        //Creates a vertical "list" of timestamps that can be clicked to move where the player is playing
        VBox alignment = new VBox();
        alignment.setSpacing(10);
        ToolBarController hyperlinkMaker = new ToolBarController();

        //Creating UI for each timestamp
        for(Duration timestamp: this.getMedia().getTimestamps())   {
            MediaHyperlink newTimestamp = hyperlinkMaker.createHyperlink(controller.createFormattedTime(timestamp),
                    timestamp.toString());
            //Although this throws an exception, it should never actually occur
            GUIHyperlink hyperlinkUI = (GUIHyperlink) GUIMediaFactory.getFor(newTimestamp);
            hyperlinkUI.getHyperlink().setOnAction(e -> {
                //Play first because of an odd bug with clicking after player ends
                controller.firePlayButton("Play");
                controller.changePlayback(timestamp.toMillis() / audioPlayer.getTotalDuration().toMillis(),
                        getAudioPlayer().getStatus());
            });
            alignment.getChildren().add(hyperlinkUI);
        }
        this.timestamps = alignment;
    }

    public void echoClick() {
        //Echoes the fact that the associated GUIAudio was clicked
        //Since player controls seemingly consume the Mouse Click event, this method allows the click to be echoed

        this.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true,
                true, true, true, true, true,
                true, true, null));
    }

    @Override
    public void mediaUpdated(app.media.Media media)   {
        //Preconditions: media is the same media represented by this GUIAudio

        //Since timestamps can be both added and removed, we recreate the entire timestamp box
        try {
            createInterface();
        } catch (Exception e) {
            throw new RuntimeException(e); //TODO: Error window
        }
    }

    @Override
    public void removed()   {
        controller.firePlayButton("Pause");
    }

    @Override
    public void setPlayerDuration(Duration time) {
        this.audioPlayer.seek(time);
    }

    @Override
    public void play() {
        this.audioPlayer.play();
    }

    @Override
    public void pause() {
        this.audioPlayer.pause();
    }

    @Override
    public Duration getCurrentDuration() {
        return this.audioPlayer.getCurrentTime();
    }

    @Override
    public void setPlaybackText(String text) {
        this.playbackText.setText(text);
    }

    @Override
    public void setPlaybackRate(double value) {this.audioPlayer.setRate(value);}
    @Override
    public void setPlaybackSliderValue(double value) {
        this.playbackSlider.setValue(value);
    }

    @Override
    public void setPlayButtonText(String text) {
        this.playButton.setText(text);
    }

    @Override
    public void setPlayerVolume(double value) {
        this.audioPlayer.setVolume(value);
    }

    public MediaPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Slider getPlaybackSlider() {
        return playbackSlider;
    }

    public Text getPlaybackText() {
        return playbackText;
    }

    public MediaPlayerController getController() {
        return controller;
    }

    public ArrayList<String> getTimestampsText() {
        ArrayList<String> hyperlinks = new ArrayList<>();
        for (Node hyperlink: timestamps.getChildren()) {
            hyperlinks.add(hyperlink.toString());
        }
        return hyperlinks;
    }

    public void setController(MediaPlayerController controller) {
        this.controller = controller;
    }

    public VBox getTimestamps() {
        return timestamps;
    }
}
