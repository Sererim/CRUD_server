package org.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.utils.ServletUtils;
import org.example.utils.DTOtoEntity;
import org.example.bl.Entity;
import org.example.bl.PilotEntity;
import org.example.db.DatabaseWorker;
import org.example.service.PilotDTO;
import org.example.utils.DatabaseAuth;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for Pilot
 */
@WebServlet (name = "PilotController", value = "/pilot")
public class PilotController extends HttpServlet {

  private static final String TAG = "PilotController";
  private DatabaseWorker databaseWorker;

  @Override
  public void init() throws ServletException {
    super.init();
    try {
      Class.forName("org.postgresql.Driver");
      databaseWorker = new DatabaseWorker(DatabaseAuth.readAuthFile());
    } catch (Exception sqle) {
      throw new ServletException(sqle);
    }
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    List<Entity> pilots;

    try {
      pilots = databaseWorker.readPilotsFromDB();
    } catch (SQLException sqle) {
      ServletUtils.errorInternalProblem(resp, TAG);
      return;
    }

    List<PilotDTO> dtoList = pilots.stream()
        .map(driver -> {
          PilotEntity pilotEntity = (PilotEntity) driver;
          return new PilotDTO(
              pilotEntity.getName(),
              pilotEntity.getGender(),
              pilotEntity.getNationality(),
              pilotEntity.getGovId()
          );
        }).toList();
    resp.getWriter().write(dtoList.toString());
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String gender = req.getParameter("gender");
    String nationality = req.getParameter("nationality");

    if (name == null || gender == null || nationality == null) {
      ServletUtils.errorBadRequest(resp, "Provide full information!", TAG);
      return;
    }

    PilotDTO pilot = new PilotDTO(name, gender, nationality, "");

    try {
      databaseWorker.writePilotToTable(DTOtoEntity.convert(pilot, "pilot"));
      ServletUtils.okResponse(resp, TAG);
    } catch (SQLException sqle) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }

  @Override
  public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String name = req.getParameter("name");
    String sex = req.getParameter("gender");
    String nationality = req.getParameter("nationality");
    String id = req.getParameter("id");

    if (name == null || sex == null || nationality == null || id == null) {
      ServletUtils.errorBadRequest(resp, "Provide full information!", TAG);
      return;
    }

    PilotDTO pilot = new PilotDTO(name, sex, nationality, id);

    try {
      databaseWorker.updatePilotsTable(DTOtoEntity.convert(pilot, "pilot"), id);
      ServletUtils.okResponse(resp, TAG);
    } catch (SQLException sqle) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }

  @Override
  public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String id = req.getParameter("id");

    try {
      String result = databaseWorker.deleteFromPilotsTable("id = " + id);
      if (result == null) {
        resp.getWriter().write("Empty result!");
        ServletUtils.okResponse(resp, TAG);
      } else if (result.isEmpty()) {
        resp.getWriter().write("No rows were affected!");
        ServletUtils.okResponse(resp, TAG);
      } else {
        resp.getWriter().write("Rows were affected:\n" + result);
        ServletUtils.okResponse(resp, TAG);
      }
    } catch (SQLException sqle) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }
}
