# Queries with explanations

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
