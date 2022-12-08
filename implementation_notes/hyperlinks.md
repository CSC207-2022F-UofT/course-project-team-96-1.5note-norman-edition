# Hyperlinks Implementation Notes

# Relevant Classes and Interfaces
* `app.Hyperlink`
* `media.GUIHyperlinkBox`
* `tool.HyperlinkTool`

# Summary
The user can add text to the page that when right-clicked opens a website. The user can also change the colour of the hyperlinks to make them distinguishible on the page.
The program uses link validation to ensure that only valid links are added to the page and clicked on the page.

The hyperlink classes only have inward pointing dependencies or are dependent on classes on the same layer. Care was taken to also not violate SOLID principles. The
biggest design issue of this feature is the shotgun surgery needed whenever a feature is implemented for text, it would also need to be implemented for hyperlinks 
(eg: adding a change font size feature, code would need to be added to both the text and hyperlink classes). Admitedly, this code smell could be fixed by refactoring
the code and having the hyperlink classes extend the corresponding text classes (which was how it was initially). However, given the time constraint of the assignment, having
multiple people ended up working on the same classes simultaneously to implement their features and this resulted in merging conflicts. These merging conflicts and all
future ones (for the duration of this project) were resolved by removing inheritance relationships to classes that were still being worked on.

## Testing
Test coverage for the  `app.Hyperlink` and `media.GUIHyperlinkBox` were thorough as shown through the Intellij coverage tests. The test coverage for 
`tool.HyperlinkTool` is low for a few reasons. Firstly, a lot of the methods depend on a mouse event which are difficult to test. Secondly, some of the methods launch
a new window or alert message which is difficult to test for using junit.


