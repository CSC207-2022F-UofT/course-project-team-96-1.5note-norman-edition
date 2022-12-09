# Shape Creation / Highlighting Implementation Notes

## Relevant Classes & Interfaces

* `app.media.GenericShape`
  * `app.media.RectangleShape`
  * `app.media.ElipseShape`
  * `app.media.PolygonShape`
* `gui.media.GUIShape`
  * `gui.media.GUIRectangle`
  * `gui.media.GUIEllipse`
  * `gui.media.GUIPolygon`
* `gui.tool.ShapeTool`
  * `gui.tool.ShapeType`
* `gui.media.GUIShapeFactory`

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
the `Page` instance, which updates its media layer, for rendering. Once a user has stopped dragging the mouse, the
shape is no longer updated and is effectively finished.

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

* Shapes have their own "Nodes" (JavaFX functionality) which they are drawn onto. Any transformation from say, 
* the media tool, will affect this node and not the shape itself. It could be thought of as a parent.

* The `ShapeType` enum is used by the settings pannel and the tool to determine what shape should be created.

* Shapes are designed to be modular, and do not have any outwards dependencies, in line with clean architecture.
  * *Frameworks and Adapters*: `Shapetool`, `GUIShapeFactory`, `GUIShape` and its implementations
  * *Enterprise Business Rules*: `GenericShape` and its implementations
  * Intermediate layers were determined as unnecessary, as `GenericShape` already has getter and setter methods, and 
  it does not depend on anything else. Outer classes like `GUIShape` may depend on it, but not the other way around.

## Result
* Modification of the shape's implementation will not affect the rest of the program
  * Shapes are effectively addons to `GUIMedia` and `Media`, and will not affect their functioning.
  * Creation can be done through various means, such as through the shape tool, or through loading, and their precise 
  implementation does not matter.
  * Rendering and loading/saving to storage are effectively handled by the rest of the program.

## Test Case Coverage
Test case coverage is good, with relevant classes being tested near 90% line coverage. Attached is a summary:

Test Coverage Header:
![image](https://user-images.githubusercontent.com/39686698/206595011-081ebdcf-058e-41c8-b183-0e1dff1a1d69.png)

Test Coverage:
![image](https://user-images.githubusercontent.com/39686698/206595206-30bc0e49-56a1-462f-b01e-96e54e2e2007.png)
![image](https://user-images.githubusercontent.com/39686698/206595386-e2791714-6258-44d9-b41c-4afae2dcdd11.png)
![image](https://user-images.githubusercontent.com/39686698/206595427-392adef9-55da-4949-8d1e-5d96c29513f2.png)
