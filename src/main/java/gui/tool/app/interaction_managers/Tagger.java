package gui.tool.app.interaction_managers;

import gui.tool.app.media.Media;
import javafx.scene.control.TextField;

public class Tagger implements InteractionManager {

    private String tag;

    public Tagger(){

    }
    @Override
    public void interact(TextField node) {
        this.tag = node.getText();
    }

    public void addTag(Media media){
        media.getTags().add(tag);
    }

    public void setTag(String toTag){
        this.tag = toTag;
    }

}