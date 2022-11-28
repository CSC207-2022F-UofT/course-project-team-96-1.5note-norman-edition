package app.interaction_managers;

import app.media.Media;
import javafx.scene.control.TextField;

public class Tagger implements InteractionManager {

    private String tag;

    public Tagger(){

    }
    @Override
    public void interact(String input) {
        this.tag = input;

    }
    public void addTag(Media media){
        media.getTags().add(tag);
    }

    public void setTag(String toTag){
        this.tag = toTag;
    }

}
