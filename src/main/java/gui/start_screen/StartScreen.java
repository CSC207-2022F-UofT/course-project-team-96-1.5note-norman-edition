package gui.start_screen;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;

import java.io.File;

import gui.SwapPane;
import gui.page_screen.PageScreen;
import gui.error_window.ErrorWindow;

import storage.SQLiteStorage;

import app.MediaCommunicator;


/**
 * First screen shown to the user from which they can create/load pages.
 */
public class StartScreen extends VBox {

    private static final int PADDING = 5;

    private SwapPane parent;
    private MenuBar menuBar;
    private Menu pageMenu;
    private Button newPageButton;
    private Button loadPageButton;
    private PageScreen pageScreen;

    private SQLiteStorage storage;
    private MediaCommunicator c;

    public StartScreen(SwapPane parent, MenuBar menuBar) {
        this.parent = parent;
        this.menuBar = menuBar;

        newPageButton = new NewPageButton();
        loadPageButton = new LoadPageButton();

        pageMenu = new Menu("app.media.Page");
        menuBar.getMenus().add(pageMenu);

        MenuItem newPageItem = new MenuItem("New");
        MenuItem loadPageItem = new MenuItem("Load");
        MenuItem closePageItem = new MenuItem("Close");
        MenuItem savePageItem = new MenuItem("Save");
        MenuItem savePageAsItem = new MenuItem("Save As");

        newPageButton.setOnAction(e -> newPage());
        newPageItem.setOnAction(e -> newPage());

        loadPageButton.setOnAction(e -> loadPage());
        loadPageItem.setOnAction(e -> loadPage());
        closePageItem.setOnAction(e -> closePage());
        savePageItem.setOnAction(e -> savePage());
        savePageAsItem.setOnAction(e -> savePageAs());

        HBox pageButtonsRow = new HBox(
                PADDING, newPageButton, loadPageButton);
        pageButtonsRow.paddingProperty().setValue(new Insets(PADDING));

        pageMenu.getItems().addAll(
                newPageItem, loadPageItem, closePageItem,
                savePageItem, savePageAsItem);
        setPageOnlyMenuItems(closePageItem, savePageItem, savePageAsItem);

        getChildren().add(pageButtonsRow);
        getChildren().add(new Separator());
    }

    private void newPageForFile(File file) {
        System.gc();

        try {
            closeStorage();
            storage = new SQLiteStorage(file);
            c = new MediaCommunicator(storage);

            if (pageScreen == null) {
                pageScreen = new PageScreen(c);
                parent.swapTo(pageScreen);
            } else {
                pageScreen.newPage(c);
            }
        } catch (Exception e) {
            new ErrorWindow(
                    this, "Couldn't load page from file.",
                    "Make sure the file selected is a valid page file.", e)
                .show();
        }
    }

    private void newPage() {
        newPageForFile(null);
    }

    private void loadPage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load app.media.Page");
        File file = fileChooser.showOpenDialog(getScene().getWindow());

        if (file != null) {
            newPageForFile(file);
        }
    }

    private void closePage() {
        pageScreen = null;
        closeStorage();
        c = null;
        parent.swapBack();
    }

    private void closeStorage() {
        if (storage != null) {
            try {
                storage.close();
                storage = null;
            } catch (Exception e) {
                new ErrorWindow(this, "Couln't close storage", null, e).show();
            }
        }
    }

    private void savePage() {
        if (storage != null) {
            if (storage.isInMemory()) {
                savePageAs();
            } else {
                try {
                    c.save();
                } catch (Exception e) {
                    new ErrorWindow(
                            this, "The page could not be saved.",
                            "Make sure the page file exists and is writable.", e)
                        .show();
                }
            }
        }
    }

    private void savePageAs() {
        if (storage != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save app.media.Page");
            fileChooser.setInitialFileName("new.page");
            File file = fileChooser.showSaveDialog(getScene().getWindow());

            if (file != null) {
                try {
                    c.save();
                    storage.saveTo(file);
                } catch (Exception e) {
                    new ErrorWindow(
                            this, "The page could not be saved as the selected file.",
                            "Make sure the selected page file is writable.", e)
                        .show();
                }
            }
        }
    }

    // Make the given MenuItems only usable when a page is open, i.e. when the
    // start screen is disabled.
    private void setPageOnlyMenuItems(MenuItem... items) {
        for (MenuItem item: items) {
            item.setDisable(!isDisabled());
            item.setVisible(isDisabled());
        }

        disabledProperty().addListener((o, oldVal, newVal) -> {
            for (MenuItem item: items) {
                item.setDisable(!newVal);
                item.setVisible(newVal);
            }
        });
    }
}


class NewPageButton extends Button {

    public NewPageButton() {
        super("New app.media.Page");
    }
}


class LoadPageButton extends Button {

    public LoadPageButton() {
        super("Load app.media.Page");
    }
}
