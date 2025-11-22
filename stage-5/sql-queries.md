### Every driver ranked for a specific season

Given a season, return the rankings of all drivers that had completed at least one race in that season

select d.driverID, d.firstName, d.lastName, sum(dr.driverPoints) as totalPoints
from driverRace dr natural join race r natural join driver d
where r.year = ?
group by d.driverID, d.firstName, d.lastName
order by totalPoints desc

The user will be inputting a year within the range 1950 to 2024 inclusive

This is a very typical query that many users of the interface would like to know. It directly compares performance of drivers in the competitive sport which is a strong starting point for discovering correlations between skilled drivers and other factors.

### How many times each circuit was used in a given year

In a given season, for all circuits, count the number of races that have taken place on the circuit and order them by year and count.

with seasonCounts as (
    select s.year, c.name, count(r.raceID) as raceCount
    from season s natural join race r natural join circuit c
    where s.year = ?
    group by s.year, c.name
)
select * from seasonCounts order by year, raceCount desc;

This gives insight into which circuits are popular among the seasons, which may be due to viewership or availability. This query can help with decisions pertaining to the introduction of new circuits.

### How many drivers did not finish some race

Given some race, count the number of drivers that had participated in that race that had also received a DNF during the race

select count(dr.driverID) as dnfCount
from race r natural join driverRace dr
where r.raceID = ? and dr.finalPosition = DNF
group by r.raceID;

The user will input the race ID of the race they are interested in. Ideally, users dont interact with the IDs of the records since those are only intuitive for searching, but there is not other attribute we can rely on

Analysts can observe correlations between high DNF rates and circuit conditions, driver/constructor rankings, among other things.

### Find some circuit with the highest number of finished races for each season

In all seasons, out of all circuits, find the circuits that hosted the highest number of races (may be multiple candidates) in their respective seasons.

with circuitCounts as (
    select r.year, c.circuitID, c.name, c.country, c.location, count(r.raceID) as raceCount
    from race r natural join circuit c
    group by r.year, c.circuitID, c.name, c.country, c.location
)
select cc.* from circuitCounts cc
natural join (
    select year, max(raceCount) as maxCount
    from circuitCounts
    group by year
) maxCounts
order by cc.year;

Analysts can see what the most popular circuit is for each year of activity, which can be used to analyze popularity trends of different circuits and its correlation to world events in the corresponding countries.

### What are the circuits found in a specific hemisphere of the globe

Given a choice of hemishphere, return the circuits that are located within that hemisphere.

select c.circuitID, c.name, c.location within country, c.country
where c.latitude between ? and ? and c.longitude between ? and ?

The user will choose which hemisphere, and the logic layer will input the corresponding parameters.

Analysts can find trends in geolocation of the circuit and its relevant hemisphere correlating to race conditions due to weather, climate, or geological restrictions (altitude, terrain, etc).

### Average grid position for a driver per season

Given a driver, find their average grid position in each season. Could be interesting to get the some metric of the averages across all drivers in all seasons to view a limited ranking.
This is median but wed just change the aggregation to MODE (this could be an option for the user)

select r.year, avg(dr.gridPosition) as avgPos
from driverRace dr natural join driver d natural join race r
where d.driverID = ?
group by r.year
order by r.year

The user will input the ID of the driver they are interested in. May need to put some sort of lookup for the user, perhaps by name or constructor.

Analysts can see trends in a drivers grid positions, relating to their performance over time in qualifyings.

### Who was the oldest/youngest driver in a specific season (order these by age)

Given a season, find the oldest and youngest drivers.
Maybe we could add a thing so it outputs just one of them depending on user input but i dont think thats necessary
We could consider getting oldest/youngest across ALL seasons

select d.driverID, d.firstName, d.lastName, d.dateOfBirth
from driver d natural join partOf p
where p.year = ?
order by d.dateOfBirth
limit 1

union

select d.driverID, d.firstName, d.lastName, d.dateOfBirth
from driver d natural join partOf p
where p.year = ?
order by d.dateOfBirth desc
limit 1

The user will input the year of the season they are interested in.

Analysts can observe and predict trends in the ratio between older and younger drivers in F1. This can help with understanding demographic and marketing potential.

### Which driver contributed the most to a specific constructor in a season based on points earned

Given a season, find the driver that contributed the most to a given constructor. The metric is points earned across all races.

select d.driverID, d.firstName, d.lastName, sum(dr.driverPoints) as totalPoints
from driverRace dr natural join race r natural join driver d
join partOf p on p.driverID = dr.driverID and p.year = r.year
where r.year = ? AND p.constructorID = (select constructorID from constructor where name = ?)
group by d.driverID, d.firstName, d.lastName
order by totalPoints dsc
limit 1;

The user will input the year of the season and the ID of the constructor. Names are unique in the dataset.

This helps analysts see which drivers are the best at contributing to specific constructors, and can help make business decisions when signing on new drivers.

### Which driver contributed the most to a specific constructor in a season based on races completed

Similar to above, but the metric is now the amount of races the driver had completed.

select d.driverID, d.firstName, d.lastName, count(dr.raceID) as raceCount
from driverRace dr natural join race r natural join driver d
join partOf p on p.driverID = dr.driverID and p.year = r.year
where r.year = ? AND p.constructorID = (select constructorID from constructor where name = ?)
group by d.driverID, d.firstName, d.lastName, dr.raceID
order by raceCount dsc
limit 1;

Same as previous.

### Which constructor spent the least amount of time driving in a given season

Out of all constructors that had participated in a given season, find the constructor that was the quickest. In other words, the constructor in which drivers apart of said constructor completed their laps in the fastest total time.

select s.year, c.name, sum(l.lapTime) as totalTime
from season s natural join race r natural join lap l natural join driver d
join on partOf p on p.driverID = d.driverID and p.year = s.year
natural join constructor c
where s.year = ?
group by s.year, c.name
order by totalTime
limit 1;

The user will input the year.

Analysts can see which constructors tend to win championships, and can find connections between performance and net worth of the companies.

### Which nationality drove the fastest average lap on circuits native to their country

Considering all nationalities any driver can be, find the nationality that, when racing on circuits native to their country, tend to complete the races in the fastest time.

with nativeLaps as (
    select d.nationality, l.lapTime
    from lap l natural join driver d natural join race r natural join circuit c
    join locale loc on d.nationality = loc.nationality and c.country = loc.country
)
select nationality, avg(lapTime) as avgTime
from nativeLaps
group by nationality
order by avgTime
limit 1;

Analysts can understand how drivers from different nationalities vary in experience driving on native circuits, and can help provide insights in to better training practices or competitition preparation, as well as circuit selection for future seasons.

## What position was a driver in during a specific lap of a specific race

This query will find a driver's position in a specific lap within a certain race.

SELECT position FROM laptimes WHERE driverID = ? AND raceID = ? AND lapnumber = ?

The user will be inputting the driver ID, race ID, and lap number of the driver, race, and lap number.

This gives room for analysis of a driver's performance overtime, which can help towards the development of strategies for future races.


## Who won in a season

SELECT d.driverID, d.firstName, d.lastName SUM(r.points) AS totalPts FROM results r NATURAL JOIN race ra NATURAL JOIN drivers d WHERE ra.year = ? GROUP BY d.driverID ORDER BY totalPts DESC LIMIT 1;

## What are the coordinates of a circuit

SELECT name, lat, lng FROM circuits WHERE circuitID = ?;

## Who won this race

SELECT d.driverID, d.firstName, d.lastName FROM results r NATURAL JOIN drivers d WHERE r.raceId = ? AND r.positionOrder = 1;

## How many points did a driver/constructor get for a specific round
> Driver

SELECT d.driverID, d.firstName, d.lastName, r.points FROM results r NATURAL JOIN drivers WHERE r.raceID = ?  AND r.driverID = ?;

> Constructor

SELECT c.constructorID, c.name, SUM(r.points) AS totalPts FROM results r NATURAL JOIN constructors c WHERE r.raceID = ? AND c.constructorID = ? GROUP BY c.constructorId, c.name;

## How many championships did a constructor/driver participate in
> Driver

SELECT d.driverID, d.firstName, d.lastName, COUNT(DISTINCT ra.year) AS seasonsParticipated
FROM results r NATURAL JOIN drivers NATURAL JOIN races GROUP BY d.driverID, d.firstName, d.lastName;

> Constructor

SELECT c.constructorID, c.name, COUNT(DISTINCT ra.year) AS seasonsParticipated
FROM results r NATURAL JOIN constructors NATURAL JOIN races GROUP BY c.constructorID, c.name;

## Who had the fastest qualifying time

> Round

SELECT d.driverID, d.firstName, d.lastName, q.q1 AS qualifyingTime
FROM qualifying q NATURAL JOIN drivers WHERE q.q1 IS NOT NULL UNION

SELECT d.driverID, d.firstName, d.lastName, q.q2 AS qualifyingTime FROM qualifying q JOIN drivers d ON q.driverId = d.driverID WHERE q.q2 IS NOT NULL UNION

SELECT d.driverID, d.firstName, d.lastName, q.q3 AS qualifyingTime FROM qualifying q JOIN drivers d ON q.driverId = d.driverID WHERE q.q3 IS NOT NULL

ORDER BY qualifyingTime ASC LIMIT 1;

> Season

SELECT d.driverId, d.firstName, d.lastName, q.qualifyingTime, r.year
FROM (
    SELECT q1 AS qualifyingTime, driverID, raceId FROM qualifying WHERE q1 IS NOT NULL
    UNION
    SELECT q2 AS qualifyingTime, driverID, raceId FROM qualifying WHERE q2 IS NOT NULL
    UNION
    SELECT q3 AS qualifyingTime, driverID, raceId FROM qualifying WHERE q3 IS NOT NULL
) AS q NATURAL JOIN drivers NATURAL JOIN races WHERE r.year = ? ORDER BY q.qualifyingTime ASC LIMIT 1;
