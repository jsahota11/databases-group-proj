package com.database.queries;

import java.sql.Connection;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.PreparedStatement;

import java.lang.StringBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

public class Database {
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-15s %-30s %-15s%n", "Driver ID", "Name", "Total Points");
				System.out.println("--------------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-15d%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("totalPoints"));
				}

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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-30s %-15s%n", "Name", "Races");
				System.out.println("-----------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-30s %-15d%n",
							resultSet.getString("name"),
							resultSet.getInt("raceCount"));
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many drivers did not finish some race, for all races
	public void racesDNF(int year) {
		String sql = "select r.raceId, r.name, count(dr.driverId) as dnfCount from races r join driverRaces dr on r.raceId = dr.raceId where r.year = ? and dr.finalPosition = -1 group by r.name, r.raceId order by dnfCount desc;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-15s %-30s %-10s%n", "Race ID", "Name", "DNFs");
				System.out.println("-----------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-10d%n",
							resultSet.getInt("raceId"),
							resultSet.getString("name"),
							resultSet.getInt("dnfCount"));
				}
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

			System.out.printf(
					"%-6s %-15s %-30s %-20s %-20s %-10s%n",
					"Year", "CircuitID", "Name", "Location", "Country", "Finishes");
			System.out.println("-------------------------------------------------------------------------------"
					+ "----------------------------------");

			while (resultSet.next()) {
				System.out.printf(
						"%-6d %-15d %-30s %-20s %-20s %-10d%n",
						resultSet.getInt("year"),
						resultSet.getInt("circuitId"),
						resultSet.getString("name"),
						resultSet.getString("location"),
						resultSet.getString("country"),
						resultSet.getInt("raceCount"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// What are the circuits found in a specific hemisphere in the globe
	public void hemispheres(int[] bounds) {
		// bounds[0] is left bound
		// 1 is right bound
		// 2 is upper bound
		// 3 is lower bound
		String sql = "select c.circuitId, c.name, c.location, c.country from circuits c where c.lat between ? and ? and c.lng between ? and ?";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, bounds[0]);
			statement.setInt(2, bounds[1]);
			statement.setInt(3, bounds[3]);
			statement.setInt(4, bounds[2]);

			ResultSet resultSet = statement.executeQuery();

			System.out.printf("%-15s %-30s %-20s %-20s%n",
					"Circuit ID", "Name", "Location", "Country");
			System.out.println("--------------------------------------------------------------------------");

			while (resultSet.next()) {
				System.out.printf(
						"%-15d %-30s %-20s %-20s%n",
						resultSet.getInt("circuitId"),
						resultSet.getString("name"),
						resultSet.getString("location"),
						resultSet.getString("country"));
			}
			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// average grid position for a driver per season
	public void averageGridPos(int driverId) {
		String sql = "select r.year, avg(dr.gridPosition) as avgPos from driverRaces dr join drivers d on dr.driverId = d.driverId join races r on r.raceId = dr.raceId where d.driverId = ? group by r.year order by r.year;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No driver corresponding to that driver ID is recorded in our data.");
			} else {

				System.out.printf("%-6s %-12s%n", "Year", "Avg Pos");
				System.out.println("-----------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-6d %-12d%n",
							resultSet.getInt("year"),
							resultSet.getInt("avgPos"));
				}
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
	public void youngestDriver(int year) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, d.dob from drivers d join partOf p on d.driverId = p.driverId where p.year = ? order by d.dob;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-15s %-30s %-15s%n",
						"Driver ID", "Name", "DOB");
				System.out.println("--------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-15s%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getDate("dob"));
				}
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
	public void oldestDriver(int year) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, d.dob from drivers d join partOf p on d.driverId = p.driverId where p.year = ? order by d.dob desc;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-15s %-30s %-15s%n", "Driver ID", "Name", "DOB");
				System.out.println("--------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-15s%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getDate("dob"));
				}
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

			if (!resultSet.isBeforeFirst()) {
				System.out.println(
						"That constructor was not active during that year OR we dont have data for that year OR there is no constructor associated with that ID.");
			} else {

				System.out.printf("%-15s %-30s %-12s%n", "Driver ID", "Name", "Total Points");
				System.out.println("--------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-12d%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("totalPoints"));
				}
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

			if (!resultSet.isBeforeFirst()) {
				System.out.println(
						"That constructor was not active during that season OR we do not have data for that season OR there is no constructor associated with that ID.");
			} else {

				System.out.printf("%-15s %-30s %-10s%n", "Driver ID", "Name", "Races");
				System.out.println("--------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-10d%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("raceCount"));
				}
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
		String sql = "select top (1) s.year, c.name, dateadd(millisecond, sum(datediff(millisecond, 0, time)),0) as totalTime from seasons s join races r on r.year = s.year join laps l on l.raceId = r.raceId	join drivers d on d.driverId = l.driverId join partOf p on p.driverId =	d.driverId and p.year = s.year join constructors c on p.constructorId =	c.constructorId where s.year = ? group by s.year, c.name order by totalTime;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-6s %-30s %-15s%n", "Year", "Name", "Total Time");
				System.out.println("--------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-6d %-30s %-15s%n",
							resultSet.getInt("year"),
							resultSet.getString("name"),
							resultSet.getTime("totalTime"));
				}
			}
			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// which nationality drove the fastest average lap on circuits native to their
	// country
	// figure out how to make the lap times numbers since we need to average NOOO
	public void fastestOnNativeCircuits() {
		String sql = "with nativeLaps as ( select d.nationality, loc.country, l.time from laps l join drivers d on d.driverId = l.driverId join races r on r.raceId = l.raceId join circuits c on c.circuitId = r.circuitId join locale loc on d.nationality = loc.nationality and c.country = loc.country) select top (1) nationality, country, dateadd(millisecond, avg(datediff(millisecond, 0, time)), 0) as avgTime from nativeLaps group by nationality, country order by avgTime;";

		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			System.out.printf("%-15s %-15s %-20s%n", "Nationality", "Avg Lap Time", "Country");
			System.out.println("------------------------------------------------------------");

			while (resultSet.next()) {
				System.out.printf(
						"%-15s %-15s %-20s%n",
						resultSet.getString("nationality"),
						resultSet.getTime("avgTime"),
						resultSet.getString("country"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println(
						"That driver did not participate in that race OR there is no driver associated with that driver ID OR there is no race associated with that race ID OR that numbered lap is not present in the race.");
			} else {

				System.out.printf("%-15s%n", "Position");
				System.out.println("----------");

				while (resultSet.next()) {
					System.out.printf("%-15d%n", resultSet.getInt("position"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-15s %-30s %-12s%n", "Driver ID", "Name", "Total Points");
				System.out.println("--------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-12d%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("totalPts"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("There is no circuit corresponding to that circuit ID.");
			} else {

				System.out.printf("%-30s %-12s %-12s %-15s%n", "Name", "Latitude", "Longitude", "Altitude");
				System.out.println("---------------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-30s %-12.6f %-12.6f %-15.2f%n",
							resultSet.getString("name"),
							resultSet.getFloat("lat"),
							resultSet.getFloat("lng"),
							resultSet.getFloat("alt"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("There is no race corresponding to that race ID.");
			} else {

				System.out.printf("%-15s %-30s%n", "Driver ID", "Name");
				System.out.println("----------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println(
						"That driver did not participate in that race OR there is no race corresponding to that race ID OR there is no driver corresponding to that driver ID.");
			} else {

				System.out.printf("%-15s %-30s %-10s%n", "Driver ID", "Name", "Points");
				System.out.println("-----------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-10d%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("points"));
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
				System.out.println(
						"That constructor did not participate in that race OR no race is associated with that race ID OR no constructor is associated with that constructor ID.");
			} else {

				System.out.printf("%-15s %-30s %-15s%n", "Constructor ID", "Name", "Points");
				System.out.println("-----------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-15d%n",
							resultSet.getInt("constructorId"),
							resultSet.getString("name"),
							resultSet.getInt("points"));
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many championships did a driver participate in
	public void driverChampionshipCount(int driverId) {
		String sql = "select d.firstname, d.lastname, count(distinct r.year) as seasonCount from driverRaces dr join drivers d on d.driverId = dr.driverId join races r on r.raceId = dr.raceId where d.driverId = ? group by d.firstname, d.lastname;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No driver is associated with that driver ID.");
			} else {

				System.out.printf("%-30s %-12s%n", "Name", "Championships");
				System.out.println("----------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-30s %-12d%n",
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("seasonCount"));
				}
			}
			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// how many championships did a constructor participate in
	public void constructorChampionshipCount(int constructorId) {
		String sql = "select c.name, count(distinct r.year) as seasonCount from constructorRaces cr join constructors c on c.constructorId = cr.constructorId join races r on r.raceId = cr.raceId where c.constructorId = ? group by c.name;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, constructorId);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No constructor is associated with that constructor ID.");
			} else {

				System.out.printf("%-30s %-15s%n", "Name", "Championships");
				System.out.println("--------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-30s %-15d%n",
							resultSet.getString("name"),
							resultSet.getInt("seasonCount"));
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// who had the fastest qualifying time in a particular round
	public void fastestQualInRound(int raceId) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual1 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.qual1 is not null and dr.raceId = ? union select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual2 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.qual2 is not null and dr.raceId = ? union select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual3 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId where dr.qual3 is not null and dr.raceId = ? order by qualTime;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, raceId);
			statement.setInt(2, raceId);
			statement.setInt(3, raceId);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No race is associated with that race ID.");
			} else {

				System.out.printf("%-15s %-30s %-10s %-12s%n", "Driver ID", "Name", "Race ID", "Qual Time");
				System.out.println("---------------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-10d %-12s%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("raceId"),
							resultSet.getTime("qualTime"));
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// who had the fastest qualifying time in a particular season
	public void fastestQualInSeason(int year) {
		String sql = "select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual1 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId join races r on r.raceId = dr.raceId where dr.qual1 is not null and r.year = ? union select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual2 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId join races r on r.raceId = dr.raceId where dr.qual2 is not null and r.year = ? union select top (1) d.driverId, d.firstname, d.lastname, dr.raceId, dr.qual3 as qualTime from driverRaces dr join drivers d on d.driverId = dr.driverId join races r on r.raceId = dr.raceId where dr.qual3 is not null and r.year = ? order by qualTime;";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);
			statement.setInt(2, year);
			statement.setInt(3, year);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-15s %-30s %-10s %-12s%n", "Driver ID", "Name", "Race ID", "Qual Time");
				System.out.println("---------------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-10d %-12s%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("raceId"),
							resultSet.getString("qualTime"));
				}
			}
			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// which constructors has a driver been part of;
	public void driversConstructorList(int driverId) {
		String sql = "select c.name, p.year from partOf p join drivers d on p.driverId = d.driverId join constructors c on c.constructorId = p.constructorId where p.driverId = ? ";

		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, driverId);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No driver is associated with that driver ID.");
			} else {

				System.out.printf("%-30s %-6s%n", "Name", "Year");
				System.out.println("-----------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-30s %-6d%n",
							resultSet.getString("name"),
							resultSet.getInt("year"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No race is associated with that race ID.");
			} else {

				System.out.printf("%-15s %-30s %-12s %-12s %-10s %-8s %-25s%n",
						"Driver ID", "Name", "Position", "Time", "Lap", "Race ID", "Race Name");
				System.out.println(
						"-----------------------------------------------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-15d %-30s %-12d %-12s %-10d %-8d %-25s%n",
							resultSet.getInt("driverId"),
							resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
							resultSet.getInt("position"),
							resultSet.getTime("time"),
							resultSet.getInt("lapNumber"),
							resultSet.getInt("raceId"),
							resultSet.getString("name"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println(
						"That driver did not participate during that year OR the season corresponding to that year is not recorded in our data OR there is no driver associated with that driver ID.");
			} else {

				System.out.printf("%-12s %-15s %-10s %-10s %-12s%n",
						"Race ID", "Driver ID", "Lap", "Position", "Time");
				System.out.println("-------------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-12d %-15d %-10d %-10d %-12s%n",
							resultSet.getInt("raceId"),
							resultSet.getInt("driverId"),
							resultSet.getInt("lapNumber"),
							resultSet.getInt("position"),
							resultSet.getTime("time"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-12s %-15s %-10s %-10s %-12s%n",
						"Race ID", "Driver ID", "Lap", "Position", "Time");
				System.out.println("-------------------------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-12d %-15d %-10d %-10d %-12s%n",
							resultSet.getInt("raceId"),
							resultSet.getInt("driverId"),
							resultSet.getInt("lapNumber"),
							resultSet.getInt("position"),
							resultSet.getTime("time"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("There is no driver corresponding to that driver ID.");
			} else {

				System.out.printf("%-6s%n", "Year");
				System.out.println("------");

				while (resultSet.next()) {
					System.out.printf("%-6d%n", resultSet.getInt("year"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("There is no constructor corresponding to that constructor ID.");
			} else {

				System.out.printf("%-6s%n", "Year");
				System.out.println("------");

				while (resultSet.next()) {
					System.out.printf("%-6d%n", resultSet.getInt("year"));
				}
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

			System.out.printf("%-15s %-30s %-15s %-15s %-12s %-12s %-10s%n",
					"Circuit ID", "Name", "Location", "Country", "Latitude", "Longitude", "Altitude");
			System.out.println(
					"-------------------------------------------------------------------------------------------");

			while (resultSet.next()) {
				System.out.printf(
						"%-15d %-30s %-15s %-15s %-12.6f %-12.6f %-10.2f%n",
						resultSet.getInt("circuitId"),
						resultSet.getString("name"),
						resultSet.getString("location"),
						resultSet.getString("country"),
						resultSet.getFloat("lat"),
						resultSet.getFloat("lng"),
						resultSet.getFloat("alt"));
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("There is no driver corresponding to that driver ID.");
			} else {

				System.out.printf("%-15s%n", "Nationality");
				System.out.println("---------------");

				while (resultSet.next()) {
					System.out.printf("%-15s%n", resultSet.getString("nationality"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("There is no constructor corresponding to that constructor ID.");
			} else {

				System.out.printf("%-15s%n", "Nationality");
				System.out.println("---------------");

				while (resultSet.next()) {
					System.out.printf("%-15s%n", resultSet.getString("nationality"));
				}
			}
			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// average lap time of a driver for a given season
	public void driversSeasonalLapTimeAvg(int driverId, int year) {
		String sql = "select dateadd(millisecond, avg(datediff(millisecond, 0, l.time)), 0) as avgTime from laps l join races r on r.raceId = l.raceId where r.year = ? and l.driverId = ?;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);
			statement.setInt(2, driverId);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println(
						"That driver did not participate during that season OR we do not have data relating to that season OR the driver associated with that driver ID does not exist.");
			} else {

				System.out.printf("%-12s%n", "Avg Time");
				System.out.println("------------");

				while (resultSet.next()) {
					System.out.printf("%-12s%n", resultSet.getTime("avgTime"));
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// average lap time across all drivers over a season
	public void seasonalLapTimeAvg(int year) {
		String sql = "select dateadd(millisecond, avg(datediff(millisecond, 0, l.time)), 0) as avgTime from laps l join races r on r.raceId = l.raceId where r.year = ?;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-12s%n", "Avg Time");
				System.out.println("------------");

				while (resultSet.next()) {
					System.out.printf("%-12s%n", resultSet.getTime("avgTime"));
				}
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// which race in a given season had the most DNFs
	public void seasonsDNFs(int year) {
		String sql = "select top (1) r.raceId, r.name, count(dr.finalPosition) as fps from races r join driverRaces dr on dr.raceId = r.raceId where dr.finalPosition = -1 and r.year = ? group by r.raceId, r.name order by fps desc;";
		try {

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, year);

			ResultSet resultSet = statement.executeQuery();

			if (!resultSet.isBeforeFirst()) {
				System.out.println("The season corresponding to that year is not recorded in our data.");
			} else {

				System.out.printf("%-12s %-30s %-6s%n", "Race ID", "Name", "DNFs");
				System.out.println("--------------------------------------------");

				while (resultSet.next()) {
					System.out.printf(
							"%-12d %-30s %-6d%n",
							resultSet.getInt("raceId"),
							resultSet.getString("name"),
							resultSet.getInt("fps"));
				}
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
			if (!resultSet.isBeforeFirst()) {
				System.out.println("There is no driver corresponding to that driver ID.");
			} else {

				System.out.printf("%-12s%n", "Wins");
				System.out.println("--------");

				while (resultSet.next()) {
					System.out.printf("%-12d%n", resultSet.getInt("wins"));
				}
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

			System.out.printf("%-15s %-30s %-12s%n", "Driver ID", "Name", "Active Years");
			System.out.println("---------------------------------------------------");

			while (resultSet.next()) {
				System.out.printf(
						"%-15d %-30s %-12d%n",
						resultSet.getInt("driverId"),
						resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
						resultSet.getInt("activeYears"));
			}

			System.out.println();

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}
	}

	// iterate through each record, parse into a csv
	// concatenate everything on @
	// split string on @
	public String[] allConstructors() {
		String sql = "select * from constructors;";
		String[] records = null;

		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			StringBuilder data = new StringBuilder();

			data.append("Constructor ID,Name,Nationality@");

			while (resultSet.next()) {
				data.append(resultSet.getInt("constructorId") + "," + resultSet.getString("name") + ","
						+ resultSet.getString("nationality") + "@");
			}
			data.deleteCharAt(data.length() - 1);
			records = data.toString().split("@");

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}

		return records;
	}

	public String[] allRaces() {
		String sql = "select * from races;";
		String[] records = null;
		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);
			StringBuilder data = new StringBuilder();

			data.append("Race ID,Round,Date,Name,Circuit ID, Season (year)@");

			while (resultSet.next()) {
				data.append(resultSet.getInt("raceId") + "," + resultSet.getInt("round") + ","
						+ resultSet.getDate("date") + "," + resultSet.getString("name") + ","
						+ resultSet.getInt("circuitId") + "," + resultSet.getInt("year") + "@");
			}

			data.deleteCharAt(data.length() - 1);
			records = data.toString().split("@");

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}

		return records;
	}

	public String[] allCircuits() {
		String sql = "select * from circuits;";

		String[] records = null;
		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			StringBuilder data = new StringBuilder();

			data.append("Circuit ID,Name,Location,Country,Latitude,Longitude,Altitude@");

			while (resultSet.next()) {
				data.append(resultSet.getInt("circuitId") + "," + resultSet.getString("name") + ","
						+ resultSet.getString("location") + "," + resultSet.getString("country") + ","
						+ resultSet.getFloat("lat") + "," + resultSet.getFloat("lng") + "," + resultSet.getFloat("alt")
						+ "@");
			}
			data.deleteCharAt(data.length() - 1);
			records = data.toString().split("@");

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}

		return records;
	}

	public String[] allDrivers() {
		String sql = "select * from drivers;";
		String[] records = null;
		try {

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);

			StringBuilder data = new StringBuilder();
			data.append("Driver ID,Name,DOB,Nationality@");

			while (resultSet.next()) {
				data.append(resultSet.getInt("driverId") + "," + resultSet.getString("firstname") + " "
						+ resultSet.getString("lastname") + "," + resultSet.getDate("dob") + ","
						+ resultSet.getString("nationality") + "," + "@");
			}
			data.deleteCharAt(data.length() - 1);
			records = data.toString().split("@");

		} catch (SQLException e) {
			System.out.println("Something went wrong...");
			e.printStackTrace();
		}

		return records;
	}

	// delete everything on the server and repopulate
	// testing this is the most frightening thing ever
	public void resetServer() {
		String makeRelative = "../../csvs-sql-data/data-sql/";
		Scanner sc = null;
		File[] files = { new File(makeRelative + "drop-tables.sql"), new File(makeRelative + "seasons.sql"),
				new File(makeRelative + "locale.sql"),
				new File(makeRelative + "circuits.sql"), new File(makeRelative + "drivers.sql"),
				new File(makeRelative + "constructors.sql"), new File(makeRelative + "races.sql"),
				new File(makeRelative + "laps.sql"), new File(makeRelative + "partOf.sql"),
				new File(makeRelative + "driverRaces.sql"), new File(makeRelative + "driverChampionships.sql"),
				new File(makeRelative + "constructorRaces.sql"),
				new File(makeRelative + "constructorChampionships.sql") };

		for (File file : files) {

			try {
				sc = new Scanner(file);
				while (sc.hasNextLine()) {
					String sql = sc.nextLine().trim();
					System.out.println(sql);
					connection.createStatement().execute(sql);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
