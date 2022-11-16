import org.junit.*;
import static org.junit.Assert.*;

import javafx.scene.*;
import javafx.scene.layout.*;

import gui.ResourceLoader;


public class TestResourceLoader {

    @Test
    public void testLoadNonExistantIcon() {
        // Loading a non-existant icon should not cause any error and should 
        // return a placeholder region with the requested size.

        // pick arbitrary values to make sure they are preserved in the returned
        // placeholder icon.
        final int SVG_WIDTH = 61;
        final int SVG_HEIGHT = 13;

        final int BITMAP_WIDTH = 11;
        final int BITMAP_HEIGHT = 42;

        Node nonExistantSVG = ResourceLoader.loadSVGicon("this path does not exist", SVG_WIDTH, SVG_HEIGHT);
        Node nonExistantBitmap = ResourceLoader.loadBitmapIcon("this path does not exist", BITMAP_WIDTH, BITMAP_HEIGHT);

        assertTrue(nonExistantSVG instanceof Region);
        assertTrue(nonExistantBitmap instanceof Region);

        Region SVGRegion = (Region) nonExistantSVG;
        Region bitmapRegion = (Region) nonExistantBitmap;

        assertTrue(SVGRegion.getPrefWidth() == SVG_WIDTH && SVGRegion.getPrefHeight() == SVG_HEIGHT);
        assertTrue(bitmapRegion.getPrefWidth() == BITMAP_WIDTH && bitmapRegion.getPrefHeight() == BITMAP_HEIGHT);
    }
}
