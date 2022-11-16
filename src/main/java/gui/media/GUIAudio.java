package gui.media;

import app.media.MediaAudio;
import app.media.MediaHyperlink;
import app.media_managers.TextModifier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import storage.FileLoaderWriter;
import storage.Storage;
import java.net.*;

public class GUIAudio extends GUIMedia<MediaAudio>{

    private URI tempFile;
    private MediaPlayer audioPlayer;
    private VBox timestamps;

    public GUIAudio(MediaAudio audio)   {
        super(audio);
    }

    public void initializeMediaPlayer()  {
        String path = "temp\\id" + Double.toString(getMedia().getID());

        Storage fw = new FileLoaderWriter();
        this.tempFile = fw.writeFile(path, getMedia().getRawData()); //Creating temp file for use by javafx.Media Class

        Media audioMedia = new Media(this.tempFile.toString());
        this.audioPlayer = new MediaPlayer(audioMedia);
    }

    public HBox createInterface()   {
        //Creates the overall interface allowing for playing MediaAudio and manipulating it
        initializeMediaPlayer();
        Button playButton = createPlayButton();
        ComboBox<String> playbackRate = createPlayRateOptions();
        Slider volumeSlider = createAudioSlider();

        HBox layout = new HBox();
        layout.getChildren().addAll(playButton, playbackRate, volumeSlider);
        return layout;
    }

    public Button createPlayButton()    {
        //Creates a button that allows pausing/playing the audio
        Button play = new Button("Play");
        play.setOnAction(e -> {
            if (play.getText().equals("Play"))  {
                //Checking if the player is at the end of the audio
                if(this.audioPlayer.getTotalDuration() == this.audioPlayer.getCurrentTime())    {
                    this.audioPlayer.seek(new Duration(0));
                }
                this.audioPlayer.play();
                play.setText("Pause");
            }   else {
                this.audioPlayer.pause();
                play.setText("Play");
            }
        });
        return play;
    }

    public ComboBox<String> createPlayRateOptions() {
        ComboBox<String> playRateOptions = new ComboBox<>();
        playRateOptions.getItems().addAll("0.5x", "1x", "1.5x", "2x");
        double rate = this.audioPlayer.getRate();
        playRateOptions.getSelectionModel().select((int) (rate / 0.5) - 1);
        playRateOptions.setOnAction(e ->{
            double selectedRate = (playRateOptions.getSelectionModel().getSelectedIndex() + 1) * 0.5;
            this.audioPlayer.setRate(selectedRate);
        });
        return playRateOptions;
    }

    public Slider createAudioSlider()   {
        MediaPlayer audioPlayer = this.audioPlayer;
        double defaultVolume = super.getMedia().getDefaultVolume();

        Slider audioSlider = new Slider(0, 1, 1);
        audioSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                audioPlayer.setVolume(defaultVolume * (double) newValue);
            }
        });
        return audioSlider;
    }

    public Slider createPlaybackSlider()    {
        MediaPlayer audioPlayer = this.audioPlayer;
        double totalDuration = this.audioPlayer.getTotalDuration().toSeconds();
        System.out.println(totalDuration);
        Slider playbackSlider = new Slider(0, totalDuration, 0);
        return playbackSlider;
    }

    public void createTimestamps()  {
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

    }

    public MediaPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
