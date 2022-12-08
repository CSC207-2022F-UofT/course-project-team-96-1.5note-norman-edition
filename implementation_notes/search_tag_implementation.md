# Searching and Tagging Implementation Notes

## Relevant Classes & Interfaces

* `Searcher`
* `Tagger`
* `SearchBarController`
* `ToolBarController`
* `TagTool`
* `SearchTool`

## Summary

The `TagTool` is a class that operates in the Frameworks and Drivers layer in reference to clean architecture. This is a graphical interface that a user can
use to create tags for objects such as shapes, text, audio, etc. The tool contains 3 components: a textfield that takes in user input, a tagging button and a button to
remove the specified tag. Within the tool, there is also a label that notifies the user of the tags of a clicked object so that they know exactly what they have tagged the object with.
Similarly, the `SearchTool` is a class that also operates within the Frameworks and Drivers layer and is a graphical interface as well. This tool contains a textfield to take in the
searches of the user, a search button as well as a button called "Find" that will readjust the page to jump to the searched result(s). The tool also contains a label that will
show the user exactly how many results are found from their search on the current page.

The `ToolBarController` is a class that operates in the Interface Adapters layer in reference to clean architecture. This is a controller that passes values from the GUI onto 
the use cases. This class was necessary in order to adhere to clean architecture, more specifically to be able to keep the business logic separate from the delivery mechanism. This
class contains two methods that the tag feature uses, and those methods relate to tagging an object and removing a tag. The `SearchBarController` is a class that also operates
in the Interface Adapters layer

The `Tagger` is a use case class that will do the actual tagging and removing of media entities.


