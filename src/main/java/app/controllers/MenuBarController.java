package app.controllers;

import app.interaction_managers.Destroyer;
import app.interaction_managers.Zoomer;

public class MenuBarController {
    public void zoom() {
        Zoomer zoomer = new Zoomer();
    }

    public void destroy() {
        Destroyer destroyer = new Destroyer();
    }
}
