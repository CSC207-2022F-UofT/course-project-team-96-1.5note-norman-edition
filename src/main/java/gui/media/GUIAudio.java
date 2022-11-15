package gui.media;

import app.media.MediaAudio;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class GUIAudio extends GUIMedia<MediaAudio>{
    //TODO: A bit of a temp class

    private String tempFile;
    private MediaPlayer audioPlayer;

    public GUIAudio(MediaAudio audio, String tempFile)   {
        super(audio);
        this.tempFile = tempFile;

        Media audioMedia = new Media(tempFile);
        this.audioPlayer = new MediaPlayer(audioMedia);

    }

    public HBox createInterface()   {
        Button playButton = createPlayButton();
        ComboBox<String> playbackRate = createPlayRateOptions();
        Slider volumeSlider = createAudioSlider();

        HBox layout = new HBox();
        layout.getChildren().addAll(playButton, playbackRate, volumeSlider);
        return layout;
    }

    public Button createPlayButton()    {
        Button play = new Button("Play");
        play.setOnAction(e -> {
            if (play.getText().equals("Play"))  {
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


}