package gui;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;


/**
 * Utility class for loading resource files (icon images, stylesheets, etc.).
 * <p>
 * Resource files are stored in the `res` directory. However, when the project
 * is built as a JAR file, the res directory is included and resources should
 * be loaded from the JAR file rather than from an external file.
 * <p>
 * Additionally, there is a common setup process for loading images of the same
 * type, etc. which is implemented by this class to avoid duplication elsewhere.
 */
public final class ResourceLoader {

    private ResourceLoader() {}

    private static ClassLoader getClassLoader() {
        return ResourceLoader.class.getClassLoader();
    }

    /**
     * Return a URL for the resource at the given path.
     * <p>
     * The path is expected to be relative to the resource directory.
     * The returned URL will either be for a file in the external `res`
     * directory, or a JAR entry in the internal `res` directory, depending on
     * how/where the program is run.
     */
    public static URL getResourceURL(String path) {
        path = "res/" + path;
        File resourceFile = new File(path);

        if (resourceFile.exists()) {
            try {
                return resourceFile.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getClassLoader().getResource(path);
        }
    }

    // Return a placeholder node with the given dimensions. If loading an icon
    // fails, this method can be used to return a node which would take up the
    // same amount of space as the desired icon so layouts don't get messed up.
    //
    // Additionally, using this method when exceptions are encountered means
    // GUI elements don't have to explicitly handle exceptions each time they
    // want to load an icon.
    private static Node getPlaceholder(int width, int height) {
        Region r = new Region();
        r.setPrefWidth(width);
        r.setPrefHeight(height);

        return r;
    }

    /**
     * Load an SVG image as an icon with the given dimensions and colour.
     *
     * @param path The path to the SVG image relative to the `res` directory.
     */
    public static Node loadSVGicon(String path, int width, int height, Color colour) {
        try (InputStream s = getResourceURL(path).openStream()) {
            // JavaFX has incomplete support for SVG images: It only supports
            // the "path" element. Therefore, when loading an SVG file, we just
            // look for the first path element in the file and use its contents.
            String svgString = new String(s.readAllBytes());
            svgString = svgString.substring(svgString.indexOf("<path") + 5);
            svgString = svgString.substring(svgString.indexOf(" d=\"") + 4);
            svgString = svgString.substring(0, svgString.indexOf('"'));

            SVGPath svgPath = new SVGPath();
            svgPath.setContent(svgString);

            Region svgRegion = new Region();
            svgRegion.setShape(svgPath);
            svgRegion.setPrefWidth(width);
            svgRegion.setPrefHeight(height);
            svgRegion.setMaxWidth(width);
            svgRegion.setMaxHeight(height);
            svgRegion.setBackground(Background.fill(colour));

            return svgRegion;
        } catch (Exception e) {
            return getPlaceholder(width, height);
        }
    }

    public static Node loadSVGicon(String path, int width, int height) {
        return loadSVGicon(path, width, height, Color.BLACK);
    }

    /**
     * Load a bitmap image as an icon with the given dimensions and colour.
     * <p>
     * The formats supported by JavaFX are: BMP, GIF, JPEG and PNG.
     *
     * @param path The path to the bitmap image relative to the `res` directory.
     */
    public static Node loadBitmapIcon(String path, int width, int height) {
        try (InputStream s = getResourceURL(path).openStream()) {
            Image image = new Image(s);
            ImageView view = new ImageView(image);

            view.setFitWidth(width);
            view.setFitHeight(height);

            return view;
        } catch (Exception e) {
            return getPlaceholder(width, height);
        }
    }
}
