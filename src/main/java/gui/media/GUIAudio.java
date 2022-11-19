package gui.media;

import app.media.MediaAudio;
import app.media.MediaHyperlink;
import app.media_managers.TextModifier;
import gui.error_window.ErrorWindow;
import gui.view_controllers.MediaPlayerController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
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

public class GUIAudio extends GUIMedia<MediaAudio> implements Playable{

    private URI tempFile;
    private MediaPlayer audioPlayer;
    private VBox timestamps;
    private Button playButton;
    private Slider playbackSlider;

    private Text playbackText;
    private double defaultVolume;
    private MediaPlayerController controller;


    public GUIAudio(MediaAudio audio)   {
        super(audio);

        initializeMediaPlayer();

        //Waiting for mediaplayer to be ready to call neccessary methods
        audioPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                createInterface();
            }
        });


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

    public void createInterface()   {
        //Creates the overall interface allowing for playing MediaAudio and manipulating it
        controller = new MediaPlayerController(this, audioPlayer.getTotalDuration());

        //Creating visual elements related to the play state of the mediaplayer
        createPlayButton();
        createPlaybackSlider();
        HBox playBox = new HBox();
        this.playbackText = new Text();
        controller.changePlaybackText(new Duration(0));

        playBox.getChildren().addAll(playButton, playbackSlider, playbackText);
        playBox.setSpacing(10); //temp

        //Creating visual elements related to the settings for the mediaplayer
        ComboBox<String> playbackRate = createPlayRateOptions();
        Slider volumeSlider = createAudioSlider();
        HBox settings = new HBox();
        settings.getChildren().addAll(playbackRate, volumeSlider);
        settings.setSpacing(10);

        configurePlayer();

        HBox layout = new HBox();
        layout.getChildren().addAll(playBox, settings);
        layout.setSpacing(40);
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setPadding(new Insets(7, 7, 7, 7));

        this.getChildren().add(layout);
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

    public void createPlayButton()    {
        //Creates a button that allows pausing/playing the audio
        Button play = new Button("Play");
        play.setOnAction(e -> {
            controller.firePlayButton(play.getText());
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
                }
            }
        });
        this.playbackSlider = playbackSlider;
    }

    public void createTimestamps()  {
        //Creates a vertical "list" of timestamps that can be clicked to move where the player is playing

        VBox alignment = new VBox();
        TextModifier hyperlinkFactory = new TextModifier();
        for(Duration timestamp: this.getMedia().getTimestamps())   {
            MediaHyperlink hyperlink = hyperlinkFactory.createHyperlink(timestamp.toString(),
                    Double.toString(timestamp.toMillis()));
            GUIHyperlink hyperlinkGUI = new GUIHyperlink(hyperlink);
            hyperlinkGUI.createTimestampLink(audioPlayer);
            alignment.getChildren().add(hyperlinkGUI);
        }
        this.timestamps = alignment;
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

    public void setController(MediaPlayerController controller) {
        this.controller = controller;
    }
}
