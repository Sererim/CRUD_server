package org.example.service.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.example.timbering.Timberland;

import java.io.IOException;

public final class ServletUtils {

  public static void errorBadRequest(
      HttpServletResponse response,
      String reason,
      String TAG
  ) throws IOException {
    Timberland.cutException(TAG, "Error! " + reason, new Exception());
    response.getWriter().write("Error! " + reason);
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

  public static void errorInternalProblem(
      HttpServletResponse response,
      String TAG
  ) throws IOException {
    String reason = "Error! Problem with the server!";
    Timberland.cutException(TAG, "Error! " + reason, new Exception());
    response.getWriter().write("Error! " + reason);
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  public static void emptyResponse(
      HttpServletResponse response,
      String reason,
      String TAG
  ) throws IOException {
    Timberland.cutInfo(TAG, "Error! " + reason);
    response.getWriter().write("Error! " + reason);
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  public static void okResponse(
      HttpServletResponse response,
      String TAG
  ) throws IOException {
    Timberland.cutInfo(TAG, "Wrote to the database!");
    response.getWriter().write("Successfully wrote to the database");
    response.setStatus(HttpServletResponse.SC_OK);
  }
}