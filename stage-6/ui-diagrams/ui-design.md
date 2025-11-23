# UI Diagrams
This models how the text prompts and output will look in our CLI. The pages here correspond to the flowchart of our REPL. Anything in [] brackets should be filled out respective to the text inside the brackets. We have written it this way for brevity and to avoid duplication.

## Opening Page
```
--- F1 DATA ---

Welcome! Select a numbered option below to get started.

1. Information on Drivers
2. Information on Constructors
3. Information on Circuits
4. Information on Races
5. General Statistics
6. Help Menu
7. Quit application

Enter your choice: 
```

## Help Menu
```
--- HELP MENU ---

Select a numbered option below for help with using this application!

1. How this application works
2. List of all available information, and where to find it
```

## [Entity] Options
Here, [entity] corresponds to the options in our write-up and flowchart (i.e. Driver, Constructor, Circuit, Race). 
```
--- INFORMATION ON [ENTITIES] ---

Here's the information you can get about [entities]! Please identify the specific [entity] you would like to get information on:
```
Here, the program will prompt the user to insert a name for Driver, Constructor, and Circuit, and a season and round number for Race. 
```
Great, here's the information we have on [entity name]. Select one of the numbered options below to get the information you'd like.

1. [query 1]
2. [query 2]
3. [query 3]
[...]

Enter your choice (or enter q to quit to the opening page): 
```
## Requested Output
```
--- REQUESTED INFORMATION ---

Here is the information you asked for: 

[Query results]

Enter q to quit and return to the opening page: 
```