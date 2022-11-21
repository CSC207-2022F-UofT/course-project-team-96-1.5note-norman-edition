package gui;

/** interface between controller level and app.gui.page.Page (in GUI level) */
// TODO change this comment

public interface Zoomable {

    default void zoomToFactor(double zoomFactor){}

    default void zoomInOrOut(String inOrOut){}
}
