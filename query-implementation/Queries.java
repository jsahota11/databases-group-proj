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
		String arg = "";

		while (line != null && !line.equals("q")) { // q is exit
			tokens = line.split("\\s+");
			if (line.indexOf(" ") > 0) // arg provided
				arg = line.substring(line.indexOf(" ")).trim();

			if (tokens[0].equals("h"))
				printHelp();

			// need to edit this to take any array of args
			else if (!validQuery(tokens[0], arg, db)) {
				System.out.println("Please enter a valid option. Type h to print the help menu.");

				System.out.println("Formula 1 DB > ");
				line = console.nextLine();
			}

		}
	}

	// need to edit this to take any array of args
	private static boolean validQuery(String input, String arg, Database db) {
		try {
			int command = Integer.parseInt(input);

			// in each case, call the method from the Database

			switch (command) {
				case 1:
					db.query_1(arg);
					break;

				case 15:
					break;

				case 29:
					break;

				case 42:
					break;

				case 61:
					break;

				case 72:
					break;

				case 87:
					break;

				case 111:
					break;

				case 127:
					break;

				case 141:
					break;

				case 158:
					break;

				case 175:
					break;

				case 186:
					break;

				case 190:
					break;

				case 194:
					break;

				case 199:
					break;

				case 203:
					break;

				case 208:
					break;

				case 213:
					break;

				case 220:
					break;
				case 231:
					break;

				default:
					break;
			}
		} catch (Exception e) {
			return false;
		}

		return true;

	}

	// this will have the help menu for our DB
	private static void printHelp() {
		System.out.println("--- HELP START ---\n");
		System.out.println("h to get help.");

		System.out.println(
				"<line number> <parameter> - See the corresponding query in the .md file, and provide a parameter if necessary.");

		System.out.println("q to exit the program.\n");
		System.out.println("--- HELP END ---");
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

	public void query_1(String year) {
		int parsedID = Integer.parseInt(year);

		try {

			String sql = "select d.driverId, d.firstName, d.lastName, sum(dr.driverPoints) as totalPoints from driverRace dr natural join race r natural join driver d where year = ? group by d.driverId, d.firstName, d.lastName order by totalPoints desc";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, parsedID);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				System.out.println(resultSet.getInt("driverID") + " " + resultSet.getString("firstName") + " "
						+ resultSet.getString("lastName") + " " + resultSet.getInt("totalPoints"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void query_15(String year) {
	}

	public void query_29(String raceId) {
	}

	public void query_42() {
	}

	public void query_61(String hemisphere) {
	}

	public void query_72(String driverId) {
	}

	public void query_87(String year) {
	}

	public void query_111(String year, String constructorName) {
	}

	public void query_127(String year, String constructorName) {
	}

	public void query_141(String year) {
	}

	public void query_158() {
	}

	public void query_175(String driverId, String raceId, String lapNumber) {
	}

	public void query_186(String year) {
	}

	public void query_190(String circuitId) {
	}

	public void query_194(String raceId) {
	}

	public void query_199(String raceId, String driverId) {
	}

	public void query_203(String raceId, String constructorId) {
	}

	public void query_208() {
	}

	public void query_213() {
	}

	public void query_220() {
	}

	public void query_231() {
	}

}
