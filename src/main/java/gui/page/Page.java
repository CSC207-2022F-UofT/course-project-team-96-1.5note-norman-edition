package gui.page;

import javafx.scene.layout.*;
import javafx.beans.value.*;

import app.MediaCommunicator;
import app.MediaObserver;


public class Page extends StackPane implements MediaObserver {

    private MediaCommunicator c;
    private PageEventHandler.HandlerMethod<?>[] handlerMethods;
    private PageEventHandler handler;

    public Page(MediaCommunicator c) {
        this.c = c;
        this.handlerMethods = new PageEventHandler.HandlerMethod<?>[0];

        c.addObserver(this);
        getStyleClass().add("page");
    }

    /**
     * Set the handler of input events.
     * <p>
     * The previous handler will be removed.
     */
    public void setEventHandler(PageEventHandler h) {
        // Remove previous handler's handler methods
        for (PageEventHandler.HandlerMethod<?> handlerMethod: handlerMethods) {
            handlerMethod.removeFromPage(this);
        }

        // Notify the previous handler that it will no longer receive events
        // from this page.
        if (handler != null) {
            handler.disabledFor(this);
        }
        handler = h;

        if (handler != null) {
            handlerMethods = handler.getHandlerMethods();
            // Notify the new handler that it will now be receiving events from
            // this page.
            handler.enabledFor(this);
        } else {
            handlerMethods = new PageEventHandler.HandlerMethod<?>[0];
        }

        // Add new handler's handler methods
        for (PageEventHandler.HandlerMethod<?> handlerMethod: handlerMethods) {
            handlerMethod.addToPage(this);
        }
    }
}
