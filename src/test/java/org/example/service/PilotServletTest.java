package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bl.Entity;
import org.example.bl.GovernmentEntity;
import org.example.bl.PilotEntity;
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

class PilotServletTest {

  @InjectMocks
  private PilotServlet pilotServlet;

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
    List<Entity> pilots = new ArrayList<>();
    PilotEntity pilotEntity = new PilotEntity(
        "TestName",
        "M",
        "TestNationally",
        ""
    );
    pilots.add(pilotEntity);

    when(databaseWorker.readPilotsFromDB()).thenReturn(pilots);

    pilotServlet.doGet(request, response);

    verify(response).getWriter();

    assertNotEquals(null, pilots);
    Assertions.assertFalse(pilots.isEmpty());
    Assertions.assertNotNull(pilots.get(0));

    List<String> data = pilots.get(0).unwrap();

    assertEquals("TestName", data.get(0));
    assertEquals("M", data.get(1));
    assertEquals("TestNationally", data.get(2));
    assertEquals("", data.get(3));
  }

  @Test
  void doGetEmptyDBTest() throws Exception {

    List<Entity> pilots = new ArrayList<>();

    when(databaseWorker.readPilotsFromDB()).thenReturn(pilots);

    pilotServlet.doGet(request, response);

    verify(response).getWriter();

    assertNotEquals(null, pilots);
    assertTrue(pilots.isEmpty());
  }

  @Test
  void doPostTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestName");
    when(request.getParameter("gender")).thenReturn("M");
    when(request.getParameter("nationality")).thenReturn("TestNat");
    when(request.getParameter("govId")).thenReturn("");

    pilotServlet.doPost(request, response);

    verify(databaseWorker).writePilotToTable(any(Entity.class));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void doPostEmptyTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestName");
    when(request.getParameter("gender")).thenReturn("M");
    when(request.getParameter("govId")).thenReturn("");

    pilotServlet.doPost(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPostNotFullTest() throws Exception {
    pilotServlet.doPost(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPutTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestName");
    when(request.getParameter("gender")).thenReturn("M");
    when(request.getParameter("nationality")).thenReturn("TestNat");
    when(request.getParameter("id")).thenReturn("1");

    pilotServlet.doPut(request, response);

    verify(databaseWorker).updatePilotsTable(any(Entity.class), eq("1"));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void doPutNotFullTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestName");
    when(request.getParameter("acronym")).thenReturn("TN");
    when(request.getParameter("capital")).thenReturn(null);
    when(request.getParameter("id")).thenReturn("1");

    pilotServlet.doPut(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPutEmptyTest() throws Exception {

    pilotServlet.doPut(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doDeleteTest() throws Exception {
    when(request.getParameter("id")).thenReturn("1");

    pilotServlet.doDelete(request, response);

    verify(databaseWorker).deleteFromPilotsTable("id = 1");
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }
}