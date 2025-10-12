# Stage 1: Data Discovery

## Formula 1 World Championship from Vopani “Rohanrao”

Sourced from Kaggle
<https://www.kaggle.com/datasets/rohanrao/formula-1-world-championship-1950-2020>

---

The dataset chosen by our group contains data on the Formula 1 (F1)
World Championship from 1950 to 2024. F1 is a world-renowned auto-racing
forum, and is widely considered a premier league for circuit racing.
The world championships considered in this dataset are entire seasons
appearing in any given year. One season comprises a series of races that
takes place on distinct circuits and public roads across the world.
The collection of races in sequence is referred to as “Grand Prix.”

### Data contained in the file(s)

The contents of this dataset are measured from the World Championships
mentioned before. Specifically, the data consists of tables
pertaining to the following:

* races
* drivers
* constructors
* qualifying sessions
* circuits
* lap times
* pit stops
* the championships overall

Constructors are the designers and builders of the cars used in races.
Some additional supporting tables are driver standings, constructor
standings and results, as well as sprint results. Sprints or sprint races
are shorter than circuits and do not require any driver to stop for pit stops.
The dataset is split into distinct files consisting of tables for the above data,
and so we may prescribe entities using those tables. Then, much of the relevant
data from the tables can be considered attributes, which we describe below.
A last file designated “status” simply carries a mapping of various statuses
describing different entities.

### Defining entities using the dataset

We treat the tables as an entity set to establish our entities.
Common attributes shared among these entities are: UIDs, which are assigned to all entities; names (for circuits,
constructors, drivers, and races); and location/nationality (for circuits,
constructors, and drivers). Entities also have IDs—such as driver ID, circuit ID, constructor ID, etc.—that reference entries in other tables like races, driver standings, results, etc. The attribute “points”
belongs to results, constructor results and standing, as well as driver standings—quantifying number of points accumulated across the seasons. In addition, driver
and constructor standings have an attribute describing the number of wins. The entities constructor standings, driver standings, lap times, and sprint results, are assigned an attribute
related to position in races and sprints. Majority of the entities except for the status entity, possess date and/or time attributes—which may describe date of birth, date of race, year of season, or duration of race. Driver entities have distinct first and last name attributes. The remaining attributes will be excluded from this model.

### On the size of the dataset, and cleaning efforts

Across all files and entity sets, there is a total number of 701,433 records.
The entity, lap times, accounts for approximately 500,000 records—reflecting the
extensive number of tracked laps across many races and seasons. Excluding the quantity of records contributed by lap times, the remaining records come from constructor results and standings,
driver standings, pit stops, qualifying races, and the results, with each having
greater than 10,000 records respectively. The remaining entity sets—circuits, constructors,
drivers, races, seasons, sprint results, and status—each have between 70 to 1,000 records.
Dataset cleaning will be necessary due to the presence of NULL values in primary attributes, such as race lengths. We can omit less-essential attributes since querying for such offers limited insight, but total race lengths can be derived from the lap times of the last-place driver
for any race. This would provide an estimate when the race should end, assuming
the race did not end due to time constraints. Fortunately, there is a lot of data
to work with in this dataset—conveniently allowing us to remove any attributes that
are riddled with NULL values and no sensible default value to replace them with.
To this end, we may write a python script to simply remove any columns with NULL
values. Otherwise, for attributes that can support a working default value, we can
simply replace any NULL values with our chosen default. Creating a script to do this
is straightforward. In order to start with the process, we will create a separate script
to discover attributes that contain NULL values. Upon the collection of such attributes, we will decide on an individual basis whether or not we should omit the attribute or substitute in a
valid default value—if available.
