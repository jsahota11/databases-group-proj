# Changes and Modifications
See below the different changes made to each table

## Circuits, Constructors, Seasons
- Removed `url` column

## Drivers
- Removed columns `url`, `code`, and `number` 

## Lap Times
- Data was clean, nothing changed. If I overlooked, please let me know!

## Races
- Removed columns `fp1_time` and `fp1_date` up to fp3
- Removed columns `quali_date` and `quali_time`
- Removed columns `sprint_time` and `sprint_date`
- Removed `url` column
- Removed `time` column

## Results
Numerical columns with null values:
- `position`
- `time`
- `milliseconds`
- `fastestLap`
- `rank`
- `fastestLapTime`
- `fastestLapSpeed`

were all changed to **-1**.

Additionally, the column `positionText` was dropped.

## Sprint Results
Numerical columns with null values:
- `position`
- `time`
- `milliseconds`
- `fastestLap`
- `fastestLapTime`

were all changed to **-1**.

Additionally, removed column `positionText`
