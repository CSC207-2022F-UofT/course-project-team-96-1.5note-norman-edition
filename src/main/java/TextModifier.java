import javafx.scene.control.Hyperlink;
import javafx.util.Duration;

public class TextModifier implements MediaManager{
    private String link;

    @Override
    public void addMedia() {

    }

    @Override
    public void modifyMedia() {

    }

    @Override
    public void searchMedia() {

    }

    public MediaHyperlink createAudioHyperlink(Duration time)    { //TODO: delete if implemented hyperlink method makes this obsolete
        Hyperlink timestamp = new Hyperlink(time.toString());
        return new MediaHyperlink(new double[]{0, 0}, new double[]{0, 0}, 0, 0,
                0, "", timestamp);
    }

    public void addLink(String givenLink){
        this.link = givenLink;
    }


}
