package app;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;


public class MediaCommunicator {

    private static Map<Storage, MediaCommunicator> communicatorMap = new HashMap<>();

    private Set<Page> pages;
    private Storage storage;

    private MediaCommunicator(Storage storage) {
        this.pages = new HashSet<>();
        this.storage = storage;
    }

    public static MediaCommunicator getFor(Storage storage) {
        return Optional.ofNullable(communicatorMap.get(storage))
            .orElse(new MediaCommunicator(storage));
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public void removePage(Page page) {
        pages.remove(page);

        if (pages.isEmpty()) {
            communicatorMap.remove(this.storage);
        }
    }
}
