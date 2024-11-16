package org.example.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Class for work with SQL (Postgres) database.
 */
public class DatabaseManager {

  private final Connection connection;

  public DatabaseManager(AuthObject auth) throws SQLException {
    connection = DriverManager.getConnection(
        auth.getUrl(),
        auth.getUsername(),
        auth.getPassword()
    );
  }

}
