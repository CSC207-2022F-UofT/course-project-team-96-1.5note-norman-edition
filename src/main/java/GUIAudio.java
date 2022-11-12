import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class GUIAudio {
    //TODO: A bit of a temp class

    private MediaAudio audio;

    public GUIAudio(MediaAudio audio)   {
        this.audio = audio;
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
                this.audio.getAudio().play();
                play.setText("Pause");
            }   else {
                this.audio.getAudio().pause();
                play.setText("Play");
            }
        });
        return play;
    }

    public ComboBox<String> createPlayRateOptions() {
        ComboBox<String> playRateOptions = new ComboBox<>();
        playRateOptions.getItems().addAll("0.5x", "1x", "1.5x", "2x");
        double rate = this.audio.getAudio().getRate();
        playRateOptions.getSelectionModel().select((int) (rate / 0.5) - 1);
        playRateOptions.setOnAction(e ->{
            double selectedRate = (playRateOptions.getSelectionModel().getSelectedIndex() + 1) * 0.5;
            this.audio.getAudio().setRate(selectedRate);
        });
        return playRateOptions;
    }

    public Slider createAudioSlider()   {
        MediaPlayer audioPlayer = this.audio.getAudio();
        double defaultVolume = this.audio.getDefaultVolume();

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
        MediaPlayer audioPlayer = this.audio.getAudio();
        double totalDuration = this.audio.getAudio().getTotalDuration().toSeconds();
        System.out.println(totalDuration);
        Slider playbackSlider = new Slider(0, totalDuration, 0);



        return playbackSlider;
    }


}
