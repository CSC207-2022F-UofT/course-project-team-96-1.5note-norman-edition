package gui.media;

import app.media.MediaAudio;
import app.media.MediaHyperlink;
import app.media_managers.TextModifier;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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

public class GUIAudio extends GUIMedia<MediaAudio>{
    /**
     * Interface for audio imported by user
     * Instance Attributes:
     *  -audioPlayer: Manages all operations involving playing audio
     *  -timestamps: A box of timestamps linked to audioPlayer
     *  -playbackSlider: Slider displays the current time elapsed on the audio player, can also change current duration
     *  -playbackText: Text displays current time elapsed on the audio player
     *  -defaultVolume: The base volume as defined by the associated audio file
     */

    private MediaPlayer audioPlayer;
    private VBox timestamps;
    private Button playButton;
    private Slider playbackSlider;

    private Text playbackText;

    private double defaultVolume;


    public GUIAudio(MediaAudio audio)   {
        super(audio);
        this.getChildren().add(createInterface());

    }

    public void initializeMediaPlayer()  {
        //Initializes the main MediaPlayer that parts of the interface will use
        String path = "temp\\id" + Double.toString(getMedia().getID());

        Storage fw = new FileLoaderWriter();
        /**
         * Manages creation/interactions on MediaAudio based on Menubars/Toolbars
         * Instance Attributes:
         * - timestamp: used to add a timestamp to an audio
         * - audio: audio to be modified
         * - page: The page where the audio exists/will exist on
         */
        URI tempFile = fw.writeFile(path, getMedia().getRawData()); //Creating temp file for use by javafx.Media Class

        Media audioMedia = new Media(tempFile.toString());
        this.audioPlayer = new MediaPlayer(audioMedia);

        this.defaultVolume = this.audioPlayer.getVolume();
    }

    public VBox createInterface()   {
        //Creates the overall interface allowing for playing MediaAudio and manipulating it
        initializeMediaPlayer();

        //Creating visual elements related to the play state of the mediaplayer
        createPlayButton();
        createPlaybackSlider();
        HBox playBox = new HBox();
        this.playbackText = new Text();
        updatePlaybackText();
        playBox.getChildren().addAll(playButton, playbackSlider, playbackText);
        playBox.setSpacing(10); //temp

        //Creating visual elements related to the settings for the mediaplayer
        ComboBox<String> playbackRate = createPlayRateOptions();
        Slider volumeSlider = createAudioSlider();
        HBox settings = new HBox();
        settings.getChildren().addAll(playbackRate, volumeSlider);
        settings.setSpacing(10);

        configurePlayer();

        HBox hLayout = new HBox();
        hLayout.getChildren().addAll(playBox, settings);
        hLayout.setSpacing(40);
        hLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        hLayout.setPadding(new Insets(7, 7, 7, 7));

        createTimestamps();

        VBox layout = new VBox();
        layout.getChildren().addAll(hLayout, this.timestamps);
        layout.setSpacing(20);
        return layout;
    }

    public void configurePlayer()  {
        //Creates configuration for the audioplayer neccessary for various UI elements
        this.audioPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                double totalDuration = audioPlayer.getTotalDuration().toSeconds();
                double percentElapsed = audioPlayer.getCurrentTime().toSeconds() / totalDuration;
                playbackSlider.setDisable(true);
                playbackSlider.setValue(percentElapsed);
                playbackSlider.setDisable(false);
                updatePlaybackText();
            }
        });

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
            if (play.getText().equals("Play"))  {
                //Checking if the player is at the end of the audio
                if(audioPlayer.getCurrentTime().equals(audioPlayer.getTotalDuration())) {
                    audioPlayer.seek(audioPlayer.getStartTime());
                }
                this.audioPlayer.play();
                play.setText("Pause");
            }   else {
                //If the player is playing, we can pause it instead
                this.audioPlayer.pause();
                play.setText("Play");
            }
        });
        this.playButton = play;
    }

    public ComboBox<String> createPlayRateOptions() {
        //Creates a combobox giving user options to change playrate of audio

        ComboBox<String> playRateOptions = new ComboBox<>();
        playRateOptions.getItems().addAll("0.5x", "1x", "1.5x", "2x");

        //Setting the default selection to the current desired playback rate
        double rate = this.audioPlayer.getRate();
        playRateOptions.getSelectionModel().select((int) (rate / 0.5) - 1);

        playRateOptions.setOnAction(e ->{
            double selectedRate = (playRateOptions.getSelectionModel().getSelectedIndex() + 1) * 0.5;
            this.audioPlayer.setRate(selectedRate);
        });
        return playRateOptions;
    }

    public Slider createAudioSlider()   {
        //Creates a slider to adjust audio that can work as it's playing

        MediaPlayer audioPlayer = this.audioPlayer;
        double defaultVolume = this.defaultVolume;

        Slider audioSlider = new Slider(0, 1, 1); //Due to MediaPlayer implementation, raising volume above default is impossible

        //Allowing slider to update volume of the player in live time
        audioSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                audioPlayer.setVolume(defaultVolume * (double) newValue);
            }
        });
        return audioSlider;
    }

    public void createPlaybackSlider()    {
        //Creates a slider that tracks current duration of the player and can change the time it's at
        MediaPlayer audioPlayer = this.audioPlayer;
        Slider playbackSlider = new Slider(0, 1, 0);
        playbackSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(playbackSlider.isPressed())    {
                    double totalDuration = audioPlayer.getTotalDuration().toSeconds();
                    audioPlayer.seek(Duration.seconds((double) newValue * totalDuration));
                    updatePlaybackText();
                }
            }
        });
        this.playbackSlider = playbackSlider;
    }

    public void updatePlaybackText()    {
        //updates playback text to reflect the live state of the MediaPlayer

        int seconds = (int) audioPlayer.getCurrentTime().toSeconds() % 60;
        int minutes = (int) audioPlayer.getCurrentTime().toMinutes() % 60;
        int hours = (int) audioPlayer.getCurrentTime().toHours();
        String[] timeProperties = {Integer.toString(seconds), Integer.toString(minutes), Integer.toString(hours)};
        for (int i = 0; i < timeProperties.length; i++)   { //Adding a zero in front of the digit for consistency with time formats
            if (timeProperties[i].length() == 1) {
                timeProperties[i] = "0" + timeProperties[i];
            }
        }

        this.playbackText.setText(timeProperties[2] + ":" + timeProperties[1] + ":" + timeProperties[0]);
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
    public void mediaUpdated(app.media.Media media) {
        createTimestamps();
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

    public VBox getTimestamps() {
        return timestamps;
    }

    public Text getPlaybackText() {
        return playbackText;
    }
}
