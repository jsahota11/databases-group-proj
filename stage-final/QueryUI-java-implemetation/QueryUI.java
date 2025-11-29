import java.util.Scanner;

public class QueryUI {

/* COMP 3380 (A01) : Adam Pazdor
 * Members: Kriza del Moro, Jatinder Sahota, Ayesha Qadir
 * Group # 32
 * Purpose: Interactive UI for a user to run queries for our DB
 */

// For developer comments: Please see README.md for more information!

    public static void main(String[] args) {
        runMenu();
    }

    // Method to run the main menu
    public static void runMenu() {
        // Scanner declare
        Scanner sc = new Scanner(System.in);
        boolean exit = false; // assume default run state (not exiting)
        while (!exit) { // while user has not exited
            printMenu(); // show main menu
            System.out.println("[SYSTEM] Key in your selection below.\n"); // for user input
            System.out.print("> "); // for user input
            String input = sc.nextLine().strip();
            if (validMenuInput(input)) { // user picked between 1-7
                switch (Integer.parseInt(input)) {
                    case 1:
                        queryDrivers();
                        break;
                    case 2:
                        queryConstructors();
                        break;
                    case 3:
                        queryCircuits();
                        break;
                    case 4:
                        queryRaces();
                        break;
                    case 5:
                        queryGenStats();
                        break;
                    case 6:
                        runHelpMenu();
                        break;
                    case 7:
                        System.out.println("[SYSTEM] Quitting application. . .");
                        exit = true;
                        break;
                    default:
                        break;
                }
            }
            else {
                System.out.println("[ERROR] Invalid input! Try again.");
            }
        }
        sc.close(); // close resources
    }

    // private static method(s)

    /* ----- PRINT MENU METHODS -----
     * All methods will print the text "interface"
     * and will also prompt for user input. Error-checking will be done as appropriate to
     * ensure a smooth interaction.
     * 
     * Helper methods have been drafted to help ensure that all users are
     * safely handled and passed
     */ 

    // Driver menu 
    private static void queryDrivers() {
        // TODO: print driver menu
        Scanner sc = new Scanner(System.in);
        // Print query list + anticipate input on desired query
        // -> will also process user input and conduct queries in reference
        int input = runDriverQueryPage();
        if (input <= 16 || input >= 1 || input == -1) {
            switch (input) { // diff query cases 
                case 1: // Driver ranking query... and so on
                    System.out.println("[SYSTEM] To query for 'Driver rankings in a season', key in: year\n");
                    System.out.print("> ");
                    String dbCommand = sc.nextLine().strip(); // take input for command
                    if (validateDrQueryInput(dbCommand, input)) { // ensure command is valid based on picked query
                        String[] tokens = dbCommand.split(" ");
                        driverRankingPerSeason(Integer.parseInt(tokens[0])); // do query!
                    }
                    else { // invalid command args -> home 
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 2:
                    System.out.println("[SYSTEM] To query for 'Driver who has been active the longest in F1', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        longestParticipatingDriver(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 3:
                    System.out.println("[SYSTEM] To query for 'Oldest/Youngest driver in a season', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverAgeInSeason(tokens[0], Integer.parseInt(tokens[1]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 4:
                    System.out.println("[SYSTEM] To query for 'Driver championship winner in a season', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverWinnerInSeason(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 5:
                    System.out.println("[SYSTEM] To query for 'Driver with fastest qualifying time in a round', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        fastestQualifyingTimeInRound(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 6:
                    System.out.println("[SYSTEM] To query for 'Driver with fastest qualifying time in a season', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        fastestQualifyingInSeason(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 7:
                    System.out.println("[SYSTEM] To query for 'Driver's average grid position for all seasons', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        avgDriverGridPos(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 8:
                    System.out.println("[SYSTEM] To query for 'Driver's specific position in specific lap # for a certain race', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverPosInLapOfRace(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 9:
                    System.out.println("[SYSTEM] To query for 'Driver's total points in a specific round', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverPtsInRound(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 10:
                    System.out.println("[SYSTEM] To query for 'Driver's number of participated championships', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverNumChampionships(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 11:
                    System.out.println("[SYSTEM] To query for 'Driver's constructor history', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverConstructorHistory(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 12:
                    System.out.println("[SYSTEM] To query for 'Driver's fastest lap in a specific season', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverFastestLapInSeason(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 13:
                    System.out.println("[SYSTEM] To query for 'Driver's active years in F1', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverActiveYrs(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 14:
                    System.out.println("[SYSTEM] To query for 'Driver's nationality', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverNationality(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 15:
                    System.out.println("[SYSTEM] To query for 'Driver's average lap time in a specific season', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverAvgLapTimeInSeason(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 16:
                    System.out.println("[SYSTEM] To query for 'Driver's total number of driver championship wins', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateDrQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        driverNumWinsDChamp(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                default:
                    break;
            }
        }
        else {
            System.out.println("[ERROR] Invalid option, try again!");
        } 
    }

    // Constructor menu
    private static void queryConstructors() {
        // TODO: print constructor menu
        Scanner sc = new Scanner(System.in);
        // Print query list + anticipate input on desired query
        // -> will also process user input and conduct queries in reference
        int input = runConstructorQueryPage();
        if (input <= 7 || input >= 1 || input == -1) {
            switch (input) { // diff query cases 
                case 1:
                    System.out.println("[SYSTEM] To query for 'Least active constructor in a season', key in: year\n");
                    System.out.print("> ");
                    String dbCommand = sc.nextLine().strip();
                    if (validateConstQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        leastActiveConstructorInSeason(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 2:
                    System.out.println("[SYSTEM] To query for 'Driver with the most point contribution in a specific constructor', key in: constructorID, year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateConstQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        highestDriverPtsInSeason(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 3:
                    System.out.println("[SYSTEM] To query for 'Driver with the most races in a specific constructor in a specific season', key in: constructorID, year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateConstQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        highestDriverRacesInSeason(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 4:
                    System.out.println("[SYSTEM] To query for 'Constructor's total points in a certain round', key in: constructorID, raceID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateConstQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        constructorPtsInRound(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 5:
                    System.out.println("[SYSTEM] To query for 'Constructor's total of participated championships', key in: constructor ID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateConstQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        constructorNumChampionships(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 6:
                    System.out.println("[SYSTEM] To query for ' Constructor's years of activity', key in: constructorID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateConstQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        constructorActiveYrs(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 7:
                    System.out.println("[SYSTEM] To query for 'Constructor's nationality', key in: constructorID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateConstQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        constructorNationality(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                default:
                    break;
            }
        }
        else {
            System.out.println("[ERROR] Invalid option, try again!");
        }
    }

    // Circuit menu
    private static void queryCircuits() {
        // TODO: print circuit menu
        Scanner sc = new Scanner(System.in);
        // Print query list + anticipate input on desired query
        // -> will also process user input and conduct queries in reference
        int input = runCircuitQueryPage();
        if (input <= 5 || input >= 1 || input == -1) {
            switch (input) { // diff query cases 
                case 1: 
                    System.out.println("[SYSTEM] Querying for. . . 'Most raced on circuits in F1'\n");
                    cirHighestRacePerSeason();
                    break;
                case 2:
                    System.out.println("[SYSTEM] To query for 'Total races held in each circuit for a specific season', key in: year\n");
                    System.out.print("> ");
                    String dbCommand = sc.nextLine().strip();
                    if (validateCircQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        circuitNumRacePerSeason(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 3:
                    System.out.println("[SYSTEM] Querying for. . . 'Most raced on circuit in all of F1'\n");
                    mostUsedCircuit();
                    break;
                case 4:
                    System.out.println("[SYSTEM] To query for 'Circuits located in specific hemispheres of the globe', key in: hemisphere <EXPECTED: 'north', 'south', 'east', 'west'>\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateCircQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        circuitsInHemisphere(tokens[0]);;
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 5:
                    System.out.println("[SYSTEM] To query for 'Coordinates of a specific circuit', key in: circuitID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateCircQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        circuitCoords(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                default:
                    break;
            }
        }
        else {
            System.out.println("[ERROR] Invalid option, try again!");
        }
    }

    // Races menu
    private static void queryRaces() {
        // TODO: print races menu
        Scanner sc = new Scanner(System.in);
        // Print query list + anticipate input on desired query
        // -> will also process user input and conduct queries in reference
        int input = runRaceQueryPage();
        if (input <= 4 || input >= 1 || input == -1) {
            switch (input) { // diff query cases 
                case 1: 
                    System.out.println("[SYSTEM] To query for 'Races with the most DNF's (Did Not Finish)', key in: year\n");
                    System.out.print("> ");
                    String dbCommand = sc.nextLine().strip();
                    if (validateRaceQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        raceWithMostDNF(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 2:
                    System.out.println("[SYSTEM] To query for 'DNF count of each race in a specific season', key in: raceID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateRaceQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        numDriverDNFPerRace(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 3:
                    System.out.println("[SYSTEM] To query for 'Winner of a specific race', key in: raceID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateRaceQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        raceWinner(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 4:
                    System.out.println("[SYSTEM] Driver with the fastest recorded lap time in a specific race', key in: raceID\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateRaceQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        fastestLapInRace(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                default:
                    break;
            }
        }
        else {
            System.out.println("[ERROR] Invalid option, try again!");
        }
    }

    // GenStats menu
    private static void queryGenStats() {
        // TODO: print general statistics menu
        Scanner sc = new Scanner(System.in);
        // Print query list + anticipate input on desired query
        // -> will also process user input and conduct queries in reference
        int input = runGenStatsQueryPage();
        if (input <= 3 || input >= 1 || input == -1) {
            switch (input) { // diff query cases 
                case 1: 
                    System.out.println("[SYSTEM] Querying for. . . 'Nationality whose drivers perform the fastest on circuits located in home-country', key in: year\n");
                    fastestNationalityInHomeCircuit();
                    break;
                case 2:
                    System.out.println("[SYSTEM] To query for 'Fastest lap recorded across all races in a specific season', key in: year\n");
                    System.out.print("> ");
                    String dbCommand = sc.nextLine().strip();
                    if (validateGenStatQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        fastestLapInSeason(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                case 3:
                    System.out.println("[SYSTEM] To query for 'Average lap time of all drivers across all races in a given season', key in: year\n");
                    System.out.print("> ");
                    dbCommand = sc.nextLine().strip();
                    if (validateGenStatQueryInput(dbCommand, input)) {
                        String[] tokens = dbCommand.split(" ");
                        seasonAvgLapTime(Integer.parseInt(tokens[0]));
                    }
                    else {
                        System.out.println("[ERROR] Invalid command! Going back home...");
                    }
                    break;
                default:
                    break;
            }
        }
        else {
            System.out.println("[ERROR] Invalid option, try again!");
        }
    }

    /**
     * Run help menu + page and prompt user input as needed
     * @TODO modularize better if one page is not enough or new information needs
     * to be added
     * 
     * @TODO will clean-up while loop a bit more later to inform user or invalid arguments
     */
    private static void runHelpMenu() {
        // TODO: print help menu
        Scanner sc = new Scanner(System.in); // in case we have multiple pages!
        boolean exit = false; // while user has not exited -- assume default state
        printHelpMenu();
        // while user has not chosen to exit
        while(!exit) { // clean this up more later 
            System.out.print("> ");
            String input = sc.nextLine().strip(); // keep asking for input until user chooses to exit
                                                  // gives an effect of keeping the help page still
            if (input.length() != 0) { // if invalid input length (non-empty) -> valid
                String[] tokens = input.split(" ");
                if (tokens.length == 1) { // valid arg nums (1); expecting one argument 'h'
                    if (!isValidNumInput(tokens[0])) { // non-numeric input -> valid
                        if (tokens[0].toLowerCase().equals("h")) { // if it's 'h' -> exit
                            exit = true;
                        }
                    }
                }
            } 
        }
    }

    // Default menu message
    private static void printMenu() {
        System.out.println("--- Formula 1 'F1' DATA (1950 - 2024) ---\n");
        System.out.println("Welcome! Selected a numbered option below (1-7) to get started.\n" +
                           "1. Information on Drivers\n" +
                           "2. Information on Constructors\n" +
                           "3. Information on Circuits\n" +
                           "4. Information on Races\n" +
                           "5. General Statistics\n" +
                           "6. Help Menu\n" +
                           "7. Quit application\n\n" +
                           "[SYSTEM] Note for look-up commands, ensure they are exactly one space apart (for multiple arguments) when keying in or it will be considered invalid!");  
    }

    /**
     * ----- QUERY METHODS -----
     * @TODO fill in query methods
     * PLEASE see README.md before proceeding!
     * 35 queries total - based on stage-5/sql-queries.md commit on Nov. 23
     */

    // DRIVER-RELATED QUERIES

    /* --- ALL DRIVER (GENERIC STATS) --- */

    // 1) Given a season, return the rankings of all drivers that had completed at least one race in that season
    private static void driverRankingPerSeason(int seasonYear) {
        // TODO: implement SQL query
        System.out.println("TEST Querying for year " + seasonYear);
    }

    // 2) Determines the length of the career of each driver that has ever participated in F1, and returns the one with the longest.
    private static void longestParticipatingDriver(int driverID) {
        // TODO: implement SQL query
    }

    // 3) Given a season, find the oldest and youngest drivers. 
    // Maybe we could add a thing so it outputs just one of them depending on user input but i dont think thats necessary We could consider getting oldest/youngest across ALL seasons
    private static void driverAgeInSeason(String preference, int seasonYear) {
        // TODO: implement SQL query
        // if older : do older query
        // else : do younger query
    }

    // 4) Who won in a season
    private static void driverWinnerInSeason(int seasonYear) { 
        // TODO: implement SQL query
    }

    // 5) Who had the fastest qualifying time in a particular round
    private static void fastestQualifyingTimeInRound(int raceID) {
        // TODO: implement SQL query
    }
    
    // 6) Who had the fastest qualifying time in a particular season
    private static void fastestQualifyingInSeason(int seasonYear) {
        // TODO: implement SQL query
    }

    /* --- DRIVER (INDIVIDUAL) SPECIFIC --- */

    // 7) Given a driver, find their average grid position in each season. 
    // Could be interesting to get the some metric of the averages across all drivers in all seasons to view a limited ranking. 
    // This is median but wed just change the aggregation to MODE (this could be an option for the user)
    private static void avgDriverGridPos(int driverID) {
        // TODO: implement SQL query
    }

    // 8) This query will find a driver's position in a specific lap within a certain race.
    private static void driverPosInLapOfRace(int driverID, int raceID, int lapNum) {
        // TODO: implement SQL query
    }

    // 9) How many points did a driver get for a specific round
    private static void driverPtsInRound(int driverID, int raceID) {
        // TODO: implement SQL query
    }

    // 10) How many championships did a driver participate in
    private static void driverNumChampionships(int driverID) {
        // TODO: implement SQL query
    }

    // 11) Given a specific driver, return all the constructors they have been a part of during their career.
    private static void driverConstructorHistory(int driverID) {
        // TODO: implement SQL query
    }

    // 12) Given a specific season and driver, return their fastest lap was during that season.
    private static void driverFastestLapInSeason(int driverID, int seasonYear) {
        // TODO: implement SQL query
    }

    // 13) Given a driver, returns all the years they participated in F1.
    private static void driverActiveYrs(int driverID) {
        // TODO: implement SQL query
    }

    // 14) Returns the nationality of a given driver. A useful query for the average user to get this information.
    private static void driverNationality(int driverID) {
        // TODO: implement SQL query
        System.out.println("TEST driverNationality for driver " + driverID);
    }

    // 15) Given a specific driver and season, takes their performance in every race during that season and returns the average lap time.
    private static void driverAvgLapTimeInSeason(int driverID, int seasonYear) {
        // TODO: implement SQL query
    }

    // 16) Given a driver, returns how many times they have been the champion of F1.
    private static void driverNumWinsDChamp(int driverID) {
        // TODO: implement SQL query
    }


    // CONSTRUCTOR-RELATED QUERIES

    /* --- ALL CONSTRUCTOR (GENERAL) --- 
     * These don't really expect constructor ID's
     */
    
    // 17) Out of all constructors that had participated in a given season, find the constructor that was the quickest. 
    // In other words, the constructor in which drivers apart of said constructor completed their laps in the fastest total time.
    private static void leastActiveConstructorInSeason(int seasonYear) {
        // TODO
    }

    /* --- CONSTRUCTOR (SINGLE) SPECIFIC --- 
     * These expect constructor ID's as input
     */

    // 18) Given a season, find the driver that contributed the most to a given, specific constructor. The metric is points earned across all races.
    private static void highestDriverPtsInSeason(int constructorID, int seasonYear) {
        // TODO
    }

    // 19) Similar to above, but the metric is now the amount of races the driver had completed.
    private static void highestDriverRacesInSeason(int constructorID, int seasonYear) {
        // TODO
    }

    // 20) How many points did a constructor get for a specific round
    private static void constructorPtsInRound(int constructorID, int raceID) {
        // TODO
    }

    // 21) How many championships did a constructor participate in
    private static void constructorNumChampionships(int constructorID) {
        // TODO
    }

    // 22) Given a constructor, return all the years they participated in F1.
    private static void constructorActiveYrs(int constructorID) {
        // TODO
    }

    // 23) Returns the nationality of a given constructor. Similar usefulness to the last query.
    private static void constructorNationality(int constructorID) {
        // TODO
    }


    // RACE-RELATED QUERIES

    /* --- ALL RACES (GENERAL) --- 
     * These don't really expect race ID's
     */

    // 24) Given a specific season, counts all the DNFs for each race and returns the one with the highest count.
    private static void raceWithMostDNF(int seasonYear) {
        // TODO
    }

    /* --- RACE (SINGLE) SPECIFIC --- 
     * These expect race ID's as input
     */

    // 25) Given some race, count the number of drivers that had participated in that race that had also received a DNF during the race
    private static void numDriverDNFPerRace(int raceID) {
        // TODO
    }

    // 26) Who won this race
    private static void raceWinner(int raceID) {
        // TODO
    }

    // 27) Given a race, return which recorded lap was the fastest (including the driver that drove it and the time it took).
    private static void fastestLapInRace(int raceID) {
        // TODO
    }

    
    // CIRCUIT-RELATED QUERIES

    /* --- ALL CIRCUITS (GENERAL) --- 
     * These don't really expect circuit ID's
     */

    // 28) In all seasons, out of all circuits, find the circuits that hosted the highest number of races (may be multiple candidates) in their respective seasons
    private static void cirHighestRacePerSeason() {
        // TODO
    }

    // 29) In a given season, for all circuits, count the number of races that have taken place on the circuit and order them by year and count.
    private static void circuitNumRacePerSeason(int seasonYear) {
        // TODO
    } 

    // 30) Returns the most used circuit over the course of all of F1.
    private static void mostUsedCircuit() {
        // TODO
    }

    /* --- CIRCUIT (SINGLE) SPECIFIC --- 
     * These expect race ID's as inputs
     */

    // 31) Given a choice of hemishphere, return the circuits that are located within that hemisphere.
    private static void circuitsInHemisphere(String hemisphere) {
        // TODO
        // Please see README.md for this! Can hardcode lat/long values
    }

    // 32) What are the coordinates of a circuit
    private static void circuitCoords(int circuitID) {
        // TODO
    }


    // GENERAL STAT QUERIES
    
    // 33) Considering all nationalities any driver can be, find the nationality that, 
    // when racing on circuits native to their country, tend to complete the races in the fastest time.
    private static void fastestNationalityInHomeCircuit() {
        // TODO
    }

    // 34) Given a specific season, compare all drivers' performance in all races during that season and return the fastest lap.
    private static void fastestLapInSeason(int seasonYear) {
        // TODO
    }

    // 35) Given a specific season, takes the performance of all drivers in every race during that season into account and returns the average lap time.
    private static void seasonAvgLapTime(int seasonYear) {
        // TODO
    }

    /* ----- HELPER METHODS -----
     * Helper methods to clean input and 
     * error-checking for safe handling.
     * 
     * See comments for more description.
     * Can be updated if needed.
     */

    /**
     * Prints the generic header for query lists
     * @param input : passed user input for "specific" entity
     */
    private static void printQueryListHeader(String entity) {
        System.out.println("[SYSTEM] Great, here are the information we have on " + entity.toUpperCase() + ".\n\n");
    }

    /**
     * Validates numerical inputs
     * @param str - user input
     * @return boolean value for validity
     */
    private static boolean isValidNumInput(String str) {
        String cleanStr = str.strip().replaceAll("\\s+", "");
        // check if it's a num
        try {
            int numString = Integer.parseInt(cleanStr); // attempt parse
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Validates user input for main menu.
     * Method created to avoid runMenu from being chunky + reptitive
     * @param mainInput - should only be numerical and between 1 - 7
     * @return returns a bool depending on validity
     */
    private static boolean validMenuInput(String mainInput) {
        boolean result = true;
        String[] tokens = mainInput.split(" ");
        if (tokens.length != 1) return false; // ensure there's only string!
        // if valid
        if (!isValidNumInput(tokens[0])) return false; // if it's not a number
        // if a valid num
        if (Integer.parseInt(tokens[0]) < 1 || Integer.parseInt(tokens[0]) > 7) return false; // out of bounds!
        // valid!
        return result;
    }

    /* Driver Menu helpers */

    /** Handles user inputs + UI flow for driver query list page 
     * @return returns user input value to process query at main page 
     */

    private static int runDriverQueryPage() {
        // User navigates through query page;
        // Expected inputs: (int) 1-16, "p 2", "p 1", "h" -> otherwise error-check, reject input, re-prompt
        Scanner sc = new Scanner(System.in);
        boolean exit = false; // by default, page keeps running
        int result = -1;
        printQueryListHeader("DRIVERS");
        printDrQueryPageOne(); // print page one first one time
        // while user is not exiting (not entering 'h')
        while (!exit) {   
            System.out.print("> ");
            String input = sc.nextLine().toLowerCase(); // get user input
            if (isValidDRQInput(input)) { // if valid input go through!
                switch (input){
                    case "1":
                        result = 1;
                        exit = true;
                        break;
                    case "2":
                        result = 2;
                        exit = true;
                        break;
                    case "3":
                        result = 3;
                        exit = true;
                        break;
                    case "4":
                        result = 4;
                        exit = true;
                        break;
                    case "5":
                        result = 5;
                        exit = true;
                        break;
                    case "6":
                        result = 6;
                        exit = true;
                        break;
                    case "7":
                        result = 7;
                        exit = true;
                        break;
                    case "8":
                        result = 8;
                        exit = true;
                        break;
                    case "9":
                        result = 9;
                        exit = true;
                        break;
                    case "10":
                        result = 10;
                        exit = true;
                        break;
                    case "11":
                        result = 11;
                        exit = true;
                        break;
                    case "12":
                        result = 12;
                        exit = true;
                        break;
                    case "13":
                        result = 13;
                        exit = true;
                        break;
                    case "14":
                        result = 14;
                        exit = true;
                        break;
                    case "15":
                        result = 15;
                        exit = true;
                        break;
                    case "16":
                        result = 16;
                        exit = true;
                        break;
                    case "p 1":
                        printDrQueryPageOne();
                        break;
                    case "p 2":
                        printDrQueryPageTwo();
                        break;
                    case "h":
                        System.out.println("[SYSTEM] Going back to Main Menu...");
                        exit = true;
                        break;
                    default:
                        break;
                }       
            }
            else {
                System.out.println("[ERROR] Please key in a valid input!\n");
            }
        }
        if (result == -1) {
            System.out.println("[SYSTEM] Selected option 'h'");
        }
        System.out.println("\n[SYSTEM] Selected option " + result); 
        return result;
    }

    // Prints page one of query list -- to reduce cognitive load
    private static void printDrQueryPageOne() {
        System.out.println("--- GENERAL DRIVER INFORMATION (1/2) ---");
        System.out.println("1. Driver rankings in a season\n" +
                           "2. Driver who has been active the longest in F1\n" +
                           "3. Oldest/Youngest driver in a season\n" +
                           "4. Driver championship winner in a season\n" +
                           "5. Driver with fastest qualifying time in a round\n" +
                           "6. Driver with fastest qualifying time in a season\n\n" +
                           "[SYSTEM] Enter one of the numbered options above (1-6) to get the information you'd like.\n" + 
                           "[SYSTEM] Enter 'p 2' to go to Page 2 or 'h' to go back to the Main Menu.\n");
    }

    // Prints page two of query list
    private static void printDrQueryPageTwo() {
        System.out.println("--- SPECIFIC DRIVER INFORMATION (2/2) ---");
        System.out.println("7. Driver's average grid position for all seasons\n" +
                           "8. Driver's specific position in specific lap # for a certain race\n" +
                           "9. Driver's total points in a specific round\n" +
                           "10. Driver's number of participated championships\n" +
                           "11. Driver's constructor history\n" +
                           "12. Driver's fastest lap in a specific season\n" +
                           "13. Driver's active years in F1\n" +
                           "14. Driver's nationality\n" +
                           "15. Driver's average lap time in a specific season\n" +
                           "16. Driver's total number of driver championship wins\n\n" +
                           "[SYSTEM] Enter one of the numbered options above (7-16) to get the information you'd like.\n" +
                           "[SYSTEM] You may enter 'p 1' to go to Page 1 or 'h' to go back to the Main Menu.\n");
    }

    /** Validate input for driver query page 
     * @param str : takes in string input to validate before going into conditions
     * @return returns a boolean whether it's valid or invalid
     */ 

    private static boolean isValidDRQInput(String str) {
        // Valid: (int) 1-16, "p 2", "p 1", "h"
        boolean result = true;
        final int START = 1; // list start
        final int END = 16; // end
        final int ONE_ASCII = 49;
        final int TWO_ASCII = 50;

        // first check if it's num input
        try {
            int numInput = Integer.parseInt(str); // valid int!
            // check if it's within query list bounds
            if (numInput < START || numInput > END) { // if not
                result = false;
            }
        }
        catch (NumberFormatException nfe) { // not a valid int, could be letters only or tokens
            String[] tokens = str.split(" ");
            if (tokens.length > 2 || tokens.length < 1) return false; // invalid command arg
            // check for valid ones
            if (tokens.length == 1) { // could only be 'h'
                if (!(str.toLowerCase().strip().equals("h"))) { // user did not choose 'h'
                    result = false;
                }
            }
            // if 2 args passed
            if (tokens.length == 2) { // if valid token
                if (tokens[0].toLowerCase().equals("p")) { // if first token is  p
                    if (tokens[1].charAt(0) < ONE_ASCII || tokens[1].charAt(0) > TWO_ASCII) { // if second token is not 1 or 2
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    /** Error-check query input based on option number
     * Each option number will be correlated to the listed queries related Driver
     * and its required parameters. Invalid inputs will simply return false -- main
     * page will handle re-prompting of input.
     * 
     * @param input : user's command input for query requested
     * @param option : corresponding query no. user selected
     * @return returns whether it's valid or not
     */ 
    private static boolean validateDrQueryInput(String input, int option) {

        boolean result = true; // ret val - assume valid by def
        String[] tokens = input.split(" ");

        // SIDE COMMENTS: expected inputs for ref
        if (option == 3) { // <!> String (older/younger), int
            // valid "older" / "younger" input
            if (tokens.length != 2) {
                return false;
            }
            if (tokens[0].toLowerCase().equals("older") || tokens[0].toLowerCase().equals("younger")) {
                if(!isValidNumInput(tokens[1])) {
                    result = false; // check 2nd token, validate
                }
            }
        }
        else if (option == 8) { // int, int, int
            if (tokens.length != 3) {
                return false;
            }
            // iterate through tokens and validate as numeric
            for (String token : tokens) {
                if(!isValidNumInput(token)) { // found one non-numeric token, command invalid!
                    return false; // immediately invalid, simply exit
                }
            }
        }
        else if (option == 9 || option == 12 || option == 15) { // int, int
            if (tokens.length != 2) {
                return false;
            }
            for (String token : tokens) {
                if(!isValidNumInput(token)) { // found one non-numeric token, command invalid!
                    return false; // immediately invalid, simply exit
                }
            }
        }
        else { // all other options only have one int input
            if (tokens.length != 1 || !isValidNumInput(tokens[0])) {
                return false;
            }
        }
       return result;
    }

    
    /* Constructor Menu helpers */

    /** Handles user inputs + UI flow for constructor query list page 
     * @return returns user input value to process query at main page 
     */

    private static int runConstructorQueryPage() {
        // User navigates through query page;
        // Expected inputs: (int) 1-7, "h" -> otherwise error-check, reject input, re-prompt
        Scanner sc = new Scanner(System.in);
        boolean exit = false; // by default, page keeps running
        int result = -1;
        printQueryListHeader("CONSTRUCTORS");
        printConstQueryPageOne(); // print page one first one time
        // while user is not exiting (not entering 'h')
        while (!exit) {   
            System.out.print("> ");
            String input = sc.nextLine().toLowerCase(); // get user input
            if (isValidCRQInput(input)) { // if valid input go through!
                switch (input){
                    case "1":
                        result = 1;
                        exit = true;
                        break;
                    case "2":
                        result = 2;
                        exit = true;
                        break;
                    case "3":
                        result = 3;
                        exit = true;
                        break;
                    case "4":
                        result = 4;
                        exit = true;
                        break;
                    case "5":
                        result = 5;
                        exit = true;
                        break;
                    case "6":
                        result = 6;
                        exit = true;
                        break;
                    case "7":
                        result = 7;
                        exit = true;
                        break;
                    case "h":
                        System.out.println("[SYSTEM] Going back to Main Menu...");
                        exit = true;
                        break;
                    default:
                        break;
                }       
            }
            else {
                System.out.println("[ERROR] Please key in a valid input!\n");
            }
        }
        if (result == -1) {
            System.out.println("[SYSTEM] Selected option 'h'");
        }
        System.out.println("\n[SYSTEM] Selected option " + result);
        return result;
    }

    /** Validate input for constructor query page 
     * @param str : takes in string input to validate before going into conditions
     * @return returns a boolean whether it's valid or invalid
     */ 

    private static boolean isValidCRQInput(String str) {
        // Valid: (int) 1-7, "h"
        boolean result = true;
        final int START = 1; // list start
        final int END = 7; // end

        // first check if it's num input
        try {
            int numInput = Integer.parseInt(str); // valid int!
            // check if it's within query list bounds
            if (numInput < START || numInput > END) return false; // if not
        }
        catch (NumberFormatException nfe) { // not a valid int, could be letters only or tokens
            String[] tokens = str.split(" ");
            if (tokens.length != 1) return false; // invalid command arg
            // check for valid ones
            // could only be 'h'
            if (!(str.toLowerCase().strip().equals("h"))) { // user did not choose 'h'
                result = false;
            }
        }
        return result;
    }

    // Prints page one of query list
    private static void printConstQueryPageOne() {
        System.out.println("--- GENERAL CONSTRUCTOR INFORMATION (1/1) ---");
        System.out.println("1. Least active constructor in a season\n\n" +
                           "--- SPECIFIC CONSTRUCTOR INFORMATION ---\n" +
                           "2. Driver with the most point contribution in a specific constructor\n" +
                           "3. Driver with the most races in a specific constructor in a specific season\n" +
                           "4. Constructor's total points in a certain round\n" +
                           "5. Constructor's total of participated championships\n" +
                           "6. Constructor's years of activity\n" +
                           "7. Constructor's nationality\n\n" +
                           "[SYSTEM] Enter one of the numbered options above (1-7) to get the information you'd like or 'h' to go back to the Main Menu.\n");
    }

    /** Error-check query input based on option number
     * Each option number will be correlated to the listed queries related to Constructors
     * and its required parameters. Invalid inputs will simply return false -- main
     * page will handle re-prompting of input.
     * 
     * @param input : user's command input for query requested
     * @param option : corresponding query no. user selected
     * @return returns whether it's valid or not
     */ 
    private static boolean validateConstQueryInput(String input, int option) {
        boolean result = true; // ret val - assume valid by def
        String[] tokens = input.split(" ");

        if (tokens.length > 2 || tokens.length < 1) return false; // invalid-check, most constr. queries
                                                                  // are only expecting 2 at most inputs
        // SIDE COMMENTS: expected inputs for ref
        if (option == 2 || option == 3 || option == 4) { // <!> String (older/younger), int
            // check if there are 2 inputs
            if (tokens.length != 2) {
                return false;
            }
            for (String token : tokens) { // make sure each token is a num!
                if(!isValidNumInput(token)) {
                    result = false; // check 2nd token, validate
                }
            }
        }
        else { // all other options only have one int input
            if (!isValidNumInput(tokens[0])) {
                return false;
            }
        }
       return result;
    }

    /* Circuit Menu helpers */

    /** Handles user inputs + UI flow for circuit query list page 
     * @return returns user input value to process query at main page 
     */

    private static int runCircuitQueryPage() {
        // User navigates through query page;
        // Expected inputs: (int) 1-16, "p 2", "p 1", "h" -> otherwise error-check, reject input, re-prompt
        Scanner sc = new Scanner(System.in);
        boolean exit = false; // by default, page keeps running
        int result = -1;
        printQueryListHeader("CIRCUITS");
        printCircQueryPageOne(); // print page one first one time
        // while user is not exiting (not entering 'h')
        while (!exit) {   
            System.out.print("> ");
            String input = sc.nextLine().toLowerCase(); // get user input
            if (isValidCRQInput(input)) { // if valid input go through!
                switch (input){
                    case "1":
                        result = 1;
                        exit = true;
                        break;
                    case "2":
                        result = 2;
                        exit = true;
                        break;
                    case "3":
                        result = 3;
                        exit = true;
                        break;
                    case "4":
                        result = 4;
                        exit = true;
                        break;
                    case "5":
                        result = 5;
                        exit = true;
                        break;
                    case "h":
                        System.out.println("[SYSTEM] Going back to Main Menu...");
                        exit = true;
                        break;
                    default:
                        break;
                }       
            }
            else {
                System.out.println("[ERROR] Please key in a valid input!\n");
            }
        }
        if (result == -1) {
            System.out.println("[SYSTEM] Selected option 'h'");
        }
        System.out.println("\n[SYSTEM] Selected option " + result); 
        //sc.close();
        return result;
    }

    /** Validate input for constructor query page 
     * @param str : takes in string input to validate before going into conditions
     * @return returns a boolean whether it's valid or invalid
     */ 
    private static boolean isValidCircQInput(String str) {
        // Valid: (int) 1-5, "h"
        boolean result = true;
        final int START = 1; // list start
        final int END = 5; // end

        // first check if it's num input
        try {
            int numInput = Integer.parseInt(str); // valid int!
            // check if it's within query list bounds
            if (numInput < START || numInput > END) return false; // if not
        }
        catch (NumberFormatException nfe) { // not a valid int, could be letters only or tokens
            String[] tokens = str.split(" ");
            if (tokens.length != 1) return false; // invalid command arg
            // check for valid ones
            // could only be 'h'
            if (!(str.toLowerCase().strip().equals("h"))) { // user did not choose 'h'
                result = false;
            }
        }
        return result;
    }

    // Prints page one of query list
    private static void printCircQueryPageOne() {
        System.out.println("--- GENERAL CIRCUIT INFORMATION (1/1) ---");
        System.out.println("1. Most raced on circuits in F1\n" +
                           "2. Total races held in each circuit for a specific season\n" +
                           "3. Most raced on circuit in all of F1\n\n" +
                           "--- SPECIFIC CIRCUIT INFORMATION ---\n" +
                           "4. Circuits located in specific hemispheres of the globe\n" +
                           "5. Coordinates of a specific circuit\n\n" +
                           "[SYSTEM] Enter one of the numbered options above (1-5) to get the information you'd like or 'h' to go back to the Main Menu.\n");
    }

    /** Error-check query input based on option number
     * Each option number will be correlated to the listed queries related to Circuits
     * and its required parameters. Invalid inputs will simply return false -- main
     * page will handle re-prompting of input.
     * 
     * @param input : user's command input for query requested
     * @param option : corresponding query no. user selected
     * @return returns whether it's valid or not
     */ 
    private static boolean validateCircQueryInput(String input, int option) {

        boolean result = true; // ret val - assume valid by def
        String[] tokens = input.split(" ");
        // check for queries that expect inputs
        if (tokens.length != 1) return false; // all expect none or only one
        // if only one, it's for option 2, 4, 5
        if (option == 2 || option == 5) { // for option 2 & 5, expecting (int) input
            if (!isValidNumInput(tokens[0])) return false; // not a num!
        } 
        else { // option 4, expecting String input
            if (isValidNumInput(tokens[0])) return false; // shouldn't be numeric!
            // otherwise valid String
            if (!(tokens[0].strip().toLowerCase().equals("south") ||
                  tokens[0].strip().toLowerCase().equals("north") ||
                  tokens[0].strip().toLowerCase().equals("east")  ||
                  tokens[0].strip().toLowerCase().equals("west"))) {
                    // if none of the above
                    return false; // invalid!
                  }
        }
       return result;
    }

    /* Races Menu helpers */

    /** Handles user inputs + UI flow for races query list page 
     * @return returns user input value to process query at main page 
     */
    private static int runRaceQueryPage() {
        // User navigates through query page;
        // Expected inputs: (int) 1-16, "p 2", "p 1", "h" -> otherwise error-check, reject input, re-prompt
        Scanner sc = new Scanner(System.in);
        boolean exit = false; // by default, page keeps running
        int result = -1;
        printQueryListHeader("RACES");
        printRaceQueryPageOne(); // print page one first one time
        // while user is not exiting (not entering 'h')
        while (!exit) {   
            System.out.print("> ");
            String input = sc.nextLine().toLowerCase(); // get user input
            if (isValidRaceQInput(input)) { // if valid input go through!
                switch (input){
                    case "1":
                        result = 1;
                        exit = true;
                        break;
                    case "2":
                        result = 2;
                        exit = true;
                        break;
                    case "3":
                        result = 3;
                        exit = true;
                        break;
                    case "4":
                        result = 4;
                        exit = true;
                        break;
                    case "h":
                        System.out.println("[SYSTEM] Going back to Main Menu...");
                        exit = true;
                        break;
                    default:
                        break;
                }       
            }
            else {
                System.out.println("[ERROR] Please key in a valid input!\n");
            }
        }
        if (result == -1) {
            System.out.println("[SYSTEM] Selected option 'h'");
        }
        System.out.println("\n[SYSTEM] Selected option " + result); 
        //sc.close();
        return result;
    }

    /** Validate input for races query page 
     * @param str : takes in string input to validate before going into conditions
     * @return returns a boolean whether it's valid or invalid
     */ 
    private static boolean isValidRaceQInput(String str) {
        // Valid: (int) 1-5, "h"
        boolean result = true;
        final int START = 1; // list start
        final int END = 4; // end

        // first check if it's num input
        try {
            int numInput = Integer.parseInt(str); // valid int!
            // check if it's within query list bounds
            if (numInput < START || numInput > END) return false; // if not
        }
        catch (NumberFormatException nfe) { // not a valid int, could be letters only or tokens
            String[] tokens = str.split(" ");
            if (tokens.length != 1) return false; // invalid command arg
            // check for valid ones
            // could only be 'h'
            if (!(str.toLowerCase().strip().equals("h"))) { // user did not choose 'h'
                result = false;
            }
        }
        return result;
    }

    // Prints page one of query list
    private static void printRaceQueryPageOne() {
        System.out.println("--- GENERAL RACES INFORMATION (1/1) ---");
        System.out.println("1. Races with the most DNF's (Did Not Finish)\n\n" +
                           "--- SPECIFIC RACE INFORMATION ---\n" +
                           "2. DNF count of each race in a specific season\n" +
                           "3. Winner of a specific race\n" +
                           "4. Driver with the fastest recorded lap time in a specific race\n\n" +
                           "[SYSTEM] Enter one of the numbered options above (1-4) to get the information you'd like or 'h' to go back to the Main Menu.\n");
    }

    /** Error-check query input based on option number
     * Each option number will be correlated to the listed queries related to Races
     * and its required parameters. Invalid inputs will simply return false -- main
     * page will handle re-prompting of input.
     * 
     * @param input : user's command input for query requested
     * @param option : corresponding query no. user selected
     * @return returns whether it's valid or not
     */ 
    private static boolean validateRaceQueryInput(String input, int option) {
        boolean result = true; // ret val - assume valid by def
        String[] tokens = input.split(" ");
        // check for queries that expect inputs
        if (tokens.length != 1) return false; // all expect only one arg
        // if only one, it should be numeric!
        if (!isValidNumInput(tokens[0])) return false; // not a num!
       return result;
    }

    /* GenStats Menu helpers */

    /** Handles user inputs + UI flow for gen stats query list page 
     * @return returns user input value to process query at main page 
     */
    private static int runGenStatsQueryPage() {
        // User navigates through query page;
        // Expected inputs: (int) 1-16, "p 2", "p 1", "h" -> otherwise error-check, reject input, re-prompt
        Scanner sc = new Scanner(System.in);
        boolean exit = false; // by default, page keeps running
        int result = -1;
        printQueryListHeader("GENERAL STATISTICS");
        printGenStatQueryPageOne(); // print page one first one time
        // while user is not exiting (not entering 'h')
        while (!exit) {   
            System.out.print("> ");
            String input = sc.nextLine().toLowerCase(); // get user input
            if (isValidGenStatQInput(input)) { // if valid input go through!
                switch (input){
                    case "1":
                        result = 1;
                        exit = true;
                        break;
                    case "2":
                        result = 2;
                        exit = true;
                        break;
                    case "3":
                        result = 3;
                        exit = true;
                        break;
                    case "h":
                        System.out.println("[SYSTEM] Going back to Main Menu...");
                        exit = true;
                        break;
                    default:
                        break;
                }       
            }
            else {
                System.out.println("[ERROR] Please key in a valid input!\n");
            }
        }
        if (result == -1) {
            System.out.println("[SYSTEM] Selected option 'h'");
        }
        System.out.println("\n[SYSTEM] Selected option " + result); 
        //sc.close();
        return result;
    }

    /** Validate input for races query page 
     * @param str : takes in string input to validate before going into conditions
     * @return returns a boolean whether it's valid or invalid
     */ 
    private static boolean isValidGenStatQInput(String str) {
        // Valid: (int) 1-5, "h"
        boolean result = true;
        final int START = 1; // list start
        final int END = 3; // end

        // first check if it's num input
        try {
            int numInput = Integer.parseInt(str); // valid int!
            // check if it's within query list bounds
            if (numInput < START || numInput > END) return false; // if not
        }
        catch (NumberFormatException nfe) { // not a valid int, could be letters only or tokens
            String[] tokens = str.split(" ");
            if (tokens.length != 1) return false; // invalid command arg
            // check for valid ones
            // could only be 'h'
            if (!(str.toLowerCase().strip().equals("h"))) { // user did not choose 'h'
                result = false;
            }
        }
        return result;
    }

    // Prints page one of query list
    private static void printGenStatQueryPageOne() {
        System.out.println("--- GENERAL STATISTICS INFORMATION (1/1) ---");
        System.out.println("1. Nationality whose drivers perform the fastest on circuits located in home-country\n" +
                           "2. Fastest lap recorded across all races in a specific season\n" +
                           "3. Average lap time of all drivers across all races in a given season\n\n" +
                           "[SYSTEM] Enter one of the numbered options above (1-3) to get the information you'd like or 'h' to go back to the Main Menu.\n");
    }

    /** Error-check query input based on option number
     * Each option number will be correlated to the listed queries related to General Statistics
     * and its required parameters. Invalid inputs will simply return false -- main
     * page will handle re-prompting of input.
     * 
     * @param input : user's command input for query requested
     * @param option : corresponding query no. user selected
     * @return returns whether it's valid or not
     */ 
    private static boolean validateGenStatQueryInput(String input, int option) {
        boolean result = true; // ret val - assume valid by def
        String[] tokens = input.split(" ");
        // check for queries that expect inputs
        if (tokens.length != 1) return false; // all expect only one arg
        // if only one, it should be numeric!
        if (!isValidNumInput(tokens[0])) return false; // not a num!
       return result;
    }

    /* Help Menu helpers */

    /** 
     * Prints help menu information 
     * @TODO fill-out help menu list
     * Discuss with ground regarding ID look-up command for races, constructors, and drivers (more in README.md)
     */ 
    private static void printHelpMenu() {
        System.out.println("--- HELP MENU ---\n" +
                           "1. TBD\n");
        System.out.println("[SYSTEM] Enter 'h' to go back to the Main Menu");
    }
}

