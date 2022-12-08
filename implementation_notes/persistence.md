# Persistence Implementation Notes

Coverage:

![image](https://user-images.githubusercontent.com/93449583/206302512-66c18452-9e66-4cbe-b097-e4e9834d3983.png)


## Relevant Classes & Interfaces

* `storage.SQLiteStorage`
* `app.MediaStorage`
* `app.MediaCommunicator`

## Summary

Storage is implemented using a SQLite database file. The database has a column for the fields
common to all Media entities (i.e. those they inherit from the base `Media` class) as well as a
column for serialized Java object data.

The details of interfacing with SQLite are contained within the `SQLiteStorage` class.
`SQLiteStorage` implements the high-level `MediaStorage` upon which the  
`MediaCommunicator` class depends.

This allows `MediaCommunicator` to save and load Media without depending on the details
of how saving/loading are implemented.

Additionally, `MediaCommunicator` provides the facade through which Media is
added/removed/modified, but does not require that users of this API (tools, etc.) concern
themselves with saving/loading.

## Result

* The persistance (saving/loading) implementation can be changed without changing the rest
  of the program.

* Only one class (`MediaCommunicator`) has to be concerned with using the
  persistence implementation, user-facing tool classes for modifying the contents of the page
  only need to implement modifying the page.
