Interface referenced from [stage 6 initial UI diagram ](https://github.com/jsahota11/databases-group-proj/blob/main/stage-6/ui-diagrams/ui-design.md)

---
# Main Menu
All "pages" below refer to the list of queries possible, grouped into **GENERAL** vs. **SPECIFIC** information.
> Basically specific ones are tied to a certain entity
## Query + Functionality Pages
Error-checking added for user inputs, provide feedback with error messages. ==TODO: Error messages could still be further refined for clarity==

*NOTE: There are 34 empty query methods currently written in the program*

- Drivers
	- When selected, 16 queries are listed 
	- Divided into two pages to ease cognitive load
		- Added a "(1/n)" label to the header to inform user
		- User can opt to switch between two pages or go to main menu
- Races
	- When selected, 4 queries are listed
- Circuits
	- When selected, 5 queries are listed
- Constructors
	- When selected, 7 queries are listed
- General Statistics
	- When selected, 3 queries are listed
- Help
	- Currently expects user input to go back to main menu
		- Doing this to simulate it "being on that page" and remaining there until user wishes to exit
		- `If ever we have a long list, we can always implement pages like in Drivers`

**COMMON FEATURES** Users can opt to go back to main menu

---
## Design choice changes + ideas
- For circuit hemisphere queries
	- User will only have to type in "south," "north," "west," " east." We can just hardcode coordinate values that will be input into the SQL query based on input conditions
		- E.g. if it was north, we can just define the lat ? and long ? ourselves to match
		- ~~Unless this was already the plan oops~~
- For oldest/youngest query
	- I made it so that the query method takes in a `String` for the "oldest/youngest" along with the ID
		- Like the hemispheres, we can use conditions to manipulate the query done
- Perhaps implement a look-up command for ID's
	- E.g. users can look-up driverID's or constructorID's that match a `String input`
	- Was planning something like
	- Let me know if this can be done ==@Jatinder==, I believe we did some kind of look-up in A3 so just a simple query like that. This is just so it makes it easier to design on our end while providing help to the user. **Command can be ran anytime, I can try to modify the code so you can.**
		- I planned to also add print messages that remind users what they have looked up maybe? Or what information they have so far? I'm not sure, depends what you guys thing
	
		```
		? d firstName lastName // will do a simple look-up query and print to user
								// to use as reference for query commands
								
		// or something like
		? c constructorName
		
		// I can work on the error checking, but basically the first two tokens
		// are command keywords and every token after is search input
		```

---
## RE: Empty SQL Methods
Please work the with method under the assumption that all inputted parameters are *correct*. I have tried to write and structure the code so that the SQL-related codes can just be plugged in without much junk inside the method for error-checking, etc.

If you have any questions, please let me know!

---
## TODO's and future plans:
- More meaningful error messages
- More feedback to inform user choice to help with memorability + navigation
- Polish up some methods (if there's time)
- Testing!! Testing!! Testing!! I haven't fully tested everything as a lot has yet to be implemented and I wanted to get something to y'all by today (Friday Nov. 28)
	- Need to test for bugs for user inputs + UI navigation
		- What I've tested so far kind of works (e.g. `queryDriver()` and `runHelpMenu()`), not sure about the rest if they work just fine
- Need to complete the **Help Menu**, can you guys let me know what you want added there? `@Jatinder, @Ayesha`
	- I assume something like the look-up feature could be added here if you guys are cool with it, but let me know what else1
