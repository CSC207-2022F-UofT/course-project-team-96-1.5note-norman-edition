public class Tagger implements InteractionManager{

    private String tag;

    public Tagger(){
    }
    @Override
    public void interact(javafx.scene.Node userInput) {
        this.tag = userInput.toString();
    }

    public void addTag(PageMedia toTag){
        toTag.setTag(tag);
    }
}
