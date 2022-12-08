package gui.error_window;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;


/**
 * Display an error message to the user and prevent further use of the GUI until
 * the message is dismissed.
 */
public class ErrorWindow extends Dialog<Object> {

    private static final int PADDING = 5;
    private static final String DEFAULT_TITLE = "Error!";

    /**
     * @param owner The main window to which the error window belongs.
     * @param title The title text for the error window. (can be null)
     * @param explanation Human-readable text explaining what went wrong.
     *  (can be null)
     * @param shortMessage Short error message text.
     *  This should fit on a single line. (can be null)
     * @param detailedMessage Long error message text. (can be null)
     */
    public ErrorWindow(
            Window owner, String title, String explanation,
            String shortMessage, String detailedMessage)
    {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);

        ButtonType type = new ButtonType("Dismiss", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(type);
        getDialogPane().getStyleClass().add("error-window");

        VBox vBox = new VBox(PADDING);
        getDialogPane().setContent(vBox);

        Text titleText = new Text(DEFAULT_TITLE);
        titleText.getStyleClass().add("title");
        setTitle(DEFAULT_TITLE);

        if (title != null) {
            titleText.setText(title);
            setTitle(title);
        }

        vBox.getChildren().add(titleText);

        if (explanation != null) {
            Text explanationText = new Text(explanation);
            explanationText.getStyleClass().add("explanation");
            vBox.getChildren().add(explanationText);
        }

        if (shortMessage != null) {
            vBox.getChildren().add(new Separator());

            TextField shortMessageText = new TextField(shortMessage);
            shortMessageText.setEditable(false);
            vBox.getChildren().addAll(
                    new Text("Short Error Message:"),
                    shortMessageText);
        }

        if (detailedMessage != null) {
            TextArea detailedMessageText = new TextArea(detailedMessage);
            detailedMessageText.setEditable(false);
            detailedMessageText.setMinHeight(0);

            ScrollPane detailedMessageScrollPane =
                new ScrollPane(detailedMessageText);
            detailedMessageScrollPane.setFitToWidth(true);
            detailedMessageScrollPane.setFitToHeight(true);

            VBox.setVgrow(detailedMessageScrollPane, Priority.ALWAYS);
            vBox.getChildren().addAll(
                    new Separator(),
                    new Text("Detailed Error Message:"),
                    detailedMessageScrollPane);
        }
    }

    /**
     * @param openFor The node whose containing window to which this error
     *  window will belong.
     */
    public ErrorWindow(
            Node openFor, String title, String explanation,
            String shortMessage, String detailedMessage)
    {
        this(
                openFor.getScene().getWindow(), title, explanation,
                shortMessage, detailedMessage);
    }

    /**
     * @param error The error to report. `error.getMessage()` will be the
     *  short message text and `error.printStackTrace()` will be the detailed
     *  message text.
     */
    public ErrorWindow(
            Window owner, String title, String explanation, Throwable error)
    {
        this(
                owner, title, explanation, error.getMessage(),
                printStackTraceToString(error));
    }

    public ErrorWindow(
            Node openFor, String title, String explanation, Throwable error)
    {
        this(
                openFor, title, explanation, error.getMessage(),
                printStackTraceToString(error));
    }

    private static String printStackTraceToString(Throwable error) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        PrintWriter p = new PrintWriter(b);

        error.printStackTrace(p);
        p.flush();

        return b.toString();
    }
}
