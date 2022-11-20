package app.controllers;

import app.interaction_managers.Destroyer;
import app.interaction_managers.Zoomer;
import app.interaction_managers.ZoomerControllerInt;

public class MenuBarController {

    // TODO string vs node input, have to figure out how UI stuff works when that's there
    // TODO which page to take in
    // percentageZoom is when they select a percentage, input will contain that percentage
    public static void percentageZoom(app.media.Page page, javafx.scene.Node input) {
        Zoomer zoomer = new Zoomer(page);
        zoomer.interact(input);
    }

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
