# Zooming and Scrolling Implementation Notes

## Relevant Classes & Interfaces

* `gui.page.Page`
* `gui.start_screen.StartScreen`
* `gui.Zoomable`

## Summary

The first main way to change the view is through the Menu Bar, which has a View menu. This menu is provided by the Menu and MenuItem JavaFX classes. We then assign methods as the actions to execute when the buttons are clicked. The zoomTo, zoomInOrOut, and centerPage methods in StartScreen then call methods in the Page class.

The second main way is by scrolling manually on the page. This functionality is implemented using Event Handlers for scrolling on the Page (ScrollHandler), with different helper methods depending on whether shift or control are held down. These helper methods then call the main zooming and scrolling methods in Page.

Zooming and scrolling are implemented by scaling and translating the mediaLayer of Page respectively. Transforming the Page itself was attempted previously, but ultimately my choice to transform the mediaLayer was made to create an infinite page for each file (similar to OneNote), rather than a page with a fixed size. Page and mediaLayer are both subclasses of the JavaFX class Node, which has methods setTranslateX, setTranslateY, and can also getTransforms which takes the x/y direction scaling factors from Scale objects.

Originally, when I was still figuring out which object to transform (PageScreen, StartScreen, Page, and mediaLayer which are all of different types), and also when I was experimenting with transforming Page vs mediaLayer later on, I kept having to make small changes in different places to the types of classes I was using because Page is a StackPane while mediaLayer is a Pane. This would be the shotgun surgery code smell. I fixed this code smell by having Page implement the Zoomable interface, which just defines the most barebones zooming and scrolling methods a class would need to alter its view. This way, I also follow the Dependency Inversion Principle from SOLID by not having StartScreen methods directly call methods from Page. This would allow me to easily create other Zoomable objects if needed, allowing me to extend the project.

Since I don't have any core data classes to actually modify, I did all my functionality in the GUI layer, using an interface between my two main classes. Thus, my code does not violate any dependency rules. Further, there is no two-way dependency as only StartScreen calls methods in Page, while Page does not call any methods in StartScreen.


## Test line coverage

* TestZoomScroll fully tests all 6 major zooming/scrolling methods in the Page class that I am able to test without simulating mouse clicks or scrolls. The jumpToCenter method isn't tested as I cannot find the visible bounds when I only have the Page, but I don't actually use it in any of my view changing operations (I implemented it as it would be a useful "view"-related method for other functionality like searching). Tests cover 84/173 lines in Page (Page has other methods aside from zooming and scrolling), and this includes 48/55 (87%) lines of testable major zoom/scrolling methods.