package gui;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.collections.ObservableList;


/**
 * Restricted StackPane which initially displays a "base" Node and allows
 * "swapping" to display another Node.
 * <p>
 * A SwapPane only contains up to 2 Nodes at a time: One "base" Node and one
 * "other" Node.
 */
public class SwapPane extends StackPane {

    private Node base;
    private Node other;

    /**
     * Set the "base" Node, replacing the previous "base" Node.
     */
    public void setBase(Node base) {
        super.getChildren().remove(this.base);
        super.getChildren().add(0, base);
        this.base = base;
    }

    /**
     * Swap away from the "base" Node to the given "other" Node.
     * <p>
     * If there was already an "other" Node in view, it will be replaced by
     * the supplied Node.
     * <p>
     * The "base" node (if present) will no longer be visible but will
     * <i>not</i> be un-loaded. It will simply be hidden and disabled, i.e. it
     * is still there, but is not interactable.
     */
    public void swapTo(Node other) {
        super.getChildren().remove(this.other);
        super.getChildren().add(other);
        this.other = other;

        if (other != null) {
            other.requestFocus();
        }

        if (base != null) {
            base.setDisable(true);
            base.setFocusTraversable(false);
        }
    }

    /**
     * Swap away to the "other" Node back to the "base" Node.
     */
    public void swapBack() {
        super.getChildren().remove(other);
        other = null;

        if (base != null) {
            base.setDisable(false);
            base.requestFocus();
        }
    }

    @Override
    public ObservableList<Node> getChildren() {
        // Prevent outside modification not done through
        // `swapTo` and `swapBack`
        return super.getChildrenUnmodifiable();
    }
}
