package org.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.utils.ServletUtils;
import org.example.utils.DTOtoEntity;
import org.example.bl.Entity;
import org.example.bl.MachineEntity;
import org.example.db.DatabaseWorker;
import org.example.service.dto.MachineDTO;
import org.example.utils.DatabaseAuth;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for
 */
@WebServlet (name = "MachineController", value = "/machine")
public class MachineController extends HttpServlet {

  private static final String TAG = "MachineController";

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
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<Entity> machines;
    try {
      machines = databaseWorker.readMachinesFromDB();
    } catch (SQLException sqle) {
      ServletUtils.errorInternalProblem(resp, TAG);
      return;
    }

    List<MachineDTO> dtoList = machines.stream()
        .map(machina -> {
          MachineEntity machineEntity = (MachineEntity) machina;
          return new MachineDTO(
              machineEntity.getName(),
              machineEntity.getSeries(),
              machineEntity.getHeadHeight(),
              machineEntity.getMaxWeight(),
              machineEntity.getPowerSource(),
              machineEntity.getPowerOutput(),
              machineEntity.getMaxAcceleration()
          );
        }).toList();
    resp.getWriter().write(dtoList.toString());
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String name = req.getParameter("name");
    String series = req.getParameter("series");
    String headHeight = req.getParameter("headHeight");
    String maxWeight = req.getParameter("maxWeight");
    String powerSource = req.getParameter("powerSource");
    String powerOutput = req.getParameter("powerOutput");
    String maxAcceleration = req.getParameter("maxAcceleration");

    if (name == null || series == null || headHeight == null || maxWeight == null ||
        powerOutput == null || powerSource == null || maxAcceleration == null) {
      ServletUtils.errorBadRequest(resp, "Provide all information!", TAG);
      return;
    }

    MachineDTO machina = new MachineDTO(
        name,
        series,
        headHeight,
        maxWeight,
        powerSource,
        powerOutput,
        maxAcceleration
    );

    try {
      databaseWorker.writeToMachineTable(DTOtoEntity.convert(machina, "machine"));
      ServletUtils.okResponse(resp, TAG);
    } catch (SQLException sqlException) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }

  @Override
  public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String series = req.getParameter("series");
    String headHeight = req.getParameter("headHeight");
    String maxWeight = req.getParameter("maxWeight");
    String powerSource = req.getParameter("powerSource");
    String powerOutput = req.getParameter("powerOutput");
    String maxAcceleration = req.getParameter("maxAcceleration");
    String id = req.getParameter("id");

    if (name == null || series == null || headHeight == null || maxWeight == null ||
        powerOutput == null || powerSource == null || maxAcceleration == null || id == null) {
      ServletUtils.errorBadRequest(resp, "Provide all information!", TAG);
      return;
    }

    MachineDTO machina = new MachineDTO(
        name,
        series,
        headHeight,
        maxWeight,
        powerSource,
        powerOutput,
        maxAcceleration
    );

    try {
      databaseWorker.updateMachinesTable(DTOtoEntity.convert(machina, "machine"), id);
      ServletUtils.okResponse(resp, TAG);
    } catch (SQLException sqlException) {
      ServletUtils.errorBadRequest(resp, "Provide full information!", TAG);
    }

  }

  @Override
  public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String id = req.getParameter("id");

    if (id == null) {
      ServletUtils.errorBadRequest(resp, "No id was provided!", TAG);
    }
    try {
      String result = databaseWorker.deleteFromGovernmentsTable("id = " + id);
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
    } catch (SQLException sqlException) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }
}
