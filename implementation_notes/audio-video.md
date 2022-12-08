# Relevant Classes / Interfaces:
- `app.media.MediaAudio`: Entity class for audio data
- `app.media.MediaVideo`: Entity class for video data. Extends `MediaAudio`
- `app.media_managers.AudioModifier`: Use case class responsibile for creating and modifying `MediaAudio`
- `app.media_managers.VideoModifier`: Use case class responsibile for creating and modifying `MediaVideo`
- `gui.media.Playable`: Interface for UI that has elements manipulating some playable media
- `gui.media.GUIAudio`: GUI class that serves as the view for `MediaAudio` in the MVC model. Implements `Playable`
- `gui.media.GUIVideo`: GUI class that serves as the view for `MediaVideo` in the MVC model. Extends `GUIAudio`
- `gui.view_controllers.GUIPlayerController`: Controller class in the MVC model allowing the user to interact with audio / video
- `gui.model.GUIPlayerModel`: Model class in the MVC model that manipulates the view via dependency inversion.
- `gui.tool.AudioTool`: GUI class allowing the user to add audio, select audio players on the page, and manage timestamps
- `gui.tool.VideoTool`: GUI class allowing user to manage videos. Extends `AudioTool`
# Summary:
The user is able to interact with an Audio or Video tool to insert audio or media onto the page respectively. Clicking on a player allows the user
to add or remove timestamps associated with the clicked player via the relevant tool. 

A player exists allowing the user to manipulate playable media as they please. They can play, redo, fast foward, change the play rate, and change the volume
of any playable media on the page. The process as a whole works off of a MVC model. 

Video implementation was done by following the open/closed principle. Anywhere where functionality would have had to change to accomodate a difference between audio 
and video was instead implemented by overriding relevant methods in parent classes.

# Testing:
Testing aimed to focus on classes on the use case layer and partially at the GUI layer as they covered the most actual 'functionality' of the program. 
However, due to multiple issues with threading, a lot of test cases report failures. Some also fail due to files not being found despite the file blatantly being there
(the logic used for opening files in tests is also completely seperated from how it is done in the program). Failing tests have been kept in as a means of 
showing what / how testing for relevant classes would have occured (mainly because they are so numerous). 
# Coverage:
![image](https://i.imgur.com/isRkKic.png)
- Half of `AudioModifier` and efffectively all of `VideoModifier` consist of 1 method which uses a JavaFX class for file loading, which is incompatible with junit.
An alternative method was written in the test class to counteract
![image](https://i.imgur.com/7ZBBbMe.png)
![image](https://i.imgur.com/hidW14t.png)
- Much of GUIAudio is events, which can't quite be covered by junit
