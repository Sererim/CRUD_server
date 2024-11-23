package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.example.db.AuthObject;
import org.example.timbering.Timberland;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;


/**
 * Utility class for handling db config file.
 */
final public class DatabaseAuth {

  private static final String TAG = "DatabaseAuth";
  private static final String FILE = "database_config.json";

  /**
   * Method for converting json file to AuthObject
   * that will be used in connection to the database
   *
   * @param file  path to configuration file
   * @return      AuthObject that is used by JDBC to handle connection to the database.
   */
  public static AuthObject readAuthFile() {
    Gson gson = new Gson();
    AuthObject authObject = null;
    try(InputStream inputStream = DatabaseAuth.class.getClassLoader().getResourceAsStream(FILE)) {
      assert inputStream != null;
      Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8) ;
      JsonReader jsonReader = new JsonReader(reader);
      authObject = gson.fromJson(jsonReader, AuthObject.class);
    } catch (JsonSyntaxException | JsonIOException | IOException | AssertionError exception) {
      Timberland.cutDEAD(TAG, "Caused by\n" + exception, "Further work is impossible!");
    }
    return authObject;
  }
}
