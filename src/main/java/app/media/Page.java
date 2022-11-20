package app.media;

import java.util.ArrayList;

public class Page {
    private ArrayList<PageMedia> elements;
    private double viewSize;

    public Page(ArrayList<PageMedia> elements, double viewSize) {
        this.elements = elements;
        this.viewSize = viewSize;
    }

    public ArrayList<PageMedia> getElements() {
        return elements;
    }

    public double getViewSize() {
        return viewSize;
    }

    public void setElements(ArrayList<PageMedia> elements) {
        this.elements = elements;
    }

    public void setViewSize(double viewSize) {
        this.viewSize = viewSize;
    }
}
