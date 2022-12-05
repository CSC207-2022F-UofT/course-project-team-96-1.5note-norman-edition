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

        return new Tool[] {
            colourTool,
            new PenTool(colourTool.colourProperty()),
            new ShapeTool(colourTool.colourProperty()),
            new TextTool(colourTool.colourProperty()),
            new HyperlinkTool(colourTool.colourProperty()),
            new AudioTool("Audio"),
            new VideoTool("Video"),
            new MediaTool(),
            new TagTool(),
            new SearchTool(),
        };
    }
}
