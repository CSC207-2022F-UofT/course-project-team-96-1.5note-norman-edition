# Relevant Classes / Interfaces:
- `app.media.MediaText`: Entity class for text data.
- `app.media.MediaImage`: Entity class for image data.
- `app.media_managers.TextModifier`: Modifies text, actually only used for hyperlink media.
- `app.media_managers.ImageModifier`: Use case class responsible for creating and modifying `MediaImage`
- `gui.media.GUITextBox`: GUI class that serves as the view for `MediaText`. Extends GUIMedia.
- `gui.media.GUIImage`: GUI class that serves as the view for `MediaImage`. Extends GUIMedia.
- `gui.tool.TextTool`: GUI class allowing the user to place down and edit the text of textboxes.
- `gui.tool.ImageTool`: GUI class that allows the user to insert images where their camera is pointing.

# Summary:
The user can interact with the Text tool or the Image tool to insert their respective type of media into the page. Text
can be pre-written in the editing box on the side then placed down repeatedly where the user clicks, then each text
box can be edited by the user at will by clicking on it to edit its contents in real time and see them updated on the
page. Image allows a user to select a from a variety of common image sources on their computer to upload images into the
page, placing its top-left corner where their camera is.

This implementation was done following the SOLID principles of design. I extended media appropriately and have including
(sometimes unused) getters and setters for all my classes for easy future extension, all in accordance with the Open/Closed.
principle. I also paid special attention to ensuring the Clean Architecture was maintained. I have followed the
proper conventions of our program and made sure that all my dependencies were pointing in the correct direction and I
tried to reduce coupling as much as possible.

As well, I worked to fight code smells. In particular, I made sure to fight against overly long methods by properly
delegating tasks to helpers, and practiced good comment usage. In the past, I have tended to overcomment and explain
basically every line of code, but I have worked hard to avoid that as much as possible here.

# Testing:

My section of the program is very difficult to test in a meaningful way, due to the large amount of user interaction and
outside input required. I have included some light tests, but they don't cover most actual useful situations.
In order to make up for this, I have spent a significant amount of time testing it myself and
attempting to create corner cases which would cause problems to occur. I encourage you to play with it yourself, as it's
quite a lot of fun: see if you can break my code!