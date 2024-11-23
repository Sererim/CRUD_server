package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bl.Entity;
import org.example.bl.GovernmentEntity;
import org.example.bl.MachineEntity;
import org.example.db.DatabaseWorker;
import org.example.service.MachineServlet;
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

class MachineServletTest {

  @InjectMocks
  private MachineServlet machineServlet;

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
    List<Entity> machines = new ArrayList<>();
    MachineEntity machineEntity = new MachineEntity(
        "TEST_name",
        "TEST_series",
        "TEST_head",
        "TEST_weight",
        "TEST_source",
        "TEST_output",
        "TEST_max",
        ""
    );
    machines.add(machineEntity);

    when(databaseWorker.readGovernmentsFromDB()).thenReturn(machines);

    machineServlet.doGet(request, response);

    verify(response).getWriter();

    assertNotEquals(null, machines);
    Assertions.assertFalse(machines.isEmpty());
    Assertions.assertNotNull(machines.get(0));

    List<String> data = machines.get(0).unwrap();

    assertEquals("TEST_name", data.get(0));
    assertEquals("TEST_series", data.get(1));
    assertEquals("TEST_head", data.get(2));
    assertEquals("TEST_weight", data.get(3));
    assertEquals("TEST_source", data.get(4));
    assertEquals("TEST_output", data.get(5));
    assertEquals("TEST_max", data.get(6));


  }

  @Test
  void doGetEmptyDBTest() throws Exception {

    List<Entity> machines = new ArrayList<>();

    when(databaseWorker.readGovernmentsFromDB()).thenReturn(machines);

    machineServlet.doGet(request, response);

    verify(response).getWriter();

    assertNotEquals(null, machines);
    Assertions.assertTrue(machines.isEmpty());
  }

  @Test
  void doPostTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestMachine");
    when(request.getParameter("series")).thenReturn("TM123");
    when(request.getParameter("headHeight")).thenReturn("10");
    when(request.getParameter("maxWeight")).thenReturn("1000");
    when(request.getParameter("powerSource")).thenReturn("Electric");
    when(request.getParameter("powerOutput")).thenReturn("500");
    when(request.getParameter("maxAcceleration")).thenReturn("10");

    machineServlet.doPost(request, response);

    verify(databaseWorker).writeToMachineTable(any(Entity.class));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void doPostEmptyTest() throws Exception {
    when(request.getParameter("name")).thenReturn(null);
    when(request.getParameter("series")).thenReturn(null);
    when(request.getParameter("headHeight")).thenReturn(null);
    when(request.getParameter("maxWeight")).thenReturn(null);
    when(request.getParameter("powerSource")).thenReturn(null);
    when(request.getParameter("powerOutput")).thenReturn(null);
    when(request.getParameter("maxAcceleration")).thenReturn(null);

    machineServlet.doPost(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPostNotFullTest() throws Exception {
    when(request.getParameter("name")).thenReturn("testing");
    when(request.getParameter("series")).thenReturn(null);
    when(request.getParameter("headHeight")).thenReturn(null);

    machineServlet.doPost(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPutTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestMachine");
    when(request.getParameter("series")).thenReturn("TM123");
    when(request.getParameter("headHeight")).thenReturn("10");
    when(request.getParameter("maxWeight")).thenReturn("1000");
    when(request.getParameter("powerSource")).thenReturn("Electric");
    when(request.getParameter("powerOutput")).thenReturn("500");
    when(request.getParameter("maxAcceleration")).thenReturn("10");
    when(request.getParameter("id")).thenReturn("1");

    machineServlet.doPut(request, response);

    verify(databaseWorker).updateMachinesTable(any(Entity.class), eq("1"));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  void doPutNotFullTest() throws Exception {
    when(request.getParameter("name")).thenReturn("TestMachine");
    when(request.getParameter("id")).thenReturn("1");

    machineServlet.doPut(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doPutEmptyTest() throws Exception {
    machineServlet.doPut(request, response);

    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCaptor.capture());

    assertEquals(HttpServletResponse.SC_BAD_REQUEST, statusCaptor.getValue().intValue());
  }

  @Test
  void doDeleteTest() throws Exception {
    when(request.getParameter("id")).thenReturn("1");

    machineServlet.doDelete(request, response);

    assertNull(databaseWorker.deleteFromMachinesTable("id = 1"));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }
}