package gui.tool;

import javafx.scene.input.*;

import app.MediaCommunicator;
import gui.page.PageEventHandler.*;
import gui.page.Page;
import gui.media.GUIPenStroke;


public class PenTool implements Tool {

    private MediaCommunicator c;
    private HandlerMethod[] handlers;
    private Page page;

    private GUIPenStroke currentStroke;

    public PenTool() {
        HandlerMethod[] handlers = {
            new HandlerMethod<>(MouseEvent.MOUSE_PRESSED, this::startStroke),
            new HandlerMethod<>(MouseEvent.MOUSE_RELEASED, this::endStroke),
            new HandlerMethod<>(MouseEvent.MOUSE_DRAGGED, this::updateStroke)
        };
        this.handlers = handlers;
    }

    @Override
    public void setCommunicator(MediaCommunicator c) {
        this.c = c;
    }

    @Override
    public String getName() {
        return "Pen";
    }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public void disabledFor(Page page) {
        this.page = null;
        finishStroke();
    }

    private void startStroke(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            currentStroke = new GUIPenStroke(page.getMouseCoords(e), 5.0);
            page.addMedia(currentStroke);
        }
    }

    private void updateStroke(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            currentStroke.update(page.getMouseCoords(e), 5.0, e.isShiftDown());
        }
    }

    private void endStroke(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            finishStroke();
        }
    }

    private void finishStroke() {
        if (currentStroke != null) {
            currentStroke.end();
            c.updateMedia(currentStroke.getMedia());
            currentStroke = null;
        }
    }
}
