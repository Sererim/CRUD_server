package org.example.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.example.DB.AuthObject;
import org.example.Timbering.Timberland;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;


/**
 * Utility class for handling db config file.
 */
final public class DatabaseAuth {

  private static final String TAG = "DatabaseAuth";

  /**
   * Method for converting json file to AuthObject
   * that will be used in connection to the database
   *
   * @param file  path to configuration file
   * @return      AuthObject that is used by JDBC to handle connection to the database.
   */
  public static AuthObject readAuthFile(Path file) {
    Gson gson = new Gson();
    AuthObject optionalAuthObject = null;
    try (Reader reader = new FileReader(file.toString(), StandardCharsets.UTF_8)) {
      JsonReader jsonReader = new JsonReader(reader);
      optionalAuthObject = gson.fromJson(jsonReader, AuthObject.class);
    } catch (JsonIOException | IOException ioex) {
      Timberland.cutException(TAG, "Caused by reading database config file.", ioex);
    } catch (JsonSyntaxException jse) {
      Timberland.cutException(TAG, "Caused by incorrect json schema.", jse);
    }
    return optionalAuthObject;
  }
}
