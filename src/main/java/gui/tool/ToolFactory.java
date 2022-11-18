package gui.tool;


/**
 * Utility class for instantiating the available tools.
 */
public final class ToolFactory {

    private ToolFactory() {}

    public static Tool[] getTools() {

        ColourTool colourTool = new ColourTool();

        Tool[] tools = {
            colourTool,
            new PenTool(colourTool.colourProperty()),
            new MediaTool()
        };

        return tools;
    }
}
