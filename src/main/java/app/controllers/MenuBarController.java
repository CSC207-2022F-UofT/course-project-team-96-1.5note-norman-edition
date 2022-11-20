package app.controllers;

import app.interaction_managers.Destroyer;

public class MenuBarController {

//    obsolete // percentageZoom is when they select a percentage, input will contain that percentage
//    public static void percentageZoom(gui.page.Page page, javafx.scene.Node input) {
//        Zoomer zoomer = new Zoomer(page);
//        zoomer.interact(input);
//    }

//    //incrementZoom is when they click the button, input will be button click? whether it's pos or neg i guess. + or - 1
//    public void incrementZoom(app.media.Page page, javafx.scene.Node input) {
//        app.interaction_managers.Zoomer zoomer = new app.interaction_managers.Zoomer(page);
//        zoomer.interact(input);
//    }

    public void destroy(javafx.scene.Node userInput) {
        Destroyer destroyer = new Destroyer();
        destroyer.interact(userInput);
    }
}
