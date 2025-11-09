# Queries with explanations

Jatinder Sahota, Ayesha Qadir, Kriza Cyrene del Moro
10/26/25

Note that for all these queries, we use the season and round to have users identify races. In most cases, we use race/round almost synonymously. 

Constructors a driver has been a part of
* Lists all the constructors a driver has ever competed with

Winner of the Driver Championship for a season
* Gets the driver with the most points for a specific season

Winner of the Constructor Championship for a season
* Gets constructor with the most points for a specific season

Winner of a specific race/round
* Gets the driver who finished first in the race for a specific round

Number of points earned by a driver (respectively, constructor) during a particular round
* Takes either a driver or constructor (this is technically 2 different queries) and a specific season and round and returns the number of points they earned in the race for that round

Position during a specific lap
* Returns the position a driver was in in during a specific lap of a specific race

A driver's fastest lap in a round/race
* Takes a specific round and driver, and finds their fastest lap in the respective race

A driver's fastest lap in a season
* Takes a specified driver and season and returns the fastest lap across all races
  
Fastest lap overall in a season
* Looks through all the races and drivers across a particular season and returns the fastest lap driven

Fastest qualifying time in a round (respectively, season)
* Takes either a season or a season and round (this is technically 2 different queries) and returns the fastest qualifying time and the driver that ran it

Years a driver (respectively, constructor) was active
* Takes either a driver or constructor (this is technically 2 different queries) and finds every year that they particpated in F1

Coordinates of a circuit
* Takes a circuit and returns its longitude, latitude, and altitude

Number of times a circuit has been used
* Calculates how many races have been run on a specific circuit across its entire lifetime

Most used circuit across
* Counts every time a specific circuit is used across all the seasons and returns the one that is most frequent

Nationality of a driver (respectively, constructor)
* Takes either a driver or constructor (this is technically 2 different queries) and returns their nationality

All drivers (respectively, constructors) that are from a specific country
* Takes a country and returns all drivers or constructors (this is technically 2 different queries) that are from that coutnry

Full ranking of drivers for a season
* Ranks all drivers in a given season by points earned

Drivers that did not reach the end of a race
* Takes a specific round/race and returns all the drivers that had to stop halfway through (did not have a finish time)

Average lap time for a season (driver-specific or over all drivers)

* Finds the mean (or median) lap time across all races in a given season, either
for one driver or for all drivers combined.

Average grid position for a driver per season

* Calculates the typical (median or mode) starting position of a specific driver
across all races in a season

Percentage of races a driver did not finish (DNF rate)

* Calculates what percentage of a driver's races (in a season or overall) ended
without finishing. This may be better off as multiple queries.

Which round had the most DNFs

* Finds the race or round (in a season or overall) with the highest number of
drivers that did not finish.

How many times a specific driver won

* Counts the total number of race victories (first placement) achieved by a
specific driver (in a seaon or overall)

Which driver participated in F1 the longest

* Finds the driver with the longest career duration based on the earliest and
latest recorded race dates.

Oldest and youngest driver in a specific season

* Finds the oldest and youngest drivers who competed in a particular season, and
we can rank the candidates by age.

Driver who contributed the most to a constructor in a season (by points or participation)

* Finds which driver scored the most points or participated in the most races
for a constructor during a season, and this may be two separate queries.

Among all champions, which winner had the most points

* Compares all championship-winning drivers and finds the one who earned the
highest total points in their winning season

Which season a specific country performed the best

* Finds the season where drivers or constructors from a given country achieved
the best results, based on points or wins. This can be broken up into multiple
queries depending on if we care about drivers or constructors, and points or wins.
