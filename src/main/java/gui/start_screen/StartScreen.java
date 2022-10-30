package gui.start_screen;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;

import java.io.File;

import gui.SwapPane;
import gui.page.GUIPage;
import gui.page.GUIPageTool;

import storage.FileStorage;

import app.MediaCommunicator;


/**
 * First screen shown to the user from which they can create/load pages.
 */
public class StartScreen extends VBox {

    private static final int PADDING = 5;

    private SwapPane parent;
    private MenuBar menuBar;
    private Menu pageMenu;
    private GUIPageTool[] tools;
    private Button newPageButton;
    private Button loadPageButton;

    private GUIPage page;

    public StartScreen(SwapPane parent, MenuBar menuBar, GUIPageTool[] tools) {
        this.parent = parent;
        this.menuBar = menuBar;
        this.tools = tools;

        newPageButton = new NewPageButton();
        loadPageButton = new LoadPageButton();

        pageMenu = new Menu("Page");
        menuBar.getMenus().add(pageMenu);

        MenuItem newPageItem = new MenuItem("New");
        MenuItem loadPageItem = new MenuItem("Load");
        MenuItem closePageItem = new MenuItem("Close");

        newPageButton.setOnAction(e -> newPage());
        newPageItem.setOnAction(e -> newPage());

        loadPageButton.setOnAction(e -> loadPage());
        loadPageItem.setOnAction(e -> loadPage());
        closePageItem.setOnAction(e -> closePage());

        HBox pageButtonsRow = new HBox(
                PADDING, newPageButton, loadPageButton);
        pageButtonsRow.paddingProperty().setValue(new Insets(PADDING));

        pageMenu.getItems().addAll(newPageItem, loadPageItem, closePageItem);
        setPageOnlyMenuItems(closePageItem);

        getChildren().add(pageButtonsRow);
        getChildren().add(new Separator());
    }

    private void newPageForFile(File file) {
        closePage();

        FileStorage storage = new FileStorage(file);
        MediaCommunicator c = MediaCommunicator.getFor(storage);
        page = new GUIPage(tools, c);

        parent.swapTo(page);
    }

    private void newPage() {
        newPageForFile(null);
    }

    private void loadPage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Notebook");
        File file = fileChooser.showOpenDialog(getScene().getWindow());

        if (file != null) {
            newPageForFile(file);
        }
    }

    private void closePage() {
        if (page != null) {
            page.close();
            page = null;
        }

        parent.swapBack();
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
        super("New Page");
    }
}


class LoadPageButton extends Button {

    public LoadPageButton() {
        super("Load Page");
    }
}
