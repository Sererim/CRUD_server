package org.example.Timbering;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Color formatter for Timberland
 * @see Timberland
 */
public class ColorFormatter extends Formatter {

  // Reset
  private static final String RESET = "\u001B[0m";

  // Regular Colors
  private static final String RED = "\u001B[31m";
  private static final String GREEN = "\u001B[32m";
  private static final String YELLOW = "\u001B[33m";

  @Override
  public String format(LogRecord record) {
    String color;

    if (record.getLevel() == Level.SEVERE) {
      color = RED;
    } else if (record.getLevel() == Level.WARNING) {
      color = GREEN;
    } else {
      color = YELLOW;
    }
    return color + formatMessage(record) + RESET + "\n";
  }
}
