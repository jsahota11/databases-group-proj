import java.sql.Connection;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.PreparedStatement;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

// TODO:
// implement all queries, adjusting syntax as needed
// printing pretty
// perhaps some input santizing if bad params are given (out of bounds IDs for example)

public class Queries {
	static Connection connection;

	public static void main(String[] args) throws Exception {
		Database db = new Database();
		Scanner console = new Scanner(System.in);
		runConsole(db, console);

		System.out.println("Exiting...");
		console.close();
	}

	public static void runConsole(Database db, Scanner console) {

		System.out.println("Welcome to the Formula 1 database! Type h for help. ");
		System.out.println("Formula 1 DB > ");
		String line = console.nextLine();
		String[] tokens;

		while (line != null && !line.equals("q")) { // q is exit
			tokens = line.split("\\s+");

			String[] args = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, args, 0, args.length);

			if (line.trim().indexOf(" ") > 0) {
				args = new String[tokens.length - 1];
				System.arraycopy(tokens, 1, args, 0, args.length);
			} // arg provided

			if (tokens[0].equals("h"))
				printHelp();

			// need to edit this to take any array of args
			else {

				boolean validIn = validQuery(tokens[0], args, db);
				if (!validIn) {
					System.out.println("Please enter a valid option. Type h to print the help menu.");
				}
			}

			System.out.println("Formula 1 DB > ");
			line = console.nextLine();

		}
	}

	// need to edit this to take any array of args
	private static boolean validQuery(String input, String[] args, Database db) {

		if (input.equals("dr") && args.length == 1) {
			db.driverRankings(Integer.parseInt(args[0]));
		} else if (input.equals("cu") && args.length == 1) {
			db.circuitUsage(Integer.parseInt(args[0]));
		} else if (input.equals("dnfs") && args.length == 1) {
			db.racesDNF(Integer.parseInt(args[0]));
		} else if (input.equals("mfr")) {
			db.mostFinishedRaces();
		} else if (input.equals("meangp") && args.length == 1) {
			db.averageGridPos(Integer.parseInt(args[0]));
		} else if (input.equals("oyd") && args.length == 1) {
			db.extremeAgedDrivers(Integer.parseInt(args[0]));
		} else if (input.equals("dcp") && args.length == 2) {
			db.driverContributionsPoints(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else if (input.equals("dcr") && args.length == 2) {
			db.driverContributionsRaces(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else if (input.equals("dp") && args.length == 3) {
			db.positionInstance(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		} else if (input.equals("sw") && args.length == 1) {
			db.seasonWinner(Integer.parseInt(args[0]));
		} else if (input.equals("cc") && args.length == 1) {
			db.circuitCoords(Integer.parseInt(args[0]));
		} else if (input.equals("rw") && args.length == 1) {
			db.raceWinner(Integer.parseInt(args[0]));
		} else if (input.equals("rpd") && args.length == 2) {
			db.roundPtsDriver(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else if (input.equals("rpc") && args.length == 2) {
			db.roundPtsConstructor(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else if (input.equals("dcc")) {
			db.driverChampionshipCount();
		} else if (input.equals("ccc")) {
			db.constructorChampionshipCount();
		} else if (input.equals("fqr")) {
			db.fastestQualInRound();
		} else if (input.equals("dcl") && args.length == 1) {
			db.driversConstructorList(Integer.parseInt(args[0]));
		} else if (input.equals("flr") && args.length == 1) {
			db.fastestLapInRace(Integer.parseInt(args[0]));
		} else if (input.equals("dfsl") && args.length == 2) {
			db.driversFastestSeasonLap(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} else if (input.equals("sfl") && args.length == 1) {
			db.seasonsFastestLap(Integer.parseInt(args[0]));
		} else if (input.equals("day") && args.length == 1) {
			db.driversActiveYears(Integer.parseInt(args[0]));
		} else if (input.equals("cay") && args.length == 1) {
			db.constructorsActiveYears(Integer.parseInt(args[0]));
		} else if (input.equals("muc")) {
			db.mostUsedCircuit();
		} else if (input.equals("cn") && args.length == 1) {
			db.constructorsNationality(Integer.parseInt(args[0]));
		} else if (input.equals("dn") && args.length == 1) {
			db.driversNationality(Integer.parseInt(args[0]));
		} else if (input.equals("sdnf") && args.length == 1) {
			db.seasonsDNFs(Integer.parseInt(args[0]));
		} else if (input.equals("dwc") && args.length == 1) {
			db.driversWinCount(Integer.parseInt(args[0]));
		} else if (input.equals("ldc")) {
			db.longestDriverCareer();
		}

		return true;
	}

	// this will have the help menu for our DB
	private static void printHelp() {
		System.out.println("\n--- HELP START ---\n");
		System.out.println("h to get help.");

		System.out.println(
				"<line number> <parameter> - See the corresponding query in the .md file, and provide a parameter if necessary.");
		System.out.println("Queries 42, 87, 141");

		System.out.println("q to exit the program.\n");
		System.out.println("--- HELP END ---\n");
	}
}

class Database {
	private Connection connection;

	public Database() {
		Properties prop = new Properties();
		String fileName = "auth.cfg";
		try {
			FileInputStream configFile = new FileInputStream(fileName);
			prop.load(configFile);
			configFile.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Could not find config file.");
			System.exit(1);
		} catch (IOException ex) {
			System.out.println("Error reading config file.");
			System.exit(1);
		}
		String username = (prop.getProperty("username"));
		String password = (prop.getProperty("password"));

		if (username == null || password == null) {
			System.out.println("Username or password not provided.");
			System.exit(1);
		}

		String connectionUrl = "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
				+ "database=cs3380;"
				+ "user=" + username + ";"
				+ "password=" + password + ";"
				+ "encrypt=false;"
				+ "trustServerCertificate=false;"
				+ "loginTimeout=30;";

		try {
			connection = DriverManager.getConnection(connectionUrl);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	// every driver ranked for a specific season
	public void driverRankings(int year) {
		String sql = "select d.driverId, d.firstname, d.lastname, sum(dr.points) as totalPoints from driverRaces dr join races r on r.raceId = dr.raceId join drivers d on d.driverId = dr.driverId where year = ? group by d.driverId, d.firstname, d.lastname order by totalPoints desc";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Driver ID: " + resultSet.getInt("driverId") + " | Name: "
						+ resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + " | Total Points: " + resultSet.getInt("totalPoints"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many times each circuit was used in a given year
	public void circuitUsage(int year) {
		String sql = "with seasonCounts as ( select c.name, count(r.raceId) as raceCount from races r join circuits c on r.circuitId = c.circuitId where r.year = ? group by c.name) select * from seasonCounts order by raceCount desc;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getString("name") + ", "
						+ resultSet.getString("raceCount") + " races.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many drivers did not finish some race
	public void racesDNF(int raceId) {
		String sql = "select r.name, count(dr.driverId) as dnfCount from races r join driverRaces dr on r.raceId = dr.raceId where r.raceId = ? and dr.finalPosition = -1 group by r.raceId, r.name;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, raceId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getString("name") + ", " + resultSet.getInt("dnfCount") + " DNFs.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// Find some circuit with the highest number of finished races for each season
	public void mostFinishedRaces() {
		String sql = "with circuitCounts as ( select r.year, c.circuitId, c.name, c.country, c.location, count(r.raceId) as raceCount from races r join circuits c on r.circuitId = c.circuitId group by r.year, c.circuitId, c.name, c.country, c.location) select cc.* from circuitCounts cc join (select year, max(raceCount) as maxCount from circuitCounts group by year) x on cc.year = x.year order by cc.year;";

		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("year") + ", " + resultSet.getInt("circuitId") + ": "
						+ resultSet.getString("name") + " | " + resultSet.getString("location") + ", "
						+ resultSet.getString("country") + ", " + resultSet.getInt("raceCount") + " finishes.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// What are the circuits found in a specific hemisphere in the globe
	public void hemispheres(String direction) {
		// String sql = "select r.name, count(dr.driverId) as dnfCount from races r join
		// driverRaces dr on r.raceId = dr.raceId where r.raceId = ? and
		// dr.finalPosition = -1 group by r.raceId, r.name;";
		//
		// try {
		//
		// PreparedStatement statement = connection.prepareStatement(sql);
		//
		// statement.setInt(1, raceId);
		//
		// ResultSet resultSet = statement.executeQuery();
		//
		// while (resultSet.next()) {
		// System.out.println(resultSet.getString("name") + ", " +
		// resultSet.getInt("dnfCount") + " DNFs.");
		// }
		//
		// System.out.println();
		//
		// } catch (SQLException e) {
		// System.out.println("Something went wrong...");
		// e.printStackTrace();
		// }
	}

	// average grid position for a driver per season
	public void averageGridPos(int driverId) {
		String sql = "select r.year, avg(dr.gridPosition) as avgPos from driverRaces dr join drivers d on dr.driverId = d.driverId join races r on r.raceId = dr.raceId where d.driverId = ? group by r.year order by r.year;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("year") + ", " + resultSet.getInt("avgPos"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// oldest and youngest drivers in a given season
	// figure out how to make this work, dont want to use two separate queries but
	// might have to
	// issue is the two order by, there can only be one ocurring after the union
	// probably have to use aggregates to grab a min on dob
	public void extremeAgedDrivers(int year) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, d.dob from drivers d join partOf p on d.driverId = p.driverId where p.year = ? order by d.dob union select top (1) d.driverId, d.firstname, d.lastname, d.dob from drivers d join partOf p on d.driverId = p.driverId where p.year = ? order by d.dob desc;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);
			statement.setInt(2, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + ", " + resultSet.getString("dob"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// given a season and constructor, get the driver that contributed the most to a
	// constructor by points earned
	public void driverContributionsPoints(int year, int constructorId) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, sum(dr.points) as totalPoints from driverRaces dr join races r on dr.raceId = r.raceId join drivers d on d.driverId = dr.driverId join partOf p on p.driverId = dr.driverId and p.year = r.year where r.year = ? and p.constructorId = ? group by d.driverId, d.firstname, d.lastname order by totalPoints desc;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);
			statement.setInt(2, constructorId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + ", " + resultSet.getInt("totalPoints"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// given a season and constructor, get the driver that contributed the most to a
	// constructor by races completed
	public void driverContributionsRaces(int year, int constructorId) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, count(dr.raceId) as raceCount from driverRaces dr join races r on dr.raceId = r.raceId join drivers d on d.driverId = dr.driverId join partOf p on p.driverId = dr.driverId and p.year = r.year where r.year = ? and p.constructorId = ? group by d.driverId, d.firstname, d.lastname order by raceCount desc;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);
			statement.setInt(2, constructorId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + ", " + resultSet.getInt("raceCount"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// which constructor spent the least amount of time driving in a given season
	// need to figure out an easy way to sum lap times since they are STRINGS
	public void quickestConstructor(int year) {
		// String sql = "select top (1) s.year, c.name, sum(l.time) as totalTime from
		// seasons s join races r on r.year = s.year join laps l on l.raceId = r.raceId
		// join drivers d on d.driverId = l.driverId join partOf p on p.driverId =
		// d.driverId and p.year = s.year join constructor c on p.constructorId =
		// c.constructorId where s.year = ? group by s.year, c.name order by
		// totalTime;";
		//
		// try {
		//
		// PreparedStatement statement = connection.prepareStatement(sql);
		//
		// statement.setInt(1, year);
		//
		// ResultSet resultSet = statement.executeQuery();
		//
		// while (resultSet.next()) {
		// System.out.println(resultSet.getInt("year") + ": " +
		// resultSet.getString("name") + " "
		// + resultSet.getString("lastname") + ", " + resultSet.getInt("raceCount"));
		// }
		//
		// System.out.println();
		//
		// } catch (SQLException e) {
		// System.out.println("Something went wrong...");
		// e.printStackTrace();
		// }
	}

	// which nationality drove the fastest average lap on circuits native to their
	// country
	// figure out how to make the lap times numbers since we need to average NOOO
	public void fastestOnNativeCircuits() {
		// String sql = "with nativeLaps as ( select d.nationality, l.lapTime, from )";
		//
		// try {
		//
		// Statement statement = connection.createStatement();
		//
		// ResultSet resultSet = statement.executeQuery(sql);
		//
		// while (resultSet.next()) {
		// System.out.println(resultSet.getInt("year") + ", " +
		// resultSet.getInt("circuitId") + ": "
		// + resultSet.getString("name") + " | " + resultSet.getString("location") + ",
		// "
		// + resultSet.getString("country") + ", " + resultSet.getInt("raceCount") + "
		// finishes.");
		// }
		//
		// System.out.println();
		//
		// } catch (SQLException e) {
		// System.out.println("Something went wrong...");
		// e.printStackTrace();
		// }
	}

	// what position was a driver in during a specific lap of a specific race
	public void positionInstance(int driverId, int raceId, int number) {
		String sql = "select position from laps where driverId = ? and raceId = ? and lapNumber = ?;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);
			statement.setInt(2, raceId);
			statement.setInt(3, number);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Position " + resultSet.getInt("position"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// who won in a season
	public void seasonWinner(int year) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, sum(dr.points) as totalPts from driverRaces dr join races r on r.raceId = dr.raceId join drivers d on d.driverId = dr.driverId where r.year = ? group by d.driverId, d.firstname, d.lastname order by totalPts desc;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + ", " + resultSet.getInt("totalPts") + " points.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// what are the coordinates of a circuit
	public void circuitCoords(int circuitId) {
		String sql = "select name, lat, lng, alt from circuits where circuitId = ?;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, circuitId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(
						resultSet.getString("name") + ": Latitude = " + resultSet.getFloat("lat") + ", Longitude = "
								+ resultSet.getFloat("lng") + ", Altitude = " + resultSet.getFloat("alt") + ".");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// who won this race
	public void raceWinner(int raceId) {
		String sql = "select d.driverId, d.firstname, d.lastname from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.raceId = ? and dr.finalPosition = 1;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, raceId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(
						resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
								+ resultSet.getString("lastname"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many points did a driver get for a specific round
	public void roundPtsDriver(int raceId, int driverId) {
		String sql = "select d.driverId, d.firstname, d.lastname, dr.points from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.raceId = ? and dr.driverId = ?;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, raceId);
			statement.setInt(2, driverId);

			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.isBeforeFirst()) { // empty?
				System.out.println("That driver did not participate in that race.");
			} else {
				while (resultSet.next()) {
					System.out.println(
							resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
									+ resultSet.getString("lastname") + ", " + resultSet.getInt("points") + " points.");
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many points did a constructor get for a specific round
	public void roundPtsConstructor(int raceId, int constructorId) {
		String sql = "select c.constructorId, c.name, cr.points from constructorRaces cr join constructors c on c.constructorId = cr.constructorId where cr.raceId = ? and cr.constructorId = ?;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, raceId);
			statement.setInt(2, constructorId);

			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.isBeforeFirst()) { // empty?
				System.out.println("That constructor did not participate in that race.");
			} else {
				while (resultSet.next()) {
					System.out.println(
							resultSet.getInt("constructorId") + ": " + resultSet.getString("name") + ", "
									+ resultSet.getInt("points") + " points.");
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many championships did a driver participate in
	public void driverChampionshipCount() {
		String sql = "select d.driverId, d.firstname, d.lastname, count(distinct r.year) as seasonCount from driverRaces dr join drivers d on d.driverId = dr.driverId join races r on r.raceId = dr.raceId group by d.driverId, d.firstname, d.lastname;";

		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				System.out.println(
						resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
								+ resultSet.getString("lastname") + ", " + resultSet.getInt("seasonCount")
								+ " championships.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many championships did a constructor participate in
	public void constructorChampionshipCount() {
		String sql = "select c.constructorId, c.name, count(distinct r.year) as seasonCount from constructorRaces cr join constructors c on c.constructorId = cr.constructorId join races r on r.raceId = cr.raceId group by c.constructorId, c.name;";

		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				System.out.println(
						resultSet.getInt("constructorId") + ": " + resultSet.getString("name") + ", "
								+ resultSet.getInt("seasonCount")
								+ " championships.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// who had the fastest qualifying time in a particular round
	public void fastestQualInRound() {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual1 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.qual1 is not null union select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual2 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.qual2 is not null union select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual3 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.qual3 is not null order by qualTime;";

		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				System.out.println(
						resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
								+ resultSet.getString("lastname") + ", Race " + resultSet.getInt("raceId") + ": "
								+ resultSet.getString("qualTime"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// who had the fastest qualifying time in a particular season
	public void fastestQualInSeason() {
		// String sql = "select top (1) d.driverId, d.firstname, d.lastname, dr.raceId,
		// dr.qual1 as qualTime from driverRaces dr join drivers d on d.driverId =
		// dr.driverId where dr.qual1 is not null union select top (1) d.driverId,
		// d.firstname, d.lastname, dr.raceId, dr.qual2 as qualTime from driverRaces dr
		// join drivers d on d.driverId = dr.driverId where dr.qual2 is not null union
		// select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual3 as
		// qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId where
		// dr.qual3 is not null order by qualTime;";
		//
		// try {
		//
		// Statement statement = connection.createStatement();
		//
		// ResultSet resultSet = statement.executeQuery(sql);
		//
		// while (resultSet.next()) {
		// System.out.println(
		// resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
		// + resultSet.getString("lastname") + ", Race " + resultSet.getInt("raceId") +
		// ": "
		// + resultSet.getString("qualTime"));
		// }
		//
		// System.out.println();
		//
		// } catch (SQLException e) {
		// System.out.println("Something went wrong...");
		// e.printStackTrace();
		// }
	}

	// which constructors has a driver been part of
	public void driversConstructorList(int driverId) {
		String sql = "select c.name, p.year from partOf p join drivers d on p.driverId = d.driverId join constructors c on c.constructorId = p.constructorId where p.driverId = ? ";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(
						resultSet.getString("name") + " during " + resultSet.getInt("year") + ".");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// the fastest lap in any given race
	public void fastestLapInRace(int raceId) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, r.raceId, r.name, l.lapNumber, l.position, l.time from laps l join races r on l.raceId = r.raceId join drivers d on d.driverId = l.driverId where r.raceId = ? order by l.time;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, raceId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(
						resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
								+ resultSet.getString("lastname") + " was in position " + resultSet.getInt("position")
								+ " when they timed " + resultSet.getString("time") + " on lap "
								+ resultSet.getInt("lapNumber") + " of Race " + resultSet.getInt("raceId") + ": "
								+ resultSet.getString("name") + ".");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// the fastest lap in a specific season for a given driver
	public void driversFastestSeasonLap(int year, int driverId) {
		String sql = "select top (1) l.* from laps l join races r on r.raceId = l.raceId where l.driverId = ? and r.year = ? order by l.time;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);
			statement.setInt(2, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Race ID: " + resultSet.getInt("raceId") + ", Driver ID: "
						+ resultSet.getInt("driverId") + ", Lap Number: " + resultSet.getInt("lapNumber")
						+ ", Position: " + resultSet.getInt("position") + ", Time: " + resultSet.getString("time"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// the fastest lap in specific season among all drivers
	public void seasonsFastestLap(int year) {
		String sql = "select top (1) l.* from laps l join races r on l.raceId = r.raceId where r.year = ? order by l.time;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println("Race ID: " + resultSet.getInt("raceId") + ", Driver ID: "
						+ resultSet.getInt("driverId") + ", Lap Number: " + resultSet.getInt("lapNumber")
						+ ", Position: " + resultSet.getInt("position") + ", Time: " + resultSet.getString("time"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// which years a driver was active
	public void driversActiveYears(int driverId) {
		String sql = "select p.year from partOf p where p.driverId = ?;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("year"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// which years a constructor was active
	public void constructorsActiveYears(int constructorId) {
		String sql = "select distinct p.year from partOf p where p.constructorId = ?;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, constructorId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("year"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// most used circuit in all of F1
	public void mostUsedCircuit() {
		String sql = "select c.* from circuits c where c.circuitId in (select top (1) r.circuitId as uses from races r group by r.circuitId order by count(r.raceId) desc);";

		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("circuitId") + ": " + resultSet.getString("name") + ", "
						+ resultSet.getString("location") + ", " + resultSet.getString("country") + ", Latitude = "
						+ resultSet.getFloat("lat") + ", Longitude = " + resultSet.getFloat("lng") + ", Altitude = "
						+ resultSet.getFloat("alt"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// nationality of a given driver
	public void driversNationality(int driverId) {
		String sql = "select nationality from drivers d where d.driverId = ?;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getString("nationality"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// nationality of a given constructor
	public void constructorsNationality(int constructorId) {
		String sql = "select nationality from constructors c where c.constructorId = ?;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, constructorId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getString("nationality"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// average lap time of a driver for a given season
	public void driversSeasonalLapTimeAvg(int driverId) {
		// String sql = "select nationality from constructors c where c.constructorId =
		// ?;";
		// try {
		//
		// PreparedStatement statement = connection.prepareStatement(sql);
		//
		// statement.setInt(1, constructorId);
		//
		// ResultSet resultSet = statement.executeQuery();
		//
		// while (resultSet.next()) {
		// System.out.println(resultSet.getString("nationality"));
		// }
		//
		// System.out.println();
		//
		// } catch (SQLException e) {
		// System.out.println("Something went wrong...");
		// e.printStackTrace();
		// }
	}

	// average lap time across all drivers over a season
	public void seasonalLapTimeAvg(int year) {
		// String sql = "select nationality from constructors c where c.constructorId =
		// ?;";
		// try {
		//
		// PreparedStatement statement = connection.prepareStatement(sql);
		//
		// statement.setInt(1, constructorId);
		//
		// ResultSet resultSet = statement.executeQuery();
		//
		// while (resultSet.next()) {
		// System.out.println(resultSet.getString("nationality"));
		// }
		//
		// System.out.println();
		//
		// } catch (SQLException e) {
		// System.out.println("Something went wrong...");
		// e.printStackTrace();
		// }
	}

	// which race in a given season had the most DNFs
	public void seasonsDNFs(int year) {
		String sql = "select top (1) r.raceId, r.name, count(dr.finalPosition) as fps from races r join driverRaces dr on dr.raceId = r.raceId where dr.finalPosition = -1 and r.year = ? group by r.raceId, r.name order by fps desc;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("raceId") + ": " + resultSet.getString("name") + ", "
						+ resultSet.getInt("fps") + " DNFs.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many times a specific driver won the driver championship
	public void driversWinCount(int driverId) {
		String sql = "select count(dc.driverId) as wins from driverChampionships dc where dc.driverId = ? and dc.winner = 1;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("wins") + " Driver Championships won.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// which driver has participated in f1 the longest
	public void longestDriverCareer() {
		String sql = "with driverCareers as (select d.driverId, count(p.year) as activeYears from drivers d join partOf p on d.driverId = p.driverId group by d.driverId), maxDriverCareers as (select driverId, activeYears from driverCareers where activeYears = (select max(activeYears) from driverCareers)) select d.*, m.activeYears from drivers d join maxDriverCareers m on m.driverId = d.driverId;";
		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("driverId") + ": " + resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + " drove competitively in F1 for "
						+ resultSet.getInt("activeYears") + " years.");
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

}
