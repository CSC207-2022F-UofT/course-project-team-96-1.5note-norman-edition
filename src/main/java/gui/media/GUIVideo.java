package gui.media;

import app.media.MediaAudio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class GUIVideo extends GUIAudio{
    public GUIVideo(MediaAudio audio) {
        super(audio);

        //Repeat code, but for some reason this doesnt execute properly in the super class
        getAudioPlayer().setOnReady(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaView videoView = new MediaView(getAudioPlayer());
                    setPlayerUI(new VideoPlayerInterface(audio.getName(), videoView));

                    configureUI();
                    createInterface();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

class VideoPlayerInterface extends PlayerInterface  {

    public VideoPlayerInterface(String name, MediaView video) {
        super(name);
        addVideoView(video);
    }

    private void addVideoView(MediaView video)   {
        Node playerLayout = getChildren().get(0);

        video.setFitHeight(200); //temp value
        video.setFitWidth(getWidth());
        this.getChildren().setAll(video, playerLayout);
        setAlignment(Pos.CENTER);
    }
}
