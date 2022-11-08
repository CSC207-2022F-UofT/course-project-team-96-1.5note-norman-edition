package gui.tool;

import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.event.*;

import app.MediaCommunicator;
import gui.page.PageEventHandler;


/**
 * Utility class for instantiating the available tools.
 */
public final class Tools {

    private Tools() {}

    public static Tool[] getTools() {
        Tool[] tools = {
            new PenTool(),
            /*
            new DummyTool("Tool 1"),
            new DummyTool("Tool 2"),
            new DummyTool("Tool 3"),
            new DummyTool("Tool 4"),
            new DummyTool("Tool 5"),
            new DummyTool("Tool 6"),
            new DummyTool("Tool 7"),
            new DummyTool("Tool 8"),
            new DummyTool("Tool 9"),
            new DummyTool("Tool 10")
            */
        };

        return tools;
    }
}


/*
// TODO: Remove this class once actual Tools are available.
class DummyTool implements Tool {

    private String name;

    public DummyTool(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FlowPane getSettingsGUI() {
        // Put lots of text to make sure it
        // gets layed out properly and doesn't
        // overflow the tool pane.
        FlowPane f = new FlowPane(
                new Text(getName()),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA "),
                new Text("AAAAAA ")
                );

        return f;
    }

    @Override
    public PageEventHandler.HandlerMethod[] getHandlerMethods() {
        PageEventHandler.HandlerMethod[] handlers = {
            new PageEventHandler.HandlerMethod(Event.ANY, e -> {
                System.out.println(getName());
                System.out.println(e);
            })
        };

        return handlers;
    }
}
*/
