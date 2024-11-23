package org.example.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.utilities.ServletUtils;
import org.example.timbering.Timberland;
import org.example.utils.DTOtoEntity;
import org.example.bl.Entity;
import org.example.bl.GovernmentEntity;
import org.example.db.DatabaseWorker;
import org.example.service.dto.GovernmentDTO;
import org.example.utils.DatabaseAuth;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for Government
 */
@WebServlet (name = "GovernmentServlet", urlPatterns = "/government")
public class GovernmentServlet extends HttpServlet {

  private DatabaseWorker databaseWorker;
  private final static String TAG = "GovernmentServlet";

  @Override
  public void init() throws ServletException {
    super.init();
    try {
      databaseWorker = new DatabaseWorker(DatabaseAuth.readAuthFile());
    } catch (SQLException sqle) {
      Timberland.cutDEAD(TAG, "Error on init()", "Couldn't establish a database connection!");
      throw new ServletException("Couldn't start the database" + sqle);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<Entity> governments;
    try {
      governments = databaseWorker.readGovernmentsFromDB();
    } catch (SQLException e) {
      ServletUtils.errorInternalProblem(resp, TAG);
      return;
    }

    List<GovernmentDTO> dtoList = governments.stream()
        .map(gov -> {
          GovernmentEntity governmentEntity = (GovernmentEntity) gov;
          return new GovernmentDTO(
              governmentEntity.getName(),
              governmentEntity.getAcronym(),
              governmentEntity.getCapital()
          );
        }).toList();
    resp.getWriter().write(dtoList.toString());
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String name = req.getParameter("name");
    String acronym = req.getParameter("acronym");
    String capital = req.getParameter("capital");

    if (name == null || acronym == null || capital == null) {
      ServletUtils.errorBadRequest(resp, "Provide all information!", TAG);
      return;
    }

    GovernmentDTO gov = new GovernmentDTO(name, acronym, capital);

    try {
      databaseWorker.writeToGovernmentsTable((DTOtoEntity.convert(gov, "government")));
      ServletUtils.okResponse(resp, TAG);
    } catch (SQLException sqlException) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String acronym = req.getParameter("acronym");
    String capital = req.getParameter("capital");
    String id = req.getParameter("id");

    if (name == null || acronym == null || capital == null || id == null) {
      ServletUtils.errorBadRequest(resp, "Provide full information! Partial update is not supported!", TAG);
      return;
    }

    GovernmentDTO gov = new GovernmentDTO(name, acronym, capital);
    try {
      databaseWorker.updateGovernmentsTable(DTOtoEntity.convert(gov, "government"), id);
      ServletUtils.okResponse(resp, TAG);
    } catch (SQLException sqlException) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String id = req.getParameter("id");
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
    } catch (SQLException sqle) {
      ServletUtils.errorInternalProblem(resp, TAG);
    }
  }
}
