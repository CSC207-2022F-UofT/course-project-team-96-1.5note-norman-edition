package app.interaction_managers;

import app.media.Media;
import javafx.scene.control.TextField;

public class Tagger implements InteractionManager {

    private String tag;

    public Tagger(){

    }
    @Override
    public void interact(javafx.scene.Node node) {
        if (node instanceof TextField) {
            this.tag = ((TextField) node).getText();
        }
    }
    public void addTag(Media media){
        media.getTags().add(tag);

    }
}
