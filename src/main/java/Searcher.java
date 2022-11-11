import javax.print.attribute.standard.Media;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Searcher implements InteractionManager {

    private String userSearch;
    private final MediaCommunicator currentPage;
    public Searcher(MediaCommunicator currentPage){
        this.currentPage = currentPage;
    }
    @Override
    public void interact(javafx.scene.Node userInput) {
        this.userSearch = userInput.toString();
    }

    public ArrayList<double[]> search() {
        Page toSearch = currentPage.getPage();
        ArrayList<PageMedia> allElements = toSearch.getElements();
        ArrayList<double[]> foundResults = new ArrayList<>();

        for (PageMedia media : allElements) {
            if (media instanceof MediaText) {
                if (((MediaText) media).getText().equals(userSearch) || media.getTag().equals(userSearch)) {
                    foundResults.add(media.getPosition());
                }
            }
            else if (media.getTag().equals(userSearch)) {
                foundResults.add(media.getPosition());
            }
        }
        return foundResults;
    }
}
