import app.media.MediaText;
import app.media.MediaImage;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Class to test Text and Image media. Since 90% of their function is loading and displaying stuff on the page and
 * responding to specific user interaction, there is little to test here with them, and they are best tested manually,
 * which is what I have been doing myself. But these are here for proper thoroughness.
 */
public class TestTextAndImage {

    @Test
    public void testMediaTextConstructor() {
        MediaText testText = new MediaText(0, 0, "TESTING 123 TEXT", "red");
        assertEquals(testText.getText(), "TESTING 123 TEXT");
    }

    @Test
    public void testMediaImageSetting() {
        MediaText testText = new MediaText(0, 0, "TESTING 123 TEXT", "red");
        testText.setText("RESET TEXT 098");
        assertEquals(testText.getText(), "RESET TEXT 098");
    }

    @Test
    public void testMediaTextSetting() {
        byte[] testData = new byte[123456789];
        MediaImage testImage = new MediaImage("TESTIMAGE", 0, 0, 1, 1, testData);
        assertEquals(testImage.getRawData(), testData);
    }
}
