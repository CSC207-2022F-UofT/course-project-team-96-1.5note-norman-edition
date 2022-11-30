package gui.tool.app.media_managers;

public class ImageModifier implements MediaManager {
    private String caption;
    @Override
    public void addMedia() {

    }

    @Override
    public void modifyMedia() {

    }

    @Override
    public void searchMedia() {

    }

    public void addCaption(String givenCaption){
        this.caption = givenCaption;
    }
}
