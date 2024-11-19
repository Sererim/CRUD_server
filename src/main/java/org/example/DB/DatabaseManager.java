package org.example.db;

import org.example.timbering.Timberland;

import java.sql.*;
import java.util.List;


public class DatabaseManager {

  private static final String TAG = "DatabaseManager";

  private final Connection connection;

  public DatabaseManager(AuthObject auth) throws SQLException {
    connection = DriverManager.getConnection(auth.getUrl(), auth.getUsername(), auth.getPassword());
    setUpTables();
  }

  /**
   * Method that creates the required tables in the database.
   */
  private void setUpTables() {
    Timberland.cutInfo(TAG, "Setting up tables...");
    setUpGovernmentTable();
    setUpEnumForPilot();
    setUpPilotTable();
    setUpMachineTable();
    setUpPilotToMachineTable();
  }

  /**
   * Method to set up a user table in the database.
   */
  private void setUpPilotTable() {

    Timberland.cutInfo(TAG, "Setting up a pilot table.");

    String statement = "CREATE TABLE IF NOT EXISTS pilots (" +
        "id SERIAL PRIMARY KEY," +
        "name VARCHAR(75)," +
        "sex gender," +
        "nationality VARCHAR(50)," +
        "government_id INTEGER," +
        "FOREIGN KEY (government_id) REFERENCES governments(id)" +
        ")";

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(statement);
      if (!preparedStatement.execute()) {
        Timberland.cutInfo(TAG, "Successfully created the user table");
      }
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Couldn't process the statement\n" + statement, sqle);
    }
  }

  /**
   * Pilot needs a single enum for gender.
   */
  private void setUpEnumForPilot() {
    Timberland.cutInfo(TAG, "Setting up ENUMS for pilots");

    try {
      connection.prepareStatement("CREATE TYPE gender AS ENUM ('M', 'F');").execute();
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Couldn't process the statement or Enum already exists", sqle);
    }
  }

  /**
   * Set up governments table.
   */
  private void setUpGovernmentTable() {
    Timberland.cutInfo(TAG, "Setting up a governments table.");

    String statement = "CREATE TABLE IF NOT EXISTS governments (" +
        "id SERIAL PRIMARY KEY," +
        "name VARCHAR(50)," +
        "acronym VARCHAR(10)," +
        "capital VARCHAR(20)" +
        ");";

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(statement);
      if (!preparedStatement.execute()) {
        Timberland.cutInfo(TAG, "Successfully created the governments table");
      }
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Couldn't process the statement\n" + statement, sqle);
    }
  }

  private void setUpMachineTable() {
    Timberland.cutInfo(TAG, "Setting up a machines table.");

    String statement = "CREATE TABLE IF NOT EXISTS machines (" +
        "id SERIAL PRIMARY KEY," +
        "name VARCHAR(50)," +
        "series VARCHAR(20)," +
        "head_height NUMERIC(4, 2)," +
        "max_weight NUMERIC(4, 2)," +
        "empty_weight NUMERIC(4, 2)," +
        "power_source TEXT," +
        "power_output SMALLINT," +
        "max_acceleration NUMERIC(4,2)," +
        "max_speed INTEGER," +
        "government_id INTEGER," +
        "FOREIGN KEY (government_id) REFERENCES governments(id)" +
        ")";

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(statement);
      if (!preparedStatement.execute()) {
        Timberland.cutInfo(TAG, "Successfully created the machines table");
      }
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Couldn't process the statement\n" + statement, sqle);
    }
  }

  private void setUpPilotToMachineTable() {
    Timberland.cutInfo(TAG, "Setting up a machines_pilots table.");

    String statement = "CREATE TABLE IF NOT EXISTS machines_pilots (" +
        "machine_id INTEGER NOT NULL," +
        "pilot_id INTEGER NOT NULL," +
        "PRIMARY KEY (machine_id, pilot_id)," +
        "FOREIGN KEY (machine_id) REFERENCES machines(id)," +
        "FOREIGN KEY (pilot_id) REFERENCES pilots(id)" +
        ");";

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(statement);
      if (!preparedStatement.execute()) {
        Timberland.cutInfo(TAG, "Successfully created the machines_pilots table");
      }
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Couldn't process the statement\n" + statement, sqle);
    }
  }


  /**
   * Read All columns from the table.<br></br>
   * <strong>May be dangerous to use if table has many rows.</strong>
   *
   * @param table correct table name
   * @return ResultSet <strong>may be null</strong>
   */
  ResultSet readAllFrom(String table) {
    Timberland.cutInfo(TAG, "Reading everything from " + table);
    String statement = "SELECT * FROM " + table + ";";
    ResultSet result = null;

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(statement);

      Timberland.cutInfo(TAG, "Executing the statement " + statement);
      result = preparedStatement.executeQuery();

      Timberland.cutInfo(TAG, "Got a ResultSet\n" + result);

    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Failed to read from " + table, sqle);
    }
    return result;
  }

  /**
   * Read from the table with a where clause.
   *
   * @param table  correct table name
   * @param clause valid WHERE clause
   *               Example "id = 1"
   * @return ResultSet may be null
   */
  ResultSet readFromWhere(String table, String clause) {
    Timberland.cutInfo(TAG, "Reading from " + table + " where " + clause);
    String statement = "SELECT * FROM " + table + " WHERE " + clause;
    ResultSet result = null;

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(statement);

      Timberland.cutInfo(TAG, "Executing the statement " + statement);

      Timberland.cutInfo(TAG, "Executing the statement " + statement);
      result = preparedStatement.executeQuery();

      Timberland.cutInfo(TAG, "Got a ResultSet\n" + result);

    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Failed to read from " + table, sqle);
    }
    return result;
  }

  void writeToTable(String table, Entity entity) {
    List<String> data = entity.getDataToDatabase();
  }


}
