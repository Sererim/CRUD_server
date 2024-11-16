package org.example.DB;

import org.example.Timbering.Timberland;

import java.sql.*;


/**
 * Class for work with SQL (Postgres) database.
 */
public class DatabaseManager {

  private static final String TAG = "DatabaseManager";

  private final Connection connection;

  public DatabaseManager(AuthObject auth) throws SQLException {
    connection = DriverManager.getConnection(auth.getUrl(), auth.getUsername(), auth.getPassword());
  }

  /**
   * Method that creates the required tables in the database.
   */
  private void setUpTables() {
    // TODO finish this method.
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
   * @param table correct table name
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

  //TODO add C U and D methods for your database.

}
