# Final Report
## Questions (to be deleted later)
- A cover page with the names and userids of all group members
- A 1 paragraph introduction to your project
- A summary of the data: Why was it chosen? What does it consist of (attributes)? How large is it(number of records)? Was any cleaning/pre-processing required? Don’t forget to acknowledge your sources! Include an ER diagram.
- A discussion of the data model:
  - Why was it broken down into those tables?
  - Did you face any difficult choices when designing the model (e.g., tricky participation/cardinality ratio decisions)?
  - Did the data model cleanly fit into the relational database?
  - Do you regret any decisions you made in your model? Did you change the model you initially designed when it came time to implement? What changes, and why?
  - Could the data be modelled in a different way, why or why not? Given the work completed, would you choose this model?
- A discussion of the database (flavour of SQL, etc.)
- A description of your interface, including a brief description of platform/language used, and screenshots of the interface in action.
- A list of interesting queries you can run using the interface. Explain what the queries return, you don’t have to include the SQL code. Explain why these queries would be interesting to an analyst.
- Concluding remarks:
  - Does this dataset require a relational database? Would other database systems be a better choice in modelling this data? Why or why not? Would the “interesting queries” you wrote be easier or harder to re-create if you were using an alternative database? Would other database systems allow for different or more interesting queries?
  - Would this database be a good teaching tool for COMP 3380? Are there good problems for future students to solve in this database?
  - A final summary paragraph
- Appendix: A summary of each group member’s contributions to the project (from Stage 1 to the end).

## Table of Contents

1. [Introduction](#introduction)
2. [Data](#data)
3. [Database](#database)
4. [Interface](#interface)
5. [Conclusion](#conclusion)
6. [Appendix](#appendix)


## Introduction
insert intro here

## Data
### some stuff from stage 1 write-up
## Formula 1 World Championship from Vopani “Rohanrao”

Sourced from Kaggle
<https://www.kaggle.com/datasets/rohanrao/formula-1-world-championship-1950-2020>

---

The dataset chosen by our group contains data on the Formula 1 (F1) World Championship from 1950 to 2024. F1 is a world-renowned auto-racing forum, and is widely considered a premier league for circuit racing. The world championships considered in this dataset are entire seasons appearing in any given year. One season comprises a series of races that takes place on distinct circuits and public roads across the world. The collection of races in sequence is referred to as “Grand Prix.”

__Data contained in the file(s)__

The contents of this dataset are measured from the World Championships mentioned before. Specifically, the data consists of tables pertaining to the following:

* races
* drivers
* constructors
* qualifying sessions
* circuits
* lap times
* pit stops
* the championships overall

Constructors are the designers and builders of the cars used in races. Some additional supporting tables are driver standings, constructor standings and results, as well as sprint results. Sprints or sprint races are shorter than circuits and do not require any driver to stop for pit stops.The dataset is split into distinct files consisting of tables for the above data, and so we may prescribe entities using those tables. Then, much of the relevant data from the tables can be considered attributes, which we describe below. A last file designated “status” simply carries a mapping of various statuses describing different entities.

__Defining entities using the dataset__

We treat the tables as an entity set to establish our entities. Common attributes shared among these entities are: UIDs, which are assigned to all entities; names (for circuits, constructors, drivers, and races); and location/nationality (for circuits, constructors, and drivers). Entities also have IDs—such as driver ID, circuit ID, constructor ID, etc.—that reference entries in other tables like races, driver standings, results, etc. The attribute “points” belongs to results, constructor results and standing, as well as driver standings—quantifying number of points accumulated across the seasons. In addition, driver and constructor standings have an attribute describing the number of wins. The entities constructor standings, driver standings, lap times, and sprint results, are assigned an attribute related to position in races and sprints. Majority of the entities except for the status entity, possess date and/or time attributes—which may describe date of birth, date of race, year of season, or duration of race. Driver entities have distinct first and last name attributes. The remaining attributes will be excluded from this model.

__On the size of the dataset, and cleaning efforts__

Across all files and entity sets, there is a total number of 701,433 records. The entity, lap times, accounts for approximately 500,000 records—reflecting the extensive number of tracked laps across many races and seasons. Excluding the quantity of records contributed by lap times, the remaining records come from constructor results and standings, driver standings, pit stops, qualifying races, and the results, with each having greater than 10,000 records respectively. The remaining entity sets—circuits, constructors, drivers, races, seasons, sprint results, and status—each have between 70 to 1,000 records. Dataset cleaning will be necessary due to the presence of NULL values in primary attributes, such as race lengths. We can omit less-essential attributes since querying for such offers limited insight, but total race lengths can be derived from the lap times of the last-place driver for any race. This would provide an estimate when the race should end, assuming the race did not end due to time constraints. Fortunately, there is a lot of data to work with in this dataset—conveniently allowing us to remove any attributes that are riddled with NULL values and no sensible default value to replace them with. To this end, we may write a python script to simply remove any columns with NULL values. Otherwise, for attributes that can support a working default value, we can simply replace any NULL values with our chosen default. Creating a script to do this is straightforward. In order to start with the process, we will create a separate script to discover attributes that contain NULL values. Upon the collection of such attributes, we will decide on an individual basis whether or not we should omit the attribute or substitute in a valid default value—if available.


## Database
### some stuff from stage 3 write-up (to be deleted later)
As explained in the summary of our data, most of our entities are organized the same way our dataset is. Most of the design decisions were made regarding the relationships between them. A few of them are fairly straightforward (a Constructor has many Drivers within it, so of course there would be a relationship there). The relationships "Part of", "Happens on", and "Makes up" fall into this category. However, the rest are a bit more complicated.

We initially had Lap solely related to Race, as it felt the most straightforward. That created the problem of needing an additional relationship with Driver since Lap needs foreign keys from both, so we decided instead to make "Recorded in" a ternary relationship. We debated back and forth about having an entity for the championship(s), and also whether we should separate the driver and constructor championships. In the end, we elected to make it 2 relationships because every Season needs to have both championships, but the championships on their own didn't have any attributes. The most complicated relationship is "Races in". This holds all the information from the qualifying format that determines precedes the race, the constructor each driver is racing for, the results of the race (final position and rank), and the points that both the driver and their respective constructor accumulates. We tried very hard to break this into some more constituent parts, but so many of these parts are integral to each race, so consolidating it into one relationship between Driver and Race seemed to make the most sense.

We decided to set the following cardinality constraints for our different relationships: driver races in race, with M-N; race happens on circuit, with M-1; race makes up season, with M-1; driver part of constructor, with M-N; lap is recorded in race and driver, with M-1-1; driver participates in driver championship, with M-N; and driver participates in constructor championship, with M-N. The justifications are as follows: driver and races have a M-N relationship, as a single driver can participate in many races, and a single race has many drivers; race and circuits have an M-1 relationship, as a single particular race can only happen on one circuit, and one circuit can host multiple races; race and season have an M-1 because many races combined form a specific, single season;  driver and constructor have an M-N relationship, since drivers can be a part of multiple constructors in one season, and a single constructor is composed of multiple drivers;  laps, race, and drive have an M-1-1 relationship, due to the fact that a single instance of a driver and race can define many laps, whereas a single lap instance is tied solely to one driver in one particular race; driver has a M-N relationship for both driver and constructor championships, as a single driver participates in both driver and constructor championships in a single season, and a each championships has many drivers competing.

## Interface
### some stuff from the stage 6 write-up (to be deleted later)
Our interface will be a text interface in the form of a REPL. We will be programming it using Java, and it will run directly in the terminal. You will be able to move between categories of queries/information, and will be able to request specific information within each category with given text prompts. There will be a help menu for guidance on what information is available, and how to get there.

__Overall Functionality__

The user will interact with the application through text prompts, guided by the interface. They will be given numbered queries for each category, and will submit the required data to run each query. We will also have a help menu, the main feature of which will be a list of all queries and their categories. This allows a user to search for if the information they're looking for is available in our application, and what category they need to access to get there. 

We have structured our queries in such a way that large volumes of data should not be returned very often. We will try to ensure that our application can handle such output, but it should not be an issue and so we will not put a huge focus on it.

__Categories__
For drivers and constructors, you are able to choose a specific driver/constructor by name and view all the available information regarding their performance and career in F1. Since we are not using the primary key here for user's convenience, there is a possibility that multiple results will be returned, in particular for driver statistics and information. The data displayed should be fairly short so there will not be an issue displaying multiple entities, and we will ensure our program is able to do so. There is a similar category for races/rounds. As stated in earlier stages, we use round and race somewhat synonymously since a user would identify a particular race by the season and round (each round has one particular race). The last category is the circuits. The user would input a specific season by year, and a round by a number from 1 to 12, and can then ask for information regarding the race of that specific round. There will be one more category for overarching statistics. This is where a lot of our most interesting queries will be, as they aggregate or calculate results over a broader range (e.g. over a season, over the lifetime of a driver, or over the entirety of F1). Again, inputs will be asked for in a similar fashion to the above category. 

## Conclusion


## Appendix
### Stage 1

### Stage 2

### Stage 3

### Stage 4

### Stage 5

### Stage 6

### Final Stage