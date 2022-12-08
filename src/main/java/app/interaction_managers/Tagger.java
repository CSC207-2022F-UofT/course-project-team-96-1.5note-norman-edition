package app.interaction_managers;

import app.media.Media;

public class Tagger implements InteractionManager {

    private String tag;

    public Tagger() {

    }

    @Override
    public void interact(String node) {
        this.tag = node;
    }

    public void addTag(Media media) {
        media.getTags().add(tag);
    }
    public void removeTag(Media media){
        media.getTags().remove(tag);
    }
}
