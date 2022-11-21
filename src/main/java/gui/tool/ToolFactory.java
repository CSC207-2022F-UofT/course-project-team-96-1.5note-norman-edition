package gui.tool;


/**
 * Utility class for instantiating the available tools.
 */
public final class ToolFactory {

    private ToolFactory() {}

    public static Tool[] getTools() {

        ColourTool colourTool = new ColourTool();
        TagTool tagTool = new TagTool();
        SearchTool searchTool = new SearchTool();

        Tool[] tools = {
            colourTool,
            new PenTool(colourTool.colourProperty()),
                tagTool, searchTool
        };

        return tools;
    }
}
