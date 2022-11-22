package gui.start_screen;

import gui.page.Page;
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
    private final MenuBar menuBar;
    private final Menu pageMenu;
    private final Menu viewMenu;
    private final Button newPageButton;
    private final Button loadPageButton;
    private PageScreen pageScreen;
    private SQLiteStorage storage;
    private MediaCommunicator c;

    public StartScreen(SwapPane parent, MenuBar menuBar) {
        this.parent = parent;
        this.menuBar = menuBar;

        newPageButton = new NewPageButton();
        loadPageButton = new LoadPageButton();

        pageMenu = new Menu("Page");
        viewMenu = new Menu("View");
        menuBar.getMenus().add(pageMenu);
        menuBar.getMenus().add(viewMenu);

        // options in page menu
        MenuItem newPageItem = new MenuItem("New");
        MenuItem loadPageItem = new MenuItem("Load");
        MenuItem closePageItem = new MenuItem("Close");
        MenuItem savePageItem = new MenuItem("Save");
        MenuItem savePageAsItem = new MenuItem("Save As");

        // options in view menu
        Menu zoomToSubMenu = new Menu("Zoom To");
        MenuItem zoomInItem = new MenuItem("Zoom In");
        MenuItem zoomOutItem = new MenuItem("Zoom Out");
        MenuItem resetItem = new MenuItem("Reset Zoom");
        MenuItem centerItem = new MenuItem("Center Page");

        //zoom to drop down menu options
        MenuItem zoomPercent25 = new MenuItem("25%");
        MenuItem zoomPercent50 = new MenuItem("50%");
        MenuItem zoomPercent75 = new MenuItem("75%");
        MenuItem zoomPercent100 = new MenuItem("100%");
        MenuItem zoomPercent150 = new MenuItem("150%");
        MenuItem zoomPercent200 = new MenuItem("200%");
        MenuItem zoomPercent300 = new MenuItem("300%");
        MenuItem zoomPercent400 = new MenuItem("400%");
        zoomToSubMenu.getItems().addAll(zoomPercent25, zoomPercent50, zoomPercent75, zoomPercent100
                , zoomPercent150, zoomPercent200, zoomPercent300, zoomPercent400);

        newPageButton.setOnAction(e -> newPage());
        newPageItem.setOnAction(e -> newPage());

        loadPageButton.setOnAction(e -> loadPage());
        loadPageItem.setOnAction(e -> loadPage());
        closePageItem.setOnAction(e -> closePage());
        savePageItem.setOnAction(e -> savePage());
        savePageAsItem.setOnAction(e -> savePageAs());

        // setting view menu actions
        zoomPercent25.setOnAction(e -> zoomTo(.25));
        zoomPercent50.setOnAction(e -> zoomTo(.5));
        zoomPercent75.setOnAction(e -> zoomTo(.75));
        zoomPercent100.setOnAction(e -> zoomTo(1.0));
        zoomPercent150.setOnAction(e -> zoomTo(1.5));
        zoomPercent200.setOnAction(e -> zoomTo(2.0));
        zoomPercent300.setOnAction(e -> zoomTo(3.0));
        zoomPercent400.setOnAction(e -> zoomTo(4.0));
        zoomInItem.setOnAction(e -> zoomInOrOut("In"));
        zoomOutItem.setOnAction(e -> zoomInOrOut("Out"));
        resetItem.setOnAction(e -> zoomTo(1.0));
        centerItem.setOnAction(e -> centerPage());

        HBox pageButtonsRow = new HBox(
                PADDING, newPageButton, loadPageButton);
        pageButtonsRow.paddingProperty().setValue(new Insets(PADDING));

        pageMenu.getItems().addAll(
                newPageItem, loadPageItem, closePageItem,
                savePageItem, savePageAsItem);
        setPageOnlyMenuItems(closePageItem, savePageItem, savePageAsItem, zoomToSubMenu, zoomInItem, zoomOutItem,
                resetItem);
        setPageOnlyMenu(viewMenu);
        viewMenu.getItems().addAll(zoomToSubMenu, zoomInItem, zoomOutItem, resetItem, centerItem);

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
        fileChooser.setTitle("Load Page");
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
            fileChooser.setTitle("Save Page");
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

    /** scale the page's contents by the given targetZoom
     *
     * @param targetZoom factor to scale by
     */
    private void zoomTo(double targetZoom) {
        Page page = pageScreen.getPage();
        page.zoomToFactor(targetZoom);
    }

    /** scale the page larger if zooming in, smaller if zooming out
     *
     * @param inOrOut String specifying whether to zoom "In" or "Out"
     */
    private void zoomInOrOut(String inOrOut) {
        Page page = pageScreen.getPage();
        page.zoomInOrOut(inOrOut);
    }

    /** bring the page back to the original starting position
     *
     */
    private void centerPage() {
        Page page = pageScreen.getPage();
        page.jumpToTopLeft(0, 0);
//        page.jumpToTopLeft(0, 0);
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

    /** hide given menus when there is no Page
     *
     * @param menus options to hide when there is no Page
     */
    private void setPageOnlyMenu(Menu... menus) {
        for (Menu menu: menus) {
            menu.setDisable(!isDisabled());
            menu.setVisible(isDisabled());
        }

        disabledProperty().addListener((o, oldVal, newVal) -> {
            for (Menu menu: menus) {
                menu.setDisable(!newVal);
                menu.setVisible(newVal);
            }
        });
    }
}

class NewPageButton extends Button {

    public NewPageButton() {
        super("New Page");
    }
}


class LoadPageButton extends Button {

    public LoadPageButton() {
        super("Load Page");
    }
}
