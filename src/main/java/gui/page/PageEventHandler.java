package gui.page;

import javafx.event.*;
import javafx.scene.input.InputEvent;


/**
 * Implementors of this interface provide handler methods which accept events
 * from a Page.
 * <p>
 * Implementors will also be notified when their handlers are added to or
 * removed from a page via the `enabledFor` and `disabledFor` methods.
 */
public interface PageEventHandler {

    /**
     * Combination of an EventType and an EventHandler which handles events of
     * an appropriate type.
     * <p>
     * The type for this class looks intimidating, but its usage is fairly
     * straightforward. Its purpose is simply to couple an EventType with
     * an EventHandler so that they can easily be passed around together.
     * <p>
     * In the following example, a HandlerMethod is created which will accept
     * all types of events and print them:
     * <pre>
     * new HandlerMethod(Event.ANY, e -> System.out.println(e));
     */
    public static record HandlerMethod<T extends Event>(
            EventType<T> eventType, EventHandler<? super T> eventHandler)
    {
        // NOTE: the `addToPage` and `removeFromPage` methods are defined here
        // instead of in the GUIPage class to avoid some rough edges of Java's
        // type system w.r.t. generics.

        void addToPage(Page page) {
            page.addEventHandler(eventType(), eventHandler());
        }

        void removeFromPage(Page page) {
            page.removeEventHandler(eventType(), eventHandler());
        }
    }

    /**
     * Get the handler methods to be added to a page through which events will
     * be received.
     */
    default HandlerMethod[] getHandlerMethods() {
        return new HandlerMethod[0];
    }

    /**
     * Called when the handler is set to receive events from the given page.
     */
    default void enabledFor(Page page) {}

    /**
     * Called when the handler is no longer set to receive events for the given
     * page.
     * <p>
     * Implementors should get rid of any references to the given page when
     * this method is called.
     */
    default void disabledFor(Page page) {}
}
