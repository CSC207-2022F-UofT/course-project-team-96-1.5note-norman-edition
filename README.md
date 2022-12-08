# OnePointFiveNote (Norman Edition)

A digital multi-media notebook with infinite pages. [Online Javadocs](https://csc207-2022f-uoft.github.io/course-project-team-96-1.5note-norman-edition/), [Implementation Notes](implementation_notes).

## Compiling

Building this project required a JDK &ge; version 17. OpenJDK 19 is recommended.

To compile & run on the command line, run `gradle run` from the root of the
project directory.

To compile and run in IntelliJ, make sure of the following:

* OpenJDK 19 is the project SDK:
  Navigate to File -> Project Structure -> Project and make sure "SDK" is set
  to `openjdk-19` and "Language level" is set to "SDK default."

* Gradle is configured to use the project SDK:
  Navigate to File -> Settings -> Build, Execution, Deployment -> Build Tools
  -> Gradle and make sure "Gradle JVM" is set to "Project SDK."
  
* `gradle [run]` is the selected run configuration

You can then run the program using the "Run" button or with its keyboard
shortcut (Shift-F10 by default).

## Usage

### Managing Pages

When the program starts, you will see 2 buttons: "New Page" and "Load Page." The
"New Page" button will immediately open the page screen to a new, blank page
while the "Load Page" button will open a file selection prompt. If the chose
file contains page data, the data will be loaded and displayed in the page
screen.

From the page screen, the currently open page can be saved using the "Save" or
"Save As" menu bar items under the "Page" menu in the top-left of the window.

New pages can be created/loaded from the page screen using the "New" or "Load"
menu bar items under the "Page" menu. Doing so will discard any un-saved changes
to the current page.

Using the "Close" menu bar item under the "Page" menu will close the page screen
and return to the starting screen described at the start of this section.

### Tools

The contents of the page (referred to in general as "media") are created/edited
using various tools. The available tools are displayed in the tool bar along the
top of the window. Select the active tool by pressing one of the buttons in the
tool bar. When a tool is selected, its GUI controls are shown in the tool pane
on the left/bottom side of the window.

In the sub-sections below, the individual tools are documented. When reference
is made to a button/text field/etc. in these sub-sections, they can be found in
the tool pane when the relevant tool is selected.

#### Colour

The colour tool does not modify the page. Instead it provides the colour used by
other tools. The colour can either be chosen from the colour picker dropdown, or
modified using the hue, saturation, value, and opacity sliders. Whenever a
colour is selected, it will be added to the history. Clicking a colour in the
history will restore it as the current selection.

#### Pen

The pen tool allows drawing lines of a configurable thickness on the page. Lines
are drawn by clicking and dragging on the page. Straight lines can be drawn by
holding the shift key while drawing.

#### Shape

The shape tool allows creating shapes on the page. The available shapes are:
rectangles, ellipses, and n-sided regular polygons for 3 &le; n &le; 10.

To create a shape, select the shape type in the drop-down menu and click and
drag on the page

#### Text

The text tool allows writing text into the page. Type text into the text box in
the tool pane, then click in the page to add the text. Clicking on existing text
in the page allows you to edit its content using the aforementioned text box.

#### Hyperlink

The hyperlink tool is similar to the text tool, except that it allows attaching
a URL to text. In addition to a text box for text contents, it also has a
"hyperlink" text box into which a valid URL must be written. Clicking on the
page will add a hyperlink with the configured text and URL.

Right clicking on a hyperlink while the hyperlink tool is active will open its
URL.

The text content of hyperlinks can be edited with the text tool.

#### Audio

Press the "Add new Audio" button to open a file selection dialogue from which
an audio file can be chosen. A player will then be created on the page for that
audio file.

#### Media

The media tool allows modifying properties common to all types of media. First,
select the media to edit by either clicking them in the page or clicking and
dragging to do a box selection. Media can be de-selected by holding the shift
key and clicking on them.

Once a selection is made, all selected media can be:

* Moved by clicking and dragging on part of the selection
* Renamed using the "Name" text field
* Rotated using the "Rotation" slider
* Re-ordered vertically using the "Z-Index" spinner
* Deleted using the "Delete All Selected Media" button

#### Tag

The tag tool allows adding strings of text as "tags" to the contents of the
page. To add a tag to a piece of media, click on it in the page, then write the
tag string into the "Tag Name" text field and press the "Tag" button.

#### Search

The search tool allows searching the contents of the page for media with the
given tag. To make a query, type a tag into the text field, then press the
"Search" button. The number of results is shown below the text field. To cycle
through the results, press the "Find" button.


### Navigation

The view of the current page can be moved around as well as zoomed in/out. The
view can be changed either by using the "View" menu in the menu bar, or using
the keyboard/mouse as follows:

* Scroll to move the view vertically
* Scroll while holding the shift key to move the view horizontally
* Scroll while holding the control key to zoom in/out
