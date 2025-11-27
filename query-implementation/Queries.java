import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

// TODO:
// implement all queries, adjusting syntax as needed
// 		NOTE: some of the queries are on data that we decided to omit. example is fastest qualifying time, we decided to omit the data due to a ton of nulls, but we still made a query for it. i will be omitting these.
// sql injection defenses
// printing pretty

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
		}

		return true;
		//
		// try {
		// switch (input) {
		// case 1:
		// if (args.length < 1)
		// return false;
		//
		// db.query_1(args[0]);
		// break;
		//
		// case 15:
		// break;
		//
		// case 29:
		// break;
		//
		// case 42:
		// db.query_42();
		// break;
		//
		// case 61:
		// break;
		//
		// case 72:
		// break;
		//
		// case 87:
		// if (args.length < 1)
		// return false;
		// db.query_87(args[0]);
		// break;
		//
		// case 111:
		// break;
		//
		// case 127:
		// break;
		//
		// case 141:
		// if (args.length < 1)
		// return false;
		// db.query_141(args[0]);
		// break;
		//
		// case 158:
		// break;
		//
		// case 175:
		// break;
		//
		// case 186:
		// break;
		//
		// case 190:
		// break;
		//
		// case 194:
		// break;
		//
		// case 199:
		// break;
		//
		// case 203:
		// break;
		//
		// case 208:
		// break;
		//
		// case 213:
		// break;
		//
		// case 220:
		// break;
		// case 231:
		// break;
		//
		// default:
		// return false;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// return false;
		// }
		//
		// return true;

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
		try {
			String url = "jdbc:sqlite:f1.db";

			connection = DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void driverRankings(int year) {
		String sql = "select d.driverId, d.firstname, d.lastname, sum(dr.points) as totalPoints from driverRaces dr natural join races r natural join drivers d where year = ? group by driverId, firstname, lastname order by totalPoints desc";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("driverId") + " " + resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + " " + resultSet.getInt("totalPoints"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	//
	// public void query_1(String year) {
	// int parsedID = Integer.parseInt(year);
	//
	// try {
	//
	// String sql = "select d.driverId, d.firstName, d.lastName,
	// sum(dr.driverPoints) as totalPoints from driverRace dr natural join races r
	// natural join driver d where year = ? group by d.driverId, d.firstName,
	// d.lastName order by totalPoints desc";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// statement.setInt(1, parsedID);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getInt("driverID") + " " +
	// resultSet.getString("firstName") + " "
	// + resultSet.getString("lastName") + " " + resultSet.getInt("totalPoints"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_15(String year) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_29(String raceId) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_42() {
	// try {
	// String sql = "with circuitCounts as (select r.year, c.circuitID, c.name,
	// c.country, c.location, count(r.raceID) as raceCount from races r natural join
	// circuits c group by r.year, c.circuitID, c.name, c.country, c.location)
	// select cc.* from circuitCounts cc natural join (select year, max(raceCount)
	// as maxCount from circuitCounts group by year) maxCounts order by cc.year";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getInt("year") + " " +
	// resultSet.getInt("circuitID") + " "
	// + resultSet.getString("name") + " " + resultSet.getString("country") + " "
	// + resultSet.getString("location"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_61(String hemisphere) {
	// int minLat = -90;
	// int maxLat = 90;
	// int minLong = -90;
	// int maxLong = 90;
	//
	// if (hemisphere.toLowerCase().equals("north") ||
	// hemisphere.toLowerCase().equals("northern")) {
	// maxLong = 9999;
	// }
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_72(String driverId) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_87(String year) {
	// try {
	// String sql = "select d.driverID, d.firstName, d.lastName, d.dateOfBirth from
	// drivers d natural join partOf p where p.year = ? union select d.driverID,
	// d.firstName, d.lastName, d.dateOfBirth from drivers d natural join partOf p
	// where p.year = ? order by d.dateOfBirth desc limit 1";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// int parsedYear = Integer.parseInt(year);
	//
	// statement.setInt(1, parsedYear);
	// statement.setInt(2, parsedYear);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getInt("driverID") + " " +
	// resultSet.getString("firstName") + " "
	// + resultSet.getString("lastName") + " " + resultSet.getInt("dateOfBirth"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_111(String year, String constructorName) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_127(String year, String constructorName) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_141(String year) {
	// try {
	//
	// String sql = "select s.year, c.name, sum(l.lapTime) as totalTime from seasons
	// s natural join races r natural join lap l natural join drivers d join partOf
	// p on p.driverID = d.driverID and p.year = s.year natural join constructor c
	// where s.year = ? group by s.year, c.name order by totalTime limit 1";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// int parsedYear = Integer.parseInt(year);
	//
	// statement.setInt(1, parsedYear);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getInt("year") + " " +
	// resultSet.getString("name") + " "
	// + resultSet.getFloat("totalTime"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_158() {
	// try {
	//
	// String sql = "with nativeLaps as (select d.nationality, l.lapTime from lap l
	// natural join driver d natural join race r natural join circuit c join locale
	// loc on d.nationality = loc.nationality and c.country = loc.country select
	// nationality, avg(lapTime) as avgTime from nativeLaps group by nationality
	// order by avgTime limit 1;";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getInt("year") + " " +
	// resultSet.getString("name") + " "
	// + resultSet.getFloat("totalTime"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_175(String driverId, String raceId, String lapNumber) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_186(String year) {
	// try {
	// String sql = "SELECT d.driverID, d.firstName, d.lastName, SUM(r.points) AS
	// totalPts FROM results r NATURAL JOIN race ra NATURAL JOIN drivers d WHERE
	// ra.year = ? GROUP BY d.driverID ORDER BY totalPts DESC LIMIT 1";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// int parsedYear = Integer.parseInt(year);
	//
	// statement.setInt(1, parsedYear);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getInt("driverID") + " " +
	// resultSet.getString("firstName") + " "
	// + resultSet.getString("lastName") + " " + resultSet.getInt("totalPts"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_190(String circuitID) {
	// try {
	//
	// String sql = "select name, lat, lng, from circuits where circuitID = ?";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// int parsedID = Integer.parseInt(circuitID);
	//
	// statement.setInt(1, parsedID);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getString("name") + " " +
	// resultSet.getFloat("lat") + " "
	// + resultSet.getFloat("lng"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_194(String raceId) {
	// try {
	//
	// String sql = "select d.driverID, d.firstName, d.lastName from results r
	// natural join drivers d where r.raceId = ? and r.positionOrder = 1;";
	//
	// PreparedStatement statement = connection.prepareStatement(sql);
	//
	// int parsedID = Integer.parseInt(raceId);
	//
	// statement.setInt(1, parsedID);
	//
	// ResultSet resultSet = statement.executeQuery();
	//
	// while (resultSet.next()) {
	// System.out.println(resultSet.getInt("driverID") + " " +
	// resultSet.getString("firstName") + " "
	// + resultSet.getString("lastName"));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_199(String raceId, String driverId) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_203(String raceId, String constructorId) {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_208() {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_213() {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_220() {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void query_231() {
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}
