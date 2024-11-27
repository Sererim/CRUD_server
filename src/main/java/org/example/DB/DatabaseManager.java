package org.example.db;

import org.example.db.dao.DAO;
import org.example.timbering.Timberland;

import java.sql.*;
import java.util.List;


/**
 * Database manager class.
 * <br></br>
 * Used for all interactions with the database (Postgres).
 * <br></br>
 * Direct methods that access the database are package private use -> Database worker for working with db.
 * @see DatabaseWorker
 */
public class DatabaseManager {

  // Tag for Timberland
  private static final String TAG = "DatabaseManager";

  private final Connection connection;

  public DatabaseManager(AuthObject auth) throws SQLException, ClassNotFoundException {
    Class.forName("org.postgresql.Driver");
    connection = DriverManager.getConnection(auth.getUrl(), auth.getUsername(), auth.getPassword());
    setUpTables();
  }

  /**
   * Method that creates the required tables in the database.
   */
  public void setUpTables() throws SQLException {
    Timberland.cutInfo(TAG, "Setting up tables...");
    setUpGovernmentTable();
    setUpPilotTable();
    setUpMachineTable();
    setUpPilotToMachineTable();
  }

  /**
   * Method to set up a user table in the database.
   */
  private void setUpPilotTable() throws SQLException {

    Timberland.cutInfo(TAG, "Setting up a pilot table.");

    String statement = "CREATE TABLE IF NOT EXISTS pilots (" +
        "id SERIAL PRIMARY KEY," +
        "name VARCHAR(75)," +
        "sex VARCHAR(1)," +
        "nationality VARCHAR(50)," +
        "government_id INTEGER," +
        "FOREIGN KEY (government_id) REFERENCES governments(id)" +
        ")";

    exec(statement);
  }


  /**
   * Set up governments table.
   */
  private void setUpGovernmentTable() throws SQLException {
    Timberland.cutInfo(TAG, "Setting up a governments table.");

    String statement = "CREATE TABLE IF NOT EXISTS governments (" +
        "id SERIAL PRIMARY KEY," +
        "name VARCHAR(50)," +
        "acronym VARCHAR(10)," +
        "capital VARCHAR(20)" +
        ");";

    exec(statement);
  }

  private void setUpMachineTable() throws SQLException {
    Timberland.cutInfo(TAG, "Setting up a machines table.");

    String statement = "CREATE TABLE IF NOT EXISTS machines (" +
        "id SERIAL PRIMARY KEY," +
        "name VARCHAR(50)," +
        "series VARCHAR(20)," +
        "head_height NUMERIC(4, 2)," +
        "max_weight NUMERIC(4, 2)," +
        "power_source TEXT," +
        "power_output SMALLINT," +
        "max_acceleration NUMERIC(4,2)," +
        "government_id INTEGER," +
        "FOREIGN KEY (government_id) REFERENCES governments(id)" +
        ")";

    exec(statement);
  }

  private void setUpPilotToMachineTable() throws SQLException {
    Timberland.cutInfo(TAG, "Setting up a machines_pilots table.");

    String statement = "CREATE TABLE IF NOT EXISTS machines_pilots (" +
        "machine_id INTEGER NOT NULL," +
        "pilot_id INTEGER NOT NULL," +
        "PRIMARY KEY (machine_id, pilot_id)," +
        "FOREIGN KEY (machine_id) REFERENCES machines(id)," +
        "FOREIGN KEY (pilot_id) REFERENCES pilots(id)" +
        ");";

    exec(statement);
  }


  /**
   * Read All columns from the table.<br></br>
   * <strong>May be dangerous to use if table has many rows.</strong>
   *
   * @param table correct table name
   * @return ResultSet <strong>may be null</strong>
   */
  public ResultSet readAllFrom(String table) throws SQLException {
    Timberland.cutInfo(TAG, "Reading everything from " + table);
    String statement = "SELECT * FROM " + table + ";";
    return execWithResult(statement);
  }

  /**
   * Read from the table with a where clause.
   *
   * @param table  correct table name
   * @param clause valid WHERE clause
   *               Example "id = 1"
   * @return ResultSet may be null
   */
  ResultSet readFromWhere(String table, String clause) throws SQLException {
    Timberland.cutInfo(TAG, "Reading from " + table + " where " + clause);
    String statement = "SELECT * FROM " + table + " WHERE " + clause;
    return execWithResult(statement);
  }

  /**
   * Write to table function.
   * We can add values without specifying column names, if amount of values is equal to the number of columns,
   * including id
   * @param table valid table name
   * @param dao valid DAO object
   */
  void writeToTable(String table, DAO dao) throws SQLException {
    Timberland.cutInfo(TAG, "Writing this object to database:\n" + dao.toString());
    List<String> data = dao.getDataToDatabase();
    // Last one should be added separately.
    String firstPart = data.stream().limit(data.size() - 1).reduce("", (s, o) -> s + ", " + "'" + o + "'");

    String statement = "INSERT INTO " + table + " VALUES (" +
        " DEFAULT " + // id
        firstPart +
        " ,'" + data.get(data.size() - 1)  +
        "');";
    exec(statement);
  }

  void writeToMachinesToPilotsTable(DAO dao, String table) throws SQLException {
    List<String> data = dao.getDataToDatabase();
    String statement = "INSERT INTO " + table + " VALUES (" +
        data.get(0) + ", " + data.get(1) + ");";
    exec(statement);
  }

  /**
   * Method to update data in a table
   * @param table valid table name
   * @param dao valid dao object
   * @param id correct id
   * @param isPilot if table to update is machines_pilots provide isPilot to say what id is being used for WHERE
   */
  void updateTable(String table, DAO dao, String id, Boolean isPilot) throws SQLException {
    Timberland.cutInfo(TAG, "Updating table " + table + " with\n" + dao.getDataToDatabase());
    String statement = "UPDATE " + table + "\n" + "SET" + "\n";
    switch (table) {
      case "governments" -> updateGovernmentsTable(statement, dao, id);
      case "pilots" -> updatePilotsTable(statement, dao, id);
      case "machines" -> updateMachinesTable(statement, dao, id);
      case "machines_pilots" -> {
        if (isPilot) {
          updateMachinesToPilotTableOnPilot(statement, dao, id);
        } else {
          updateMachinesToPilotTableOnMachine(statement, dao, id);
        }
      }
    }
  }

  void updateGovernmentsTable(String statement, DAO dao, String id) throws SQLException {
    List<String> data = dao.getDataToDatabase();
    String addition = "name = '" + data.get(0) + "',\n" +
        "acronym = '"            + data.get(1) + "',\n" +
        "capital = '"            + data.get(2) + "'\n" +
        "WHERE id = " + id + ";";

    exec(
        statement + addition
    );
  }

  void updatePilotsTable(String statement, DAO dao, String id) throws SQLException {
    List<String> data = dao.getDataToDatabase();
    String addition = "name = '" + data.get(0) + "',\n" +
        "sex = '"                + data.get(1) + "',\n" +
        "nationality = '"        + data.get(2) + "',\n" +
        "government_id = "      + data.get(3) + "\n" +
        "WHERE id = " + id + ";";

    exec(
        statement + addition
    );
  }

  void updateMachinesTable(String statement, DAO dao, String id) throws SQLException {
    List<String> data = dao.getDataToDatabase();
    String addition = "name = '"     + data.get(0) + "',\n" +
        "series = '"                 + data.get(1) + "',\n" +
        "head_height = "            + data.get(2) + ",\n" +
        "max_weight = "             + data.get(3) + ",\n" +
        "power_source = '"           + data.get(4) + "',\n" +
        "power_output = "           + data.get(5) + ",\n" +
        "max_acceleration = "       + data.get(6) + ",\n" +
        "government_id = "          + data.get(7) + "\n" +
        "WHERE id = " + id + ";";

    exec(
        statement + addition
    );
  }

  void updateMachinesToPilotTableOnMachine(String statement, DAO dao, String id) throws SQLException {
    List<String> data = dao.getDataToDatabase();
    String addition = "machine_id = " + data.get(0) + ",\n" +
        "pilot_id = "                 + data.get(1) + "\n" +
        "WHERE machine_id = " + id + ";";

    exec(
        statement + addition
    );
  }

  void updateMachinesToPilotTableOnPilot(String statement, DAO dao, String id) throws SQLException {
    List<String> data = dao.getDataToDatabase();
    String addition = "machine_id = " + data.get(0) + ",\n" +
        "pilot_id = "                 + data.get(1) + "\n" +
        "WHERE pilot_id = " + id + ";";

    exec(
        statement + addition
    );
  }

  /**
   * Method to delete row or rows from a table
   * @param table valid table name
   * @param clause correct clause
   * @return a result set
   */
  ResultSet deleteFromTable(String table, String clause) throws SQLException {
    String statement = "DELETE FROM " + table + "\n" +
        "WHERE " + clause + "\n" +
        "RETURNING *;";
    return execWithResult(statement);
  }


  /**
   * Method to execute a given statement, that produce a result (Example: SELECT).
   * @param statement valid sql statement
   * @return result set
   */
  ResultSet execWithResult(String statement) throws SQLException {
    ResultSet resultSet = null;
    resultSet = connection.prepareStatement(statement).executeQuery();
    connection.prepareStatement(statement).close();

    if (resultSet == null)
      Timberland.cutInfo(TAG, "Empty result set! Statement:\n" + statement);

    return resultSet;
  }

  /**
   * Method to execute a given statement, that doesn't produce a result (Example INSERT INTO)
   * @param statement valid sql statement
   */
  void exec(String statement) throws SQLException {
    connection.prepareStatement(statement).execute();
    connection.prepareStatement(statement).close();
  }
}
