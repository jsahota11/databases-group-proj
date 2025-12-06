This README relates to the Project Submission.

TL;DR
To start the project, type `make run` at the root of the unzipped folder (on the same level as the makefile).
Instructions are on screen, just enter the number corresponding to any menu option to interact with the program.
To rebuild and repopulate the DB, run the program and enter 12 on the main menu (try not to).

`make build` (or just `make`) will compile a JAR file which will be sourced once you use `make run`.
Alternatively, `build` is in the recipe of `run` so you can also just use `make run` if you'd like.
If you decide to `make clean`, then `build` will have to run before `run`.

`make build` has already been invoked before submitting, so starting the program shouldn't take long.
You only need to type `make run` to run the program.

Upon starting the application, you will be greeted with an interface. The instructions are contained
in the main menu, including any options and interactive capabilities. Pick any menu option, and enter
the corresponding option number into the terminal. From there, follow the on-screen requests to query for data.
This may involve entering various additional data to query on (like a year), or it will simply print the results.

Below I note some notable menu options:

10 will open the help menu, which explains in more depth how to interact with the UI.
11 will exit the application and terminate the process.

12 will repopulate the database. In particular, 12 will drop all tables and begin repopulation of the DB IMMEDIATELY upon
entering 12 (well, after clicking "enter"). Our dataset is quite large, and so repopulation is not quick. Please do not
repopulate unless something in the data breaks. We went to great efforts to ensure no such breaking is possible.

The database is repopulated through a series of SQL commands. The SQL files are included with the submission.

You do not need to create or populate any databases for our program. Everything has been set up in advance.

Included is an auth.cfg file so the Database can connect to Uranium for querying. The implementation allows for automated
connection upon running the application, so you do not need to do anything.

Below are my credentials for the Uranium DB, but I will say again that you do not need to tamper with any files
or use these for our application, they are already being employed in the software.
User: sahotaj2
Pass: 7968392
