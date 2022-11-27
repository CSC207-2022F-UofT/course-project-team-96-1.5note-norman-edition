package gui;

/** Interface implemented by Nodes that are able to scale larger or smaller, creating a zooming appearance in
 * the GUI.
 * Implementors must be able to zoom to a given scale factor, and also have some consistent way of zooming in or
 * out given an input specifying in or out.
 */

public interface Zoomable {

    default void zoomToFactor(double zoomFactor){}

    default void zoomInOrOut(String inOrOut){}
}
