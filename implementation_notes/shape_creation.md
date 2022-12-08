# Shape Creation / Highlighting Implementation Notes

## Relevant Classes & Interfaces

* `GenericShape`
  * `RectangleShape`
  * `ElipseShape`
  * `PolygonShape`
* `GUIShape`
  * `GUIRectangle`
  * `GUIEllipse`
  * `GUIPolygon`
* `ShapeTool`
* `GUIShapeFactory`

## Summary

The`ShapeTool` operates in the Frameworks and Drivers layer, and is graphical interface a user can use to create shapes.
It registers the `JavaFX` mouse and keystroke events relevant. When a user first clicks the page using the tool, a 
`GUIShape` is instantiated, which constructs a `GenericShape` in its constructor. It is then added to the `Page`, to be 
rendered.

A `GenericShape` contains information about the shape such as its position, orientation and dimensions. 
This information can be useful to other classes in the program, such as `Page`, `MediaStorage`, `MediaCommunicator`,
which need to know where the shape is to determine whether to draw it. It effectively functions as a core entity class.

A `GUIShape` holds a reference to its corresponding `GenericShape` and may access its attributes, which it uses to
define how the shape should be drawn. Its purpose is to separate the GUI from the core entity classes;
how we draw a shape should not be concerned with how we load and store it (Dependency Inversion).

While the user drags the mouse, `JavaFX` calls the `ShapeTool` to update the shape. This call is then passed to
the `Page` instance, which updates its media layer, for rendering.

When a shape is loaded via the `GUIMediaFactory`, the `GUIShapeFactory` is used to create the exact shape type that is 
needed. This way, the `GUIMediaFactory` does not need to concern itself with which type of shape it is loading, and
allows for the future addition of different shape types (Dependency Inversion).

Note:
* `GUIRectangle`, `GUIEllipse` and `GUIPolygon` are all children of `GUIShape`, 
and utilize different attributes and methods specific to their shape type. These are good examples of the
Liskov Substitution and open-closed principles.

* `RectangleShape`, `EllipseShape` and `PolygonShape` are all children of `GenericShape`,
and utilize different attributes and methods specific to their shape type. These are good examples of the
Liskov Substitution and open-closed principles.

* Shapes are designed to be modular, and do not have any outwards dependencies, in line with clean architecture.
  * *Frameworks and Adapters*: `Shapetool`, `GUIShapeFactory`
  * *Application Business Rules*: `GUIShape` and its implementations
  * *Enterprise Business Rules*: `GenericShape` and its implementations

* We considered whether to use the `ToolBarController` (*Interface adapter*) and 
`MediaCommunicator` (*Application Business Rules*) as intermediates in the creation of shapes, but decided against it.
The following reasons motivated this decision:
  * The `ShapeTool` must maintain a reference to the shape it's currently modifying, so a preview may be seen while
  dragging the mouse. No other class must maintain such a reference, so it is unnecessary to add "middlemen".
  * Only the `MediaTool` is concerned with moving the shapes within the page (updating their attributes), and has 
  defined methods to do so.

## Result
* Modification of the shape's implementation will not affect the rest of the program
  * Shapes are effectively addons to `GUIMedia` and `Media`, and will not affect their functioning.
  * Creation can be done through various means, such as through the shape tool, or through loading, and their precise 
  implementation does not matter.
  * Rendering and loading/saving to storage are effectively handled by the rest of the program.

## Test Case Coverage
Test case coverage is good, with most classes being tested near 90% line coverage. Attached is a summary:

