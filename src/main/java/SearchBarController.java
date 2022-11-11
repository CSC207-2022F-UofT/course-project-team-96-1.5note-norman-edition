public class SearchBarController {

    // Refine return type. As of right now, documentation implies it can be a
    // media, file, or subsection of text in a text box???
    // I have started adjusting the CRC cards so that they only implies returning the text media,
    // But tags are still an issue.
    public void passSearch(javafx.scene.Node input, MediaCommunicator page) {
        Searcher toSearch =new Searcher(page);
        toSearch.interact(input);
    }
}
