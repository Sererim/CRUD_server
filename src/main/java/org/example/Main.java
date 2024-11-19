package org.example;


import com.sun.net.httpserver.HttpServer;
import org.example.db.AuthObject;
import org.example.db.DatabaseManager;
import org.example.timbering.Timberland;
import org.example.utils.DatabaseAuth;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.sql.SQLException;

public class Main {

  private static final String TAG = "Main";

  public static void main(String... args) {

    String credentialsFilename = "";
    boolean logToFile = true;

    // Default mode for server.
    if (args.length == 0) {
      credentialsFilename = "database_config.json";
    } else {
      if (args[0].equals("-help")) {
        System.out.println(String.join("\n",
            "Simple server.",
            "First argument is file name for credentials .json file for database that is used by the server",
            "Second argument is for logging, pass in Y/y if you want the server to write it logs to a file."
        ));
        // Early return.
        return;
      } else {
        switch (args.length) {
          // We got only a filename
          case 1:
            credentialsFilename = args[0];
            break;
          case 2:
            credentialsFilename = args[0];
            logToFile = args[1].equalsIgnoreCase("y");
        }
      }
    }

    // Set up the server.

    // Set up logger.
    try {
      Timberland.setTimber(logToFile);
    } catch (IOException e) {
      throw new RuntimeException("Could not set up Timberland fpr logging!\n" + e);
    }

    // Get database auth file
    AuthObject auth = DatabaseAuth.readAuthFile(Path.of(credentialsFilename));
    if (auth == null) {
      Timberland.cutDEAD(
          TAG,
          "Could not create AuthObject for connecting to a database!\n" +
              "Connection to a database is required to run the program. Not allowed program state",
          "Either file name is incorrect or json schema is wrong."
      );
      // Early return.
      return;
    }

    Timberland.cutInfo(TAG, "Successfully loaded the config file for the database." +
        "\nEstablishing the connection...");

    // Connect to the database.
    DatabaseManager databaseManager = null;
    try {
      databaseManager = new DatabaseManager(auth);
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Couldn't connect to the database!", sqle);
      // Early return.
      return;
    }

    // databaseManager != null !
    // Start the server.
    HttpServer server = null;
    try {
      server = HttpServer.create(new InetSocketAddress(8085), 0);
    } catch (IOException ioe) {
      Timberland.cutException(TAG, "Couldn't initialise an http server!", ioe);
    }
    if (server == null) {
      Timberland.cutDEAD(
          TAG,
          "Problems with starting a server!",
          "It is possible that program doesn't have a permission or port is already being used"
      );
      return;
    }

    server.createContext("/pilot");
    server.createContext("/machine");
    server.createContext("/government");
    server.createContext("/machine_to_pilot");

    System.out.println("DONE!");
  }
}