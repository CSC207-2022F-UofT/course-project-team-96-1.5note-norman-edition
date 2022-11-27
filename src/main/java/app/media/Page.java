package app.media;
import java.util.ArrayList;

public class Page {
    private ArrayList<Media> elements;
    private double viewSize;

    public Page(ArrayList<Media> elements, double viewSize) {
        this.elements = elements;
        this.viewSize = viewSize;
    }

    public ArrayList<Media> getElements() {
        return elements;
    }

    public double getViewSize() {
        return viewSize;
    }

    public void setElements(ArrayList<Media> elements) {
        this.elements = elements;
    }

    public void setViewSize(double viewSize) {
        this.viewSize = viewSize;
    }
}
