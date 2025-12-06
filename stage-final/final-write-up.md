# Final Report

## Table of Contents

1. [Introduction](#introduction)
2. [Data](#data)
3. [Data Model](#data-model)
4. [Database](#database)
5. [Interface](#interface)
6. [Queries](#queries)
7. [Conclusion](#conclusion)
8. [Appendix](#appendix)


## Introduction

Our group chose to model all data from all the past seasons of Formula 1. There's a lot of potential for analysis, and a well-designed data model will best allow for that. We included as much data as possible to allow for full analysis (or for full access to data for a layperson). Our UI is a lightweight text-based interface. It categorizes all available queries to make the data easily accessible, and takes into account those who are less experienced with CLIs by including a help menu. This report details the process of creating this project, and further explains its features.

## Data

Our data was retrieved from the dataset Formula 1 World Championship from Vopani “Rohanrao”, sourced from Kaggle. It contains data on the Formula 1 (F1) World Championship from 1950 to 2024. 

F1 is a world-renowned auto-racing forum, and is widely considered a premier league for circuit racing. The world championships considered in this dataset are entire seasons appearing in any given year. One season comprises a series of 12 races that takes place on distinct circuits across the world, alongside qualifying rounds that precede them. Constructors are the designers and builders of the cars used in races, and each racer belongs to a constructor and races for them throughout each season.

The data consists of tables pertaining to the following:

* races
* drivers
* constructors
* qualifying sessions
* circuits
* lap times
* the championships overall

These had attributes like identifying information (name, code, nationality, etc.), and times and positions(for qualifying sessions and lap times). Some additional supporting tables are driver standings, constructor standings and (race) results, as well as sprint results. Sprints or sprint races are shorter than circuits and do not require any driver to stop for pit stops. 

When deciding on a dataset, we chose this one because we felt it would divide well into entities. Each aspect of F1 listed above is closely related, but still has it's own extensive set of data and attributes. Additionally, it had lots of records since it included all of F1's history. 

Across all files and entity sets, there is a total number of 701,433 records. Lap times account for approximately 500,000 records. Excluding the quantity of records contributed by lap times, the remaining records come from constructor results and standings, driver standings, pit stops, qualifying races, and the results, with each having greater than 10,000 records respectively. The remaining entity sets—circuits, constructors, drivers, races, seasons, sprint results, and status—each have between 70 to 1,000 records.

Our dataset required quite a bit of cleaning, largely in the form of cutting attributes we felt weren't necessary. Many were cut due to having excessive null values, though some (like information on pit stops) were cut to keep the scope of our project more reasonable. 

We also added on table on our own, made from scratch. Locale was created by hand to map nationalities to their countries.

This is the final version of the ER diagram that we created based off of the dataset.
- insert ER diagram here

## Data Model

Most of our entities are organized the same way our dataset is. As mentioned above, we picked a dataset that we felt would easily divide into entities, so this is expected. Most of the design decisions were made regarding the relationships between them. 

A few of them are fairly straightforward (a Constructor has many Drivers within it, so of course there would be a relationship there). The relationships "Part of", "Happens on", and "Makes up" fall into this category. However, the rest are a bit more complicated.

We initially had Lap solely related to Race, as it felt the most straightforward. That created the problem of needing an additional relationship with Driver since Lap needs foreign keys from both, so we decided instead to make "Recorded in" a ternary relationship. We debated back and forth about having an entity for the championship(s), and also whether we should separate the driver and constructor championships. In the end, we elected to make it 2 relationships because every Season needs to have both championships, but the championships on their own didn't have any attributes. The most complicated relationship is "Races in". This holds all the information from the qualifying format that determines precedes the race, the constructor each driver is racing for, the results of the race (final position), and the points that both the driver and their respective constructor accumulates. We tried very hard to break this into some more constituent parts, but so many of these parts are integral to each race, so consolidating it into one relationship between Driver and Race seemed to make the most sense. Locale was connected later to convert the country the Circuits were located in to nationalities. 

Despite picking our dataset with future steps in mind, it clearly did not fit into a relational model as cleanly as we had hoped. We had to make some changes to the model when we began implementing our queries; in particular, adding Locale as an entity. We also went back and forth on keeping qualifyingTimes in the "Races in" relationship, or just cutting the qualifying session data entirely. We also removed milliseconds from the Lap data, which later made things more complicated during implementation. 

The last, and more overarching, regret was choosing something all of us were so unfamiliar with. We initially thought it would be a fun learning opportunity because we were all interested in it, but it made communication a bit more difficult than we expected as we were all at different levels of understanding during certain parts of the project. We realized this issue early on, but chose to put more effort into our communication rather than restart our project. Despite the difficulties, we feel it was the right decision.


## Database

We made a relational database. It is hosted on Uranium, and all our queries are written in Microsoft SQL. They were originally written for SQLite, but had to be re-written a little bit for Uranium. 

As mentioned earlier, the bulk of the data comes from laps. This made populating the database somewhat difficult because it took up quite a bit of memory to do so. Transactions were also made impossible because they simply would not run. In the end, we had to populate the data on laps a little bit at a time (about 50,000 entries at a time) to make it work.

## Interface
Our project has a text interface in the form of a REPL, programmed in Java. It is run directly in the terminal. The user interacts wth the program through text inputs, guided by prompts from the interface. There are many categories of queries/information, with numbered queries in each category. The categories are drivers, constructors, circuits, races, and general statistics (for more overarching queries).

From the home page, the user chooses which category they would like to get information from. For drivers, constructors, circuits, and races (more broadly, for entities), the user needs to get the ID first from the "ID Reference" section of the home page, and then pick the respective category. For the queries that would otherwise return large volumes of data, we limit the data returned at once and allow the user to page through it.

There is also a menu option to repopulate the data if the user requires it. As the warning in the menu options states, this should not be done randomly as it takes a good deal of time to work through all the data.

There is a help menu available for guidance if need be. It describes how to use the application, much like in this report. 

Below are screenshots of some pieces of the UI in action. 
- insert screenshots here

## Queries

Below is a list of the most interesting queries we implemented, alongside an explanation of why they are valuable from an analyst's perspective.

### Which driver contributed the most to a specific constructor in a season based on points earned

Given a season, find the driver that contributed the most to a given constructor. The metric is points earned across all races. The user will input the year of the season and the ID of the constructor. Names are unique in the dataset.

This helps analysts see which drivers are the best at contributing to specific constructors, and can help make business decisions when signing on new drivers.

### Which driver contributed the most to a specific constructor in a season based on races completed

Similar to above, but the metric is now the amount of races the driver had completed.

Same as previous.

### Which nationality drove the fastest average lap on circuits native to their country

Considering all nationalities any driver can be, find the nationality that, when racing on circuits native to their country, tend to complete the races in the fastest time.

Analysts can understand how drivers from different nationalities vary in experience driving on native circuits, and can help provide insights in to better training practices or competitition preparation, as well as circuit selection for future seasons.

### Average lap time over the entirety of a given season
Given a specific season, takes the performance of all drivers in every race during that season into account and returns the average lap time. 

Analysts can use this to compare the overall performance of the drivers during a certain season. During a broader analysis, it could be used to help gauge the difficulty of certain tracks and see if different combinations lead to a harder season overall.

### Which race in a give season had the most drivers who did not finish (DNF)
Given a specific season, counts all the DNFs for each race and returns the one with the highest count. 

Analysts could gauge the difficulty of a specific circuit, or judge the performance of drivers on the same circuit from year to year. 


## Conclusion

The complexity and close knit relationships make a relational database a good choice for this dataset. There may be some advantages of other database systems, but those would mostly be in the realm of the simpler queries. For example, a graph database might have an easier time determining things like the driver-constructor relations, or most commonly used circuit. However, some of our more complex queries require the benefits of a relational database to run. A good example is determining which nationality of drivers drove the fastest average lap on circuits native to their country, due to the aggregation of laps. Such a query requires a relational database, and could not be re-created easily with other database systems. 

There are a lot of things that could be done with this database as a teaching tool. It may be a good series of activities, as we believe there are still many things to be done. There are some drawbacks to the way we organized our data, and there are many, many more queries that could be created. Students could also explore how to integrate some of the data that we cut from the original dataset. We imagined a series of in-class activities, where students are given the data and a specific optimization or modification to implement. However, we also all believe that this would not be a good dataset to attempt that with, as F1 is a relatively difficult subject to understand. As mentioned earlier, our own team had communication issues since we were all unfamiliar with the event. Despite spending lots of time researching and discussing it, none of us found it very intuitive to understand, and thus, we don't believe a classroom full of students would either. 

This dataset and the database we created for it are robust and have a lot of potential for analysis. We organized the relational model in such a way that optimizes for that analysis. Our queries cover the breadth of the information we gathered from the dataset, and also contain lots of analytical queries that would help in potential correlations and trends among successful drivers and teams. Though we don't believe it would be a great teaching tool for this course, it is a useful and intriguing application for anyone with even a interest in Formula 1.

## Appendix

### Stage 1
- All 3 members helped look for and decide on a dataset
- Ayesha wrote the project timeline
- Jatinder wrote the summary of dataset, created a Git repository for the project, and submitted all finished work

### Stage 2
- All 3 members discussed and decided on how to separate the dataset into entities and relationships
- KC wrote the summary paragraph for the dataset
- Ayesha drew the ER diagram, and submitted all finished work

### Stage 3
- All 3 members dicussed and decided how to modify the ER diagram based on feedback from earlier stages, and made a bullet-point draft of the group reflection together
- Ayesha modified the Stage 1 summary of the dataset as needed and updated the timeline
- Jatinder converted the ER diagram to the relational model, including merging and normalizing
- KC wrote the group reflection based on the group's bullet-point notes, and submitted all finished work

### Stage 4
- All 3 members brainstormed and decided on queries together, and discussed how to implement feedback from earlier stages
- Jatinder submitted the finalized list of queries

### Stage 5
- All 3 members discussed how to implement Stage 4 feedback, and implemented approximately 1/3 of the group's queries along with writing descriptions for them
- Ayesha submitted final copies of everything (though due to some issues with Git, one member's work did not get submitted)

### Stage 6
- All 3 members discussed and decided on UI design, and made a bullet-point draft of the group reflection together
- Ayesha wrote out the group reflection and the UI description, and created the diagrams for the UI
- Jatinder updated the group timeline 
- KC cleaned all the data in preparation for the final stage, and submitted all finished work

### Final Stage
- All 3 members continually discussed and decided on minor changes and implementation details, made a bullet-point draft of the group reflection together, and rehearsed for the final demonstration together
- Ayesha wrote the final write-up and final reflection based on the group's bullet-point notes
- KC implemented the frontend/UI in Java, including creating scaffolding for the backend to be connected
- Jatinder set up the database on Uranium, implemented all queries to work on Uranium and connect to the frontend, connected the front- and backend, and submitted all finished work

