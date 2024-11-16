package org.example.Timbering;

import java.io.IOException;
import java.util.Date;
import java.util.logging.*;


/**
 * Simple wrapper class for logging.
 */
public final class Timberland {

  private static final Logger timber          = Logger.getLogger(Timberland.class.getName());
  private static final Date date              = new Date();

  /**
   * Method to set up timber.
   * @param logToFile - pass true to allow logging to file.
   * @throws IOException - if file can't be created or written to.
   * @throws SecurityException - if program level of precession doesn't allow file creation or logging.
   */
  public static void setTimber(Boolean logToFile) throws IOException, SecurityException {
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new ColorFormatter());
    timber.addHandler(consoleHandler);

    if (logToFile) {
      FileHandler logFile = new FileHandler("audit.txt", true);
      logFile.setFormatter(new SimpleFormatter());
      timber.addHandler(logFile);
    }
    timber.setLevel(Level.ALL);
    timber.setUseParentHandlers(false);
  }

  /**
   * Method for info level logging
   *
   * @param tag      that is used to identify the class
   * @param message  to be shown in commandline
   */
  public static void cutInfo(String tag, String message) {
    timber.log(Level.INFO, getMessage(tag, message));
  }

  /**
   * Method for exception level logging.
   * @param tag      that is used to identify the class
   * @param message  to be shown in commandline
   */
  public static void cutException(String tag, String message, Exception exception) {
    timber.log(Level.WARNING, getMessage(tag, message), exception);
  }

  /**
   * Method for I AM DEAD state
   * @param tag      that is used to identify the class
   * @param message  to be shown in commandline
   * @param reason   state of the program before <strong>DYING</strong>.
   */
  public static void cutDEAD(String tag, String message, String reason) {
    timber.log(Level.SEVERE, getMessage(tag, message) + "\n\n" + reason);
    // WE ARE DEAD
    throw new RuntimeException();
  }

  private static String getMessage(String tag, String message) {
    return String.join("\n", tag, date.toString(), message);
  }
}
