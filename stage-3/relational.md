these are in order of execution

merging --

the many side of happens-on and makes-up are both races, and since race entity concerns a specific instance of a race occuring, we can collapse the two relations by giving them foreign keys year and circuit ID to determine which season and circuit it pertains to

before:
race(race ID, round, date, grand prix or sprint)
happens on (circ ID , race ID) 1-M
makes up (race ID, year) 1-M

after:
race( (PK) race ID, round, date, grand prix or sprint, (FK) circ ID -> circuit, (FK) year -> season)




normalizing --

constructor is already in 3NF

constructor( (PK) constructorID, ref, name, nationality)




season is trivially in 3NF

season( (PK) year )



race is in 3NF

race( (PK) race ID, round, date, grand prix or sprint, (FK) circ ID -> circuit, (FK) year -> season)




circuit has MVA, otherwise i dont think it violates transitive dep since we look up all attributes on strictly circ ID, and while we could technically use lat/long/alt to get location, we do not employ that since our data does not do that

before:
circuit(circID, name, location (location within country, country), coordinates (lat, lng, alt))

after:
circuit( (PK) circID, name, location within country, country, lat, lng, alt)




driver: we just need to break up first and last name

before:
driver(driverID, driverRef, name (first and last), dob, nationality)

after:
driver( (PK) driverID, driverRef, first name, last name, dob, nationality), where there is no partial key dependencies or transitive dependencies





lap and recorded in: it depends on race ID and driver ID to uniquely identify a lap, so we can collapse these

before:
lap (lap number, lap time, position)
recorded in (race ID, driver ID, lap number) 1-1-M

after:
lap( (PK) lap number, lap time, position. (FK) (PK) race ID -> race, (FK) (PK) driver ID -> driver)





partOf(driver ID, constructor ID) M-M, drivers can be in many constructors, but we will have data anomalies since driver ID 1 can map to two different constructors, so we add "year" from season. we create a new table, and since all attributes depend on the composite key in whole and there is no transitive dep, this is 3NF

before:
partOf(driver ID, constructor ID) M-M,

after:
partOf( (PK) (FK) driverID -> driver, (PK) (FK) constructor ID -> constructor, (PK) (FK) year -> season)





races in: has to be a table, but there is partial key dep with constructor points, so it needs to be split

before:
races in (constructor ID, times, grid position, final position, rank, driver points, constructor points ) M-M

after:
constructorRace ( (FK) (PK) raceID -> race, (FK) (PK) constructor ID -> constructor, constructor points)
driverRace ((FK) (PK) raceID -> race, (FK) (PK) driver ID -> driver, driver points, grid position, final position, rank)



championships: both need to become tables, basically already 3NF

before:
participates in driver championship (driver ID, year, winner) M-M
participates in constructor champtionship (constructor ID, year, winners MVA) M-M

after:
driverChampionship ( (PK) (FK) driver ID -> driver, (PK) (FK) year -> season, bit toggle winner)
constructorChampionship ( (PK) (FK) constructor ID -> constructor, (PK) (FK) year -> season, bit toggle winner)



how do we do grid position, final position, rank, for the constructor Race
