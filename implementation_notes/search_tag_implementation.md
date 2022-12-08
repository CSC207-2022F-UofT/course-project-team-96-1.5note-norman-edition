# Searching and Tagging Implementation Notes

## Relevant Classes & Interfaces

* `Searcher`
* `Tagger`
* `SearchBarController`
* `ToolBarController`
* `InteractionManager`
* `TagTool`
* `SearchTool`

## Summary

The `TagTool` is a class that operates in the Frameworks and Drivers layer in reference to clean architecture. This is a graphical interface that a user can
use to create tags for objects such as shapes, text, audio, etc. The tool contains 3 components: a textfield that takes in user input, a tagging button and a button to
remove the specified tag. Within the tool, there is also a label that notifies the user of the tags of a clicked object so that they know exactly what they have tagged the object with. Similarly, the `SearchTool` is a class that also operates within the Frameworks and Drivers layer and is a graphical interface as well. This tool contains a textfield to take in the searches of the user, a search button as well as a button called "Find" that will readjust the page to jump to the searched result(s). The tool also contains a label that will show the user exactly how many results are found from their search on the current page.

The `ToolBarController` is a class that operates in the Interface Adapters layer in reference to clean architecture. This is a controller that passes values from the GUI onto the use cases. This class was necessary in order to adhere to clean architecture, more specifically to be able to keep the business logic separate from the delivery mechanism. This class contains two methods that the tag feature uses, and those methods relate to tagging an object and removing a tag. The `SearchBarController` is a class that also operates in the Interface Adapters layer, and works similarly to the `ToolBarController`. This controller class calls upon the `Searcher` class in order to get the necessary results, as well as their x and y positions so that we will be able to use those coordinates to "jump" to that object when called for.

The `InteractionMAnager` is an interface that both the `Searcher` and `Tagger` classes use, as it allows both these classes to take in string input of user queries and transform them into their respective attributes.

The `Tagger` class has methods that access media entities and are able modify the media's attributes, more specifically their set of tags. The tagger class has methods to add and remove tags. The `Searcher` class also has access to media entities and has methods to search within all media on the page and fetch their attributes such as their positions which are necessary to be able to find them on the page. The `Searcher` class also looks for a match from the user's searches within tags, and more specifically, contents within text media.

## Updates from Initial Design

There weren't many updates to my initial design other than removing a class called `MediaManager` that acted like an additional controller class between the use case layer and the entities, as well as simplifying the searching feature. I simplified the searching feature as initially, I had the results pop up on an external window of the application but I soon realized that implementing this desing isn't very user friendly. Additionally, it would be hard to decide what should show up within the results window since there are many things that the searcher could return, an example being a pen stroke. Instead, I had the number of results of the search shown within the settings of the `SearchTool` and created a button to cycle between and jump to the speicifc results. 

## Clean Architecture

My design of my implementation didn't stray too far from the model that I came up with using the CRC cards in a previous milestone, although our group realized that a class that we had before called `MediaManager` wasn't necessary for us to use as in reference to clean architecture, most of our classes that were in the use case layer could just access attributes of our entities rather than use this `MediaManager` class as a buffer to access them. I made sure to keep my design simple and focused primarily on making sure that my implementation followed clean architecture.

## SOLID Design and Principles

Starting off with Single Responsibility, I feel that most of my classes implemented this principle, and I specifically implemented it by having searching and tagging in separate classes. Although they both have similarities, it was important that they become different classes so that it was easier to understand the purpose of the class as well as make sure that testing these specific functions would be a lot more concise. It was also a lot more functional in terms of organization. I also implemented this principle in my controller classes as well, just so that each controller class belonged to a single feature. 

In my case, I didn't think it was necessary to have the Open-Closed feature/principle implemented in my design as my designs were pretty independent and did not require this. I also did not have to implement Interface Segregation as I only had one interface with a singular method.

A challenge that I did face was trying to adhere to the Liskov Substitution principle, since I wasn't sure how to implement my interface called `InteractionManager`. At first, I had the method within the interface take in a node of a JavaFX object, but after some consideration, if the application were to shift and use a completely different library for the gui, I would have to modify this interface and the classes that use it accordingly, so I tried to make it more generic by having a string input instead of a specific JavaFX object.

I tried to implement dependency inversion as I did not want my gui to depend on my use cases. I made sure that my controller classes instead had some type of abstraction in order to prevent this.

## Code Smells

Some code smells I had were the Middle Man, Shotgun Surgery as well as Duplicate Code. I was able to solve the Middle Man problem by removing the class entirely, that class being `Media Manager` as I referenced earlier above. I also fixed the Shotgun Surgery problem as it was prominent in my interface, and instead I made my method within the interface more generic instead to prevent these changes from occuring in different places of my code. I had the problem of Duplicate Code within my searcher class when trying to find the x and y positions of my media objects, so I fixed this by having one method and storing these values into attributes instead of returining separate arrays from similar code.

## Design Patterns

Design patterns that I tried to incorporate in my design are the Observer design pattern and the Command design pattern. I tried to implement the Observer design pattern mostly in the Interface Adapters layer of my program, where I made sure to update my gui according to what is being searched or tagged. I also used the Command design pattern as they both have methods that are must be called by the caller in order to execute their main purpose.

## Test Coverage

For my implementation, I was able to provide tests for most of the classes I created, although I was unable to test anything related to the GUI (tests related to `SearchTool` and `TagTool`) as testing with JavaFX seemed to be more complex than regular JUnit testing. I provided tests for all of the business logic and attached are some screenshots of the coverage of my tests.

