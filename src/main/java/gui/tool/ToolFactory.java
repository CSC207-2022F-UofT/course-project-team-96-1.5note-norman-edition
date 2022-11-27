package gui.tool;


/**
 * Utility class for instantiating the available tools.
 */
public final class ToolFactory {

    private ToolFactory() {}

    /**
     * Return an array containing instances of all the available tools.
     */
    public static Tool[] getTools() {

        ColourTool colourTool = new ColourTool();

        Tool[] tools = {
            colourTool,
            new PenTool(colourTool.colourProperty()),
            new ShapeTool(colourTool.colourProperty()),
            new MediaTool(),
            new TextTool(),
            new TagTool(),
            new SearchTool()
        };

        return tools;
    }
}
