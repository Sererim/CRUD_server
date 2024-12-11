package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bl.Entity;
import org.example.bl.GovernmentEntity;
import org.example.controllers.GovernmentController;
import org.example.db.DatabaseWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GovernmentControllerTest {

  @InjectMocks
  private GovernmentController governmentServlet;

  @Mock
  private DatabaseWorker databaseWorker;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private PrintWriter writer;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    when(response.getWriter()).thenReturn(writer);
  }

  @Test
  void doGetTest() throws Exception {
    List<Entity> governments = new ArrayList<>();
    GovernmentEntity governmentEntity = new GovernmentEntity(
        "TestName",
        "TN",
        "TestCapital"
    );
    governments.add(governmentEntity);

    when(databaseWorker.readGovernmentsFromDB()).thenReturn(governments);

    governmentServlet.doGet(request, response);

    verify(response).getWriter();

    assertNotEquals(null, governments);
    Assertions.assertFalse(governments.isEmpty());
    Assertions.assertNotNull(governments.get(0));

    List<String> data = governments.get(0).unwrap();

    assertEquals("TestName", data.get(0));
    assertEquals("TN", data.get(1));
    assertEquals("TestCapital", data.get(2));

  }

  @Test
  void doGetEmptyDBTest() throws Exception {

    List<Entity> governments = new ArrayList<>();

    when(databaseWorker.readGovernmentsFromDB()).thenReturn(governments);

    governmentServlet.doGet(request, response);

    verify(response).getWriter();

    assertNotEquals(null, governments);
    Assertions.assertTrue(governments.isEmpty());
  }

  @Test
  void doPostTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestName");
    when(request.getParameter("acronym")).thenReturn("TN");
    when(request.getParameter("capital")).thenReturn("TestCapital");

    governmentServlet.doPost(request, response);

    verify(databaseWorker).writeToGovernmentsTable(any(Entity.class));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void doPostEmptyTest() throws Exception {
    when(request.getParameter("name")).thenReturn(null);
    when(request.getParameter("acronym")).thenReturn(null);
    when(request.getParameter("capital")).thenReturn(null);

    governmentServlet.doPost(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPostNotFullTest() throws Exception {
    when(request.getParameter("name")).thenReturn("null");
    when(request.getParameter("acronym")).thenReturn("null");
    when(request.getParameter("capital")).thenReturn(null);

    governmentServlet.doPost(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPutTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestName");
    when(request.getParameter("acronym")).thenReturn("TN");
    when(request.getParameter("capital")).thenReturn("TestCapital");
    when(request.getParameter("id")).thenReturn("1");

    governmentServlet.doPut(request, response);

    verify(databaseWorker).updateGovernmentsTable(any(Entity.class), eq("1"));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void doPutNotFullTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestName");
    when(request.getParameter("acronym")).thenReturn("TN");
    when(request.getParameter("capital")).thenReturn(null);
    when(request.getParameter("id")).thenReturn("1");

    governmentServlet.doPut(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPutEmptyTest() throws Exception {
    when(request.getParameter("name")).thenReturn(null);
    when(request.getParameter("acronym")).thenReturn(null);
    when(request.getParameter("capital")).thenReturn(null);
    when(request.getParameter("id")).thenReturn(null);

    governmentServlet.doPut(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doDeleteTest() throws Exception {
    when(request.getParameter("id")).thenReturn("1");

    governmentServlet.doDelete(request, response);

    verify(databaseWorker).deleteFromGovernmentsTable("id = 1");
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }
}