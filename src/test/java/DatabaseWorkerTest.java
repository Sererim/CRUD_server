import org.example.bl.*;
import org.example.db.AuthObject;
import org.example.db.DatabaseWorker;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseWorkerTest {

  public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
      "postgres:16-alpine"
  );

  private static DatabaseWorker databaseWorker;

  @BeforeAll
  static void beforeALl() throws SQLException, ClassNotFoundException {
    postgresContainer.start();
    AuthObject auth = new AuthObject(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
    databaseWorker = new DatabaseWorker(auth);
    syntheticData();
  }

  @AfterAll
  static void afterAll() {
    postgresContainer.stop();
  }

  /**
   * Generate synthetic data for tests.
   */
  static void syntheticData() {
    assertDoesNotThrow(() -> {
      databaseWorker.writeToGovernmentsTable(
          new GovernmentEntity(
              "Zeon",
              "Z",
              "Side 3"
          )
      );
      databaseWorker.writePilotToTable(
          new PilotEntity(
              "Char",
              "M",
              "Zeon",
              "1"
          )
      );
      databaseWorker.writeToMachineTable(new MachineEntity(
          "Zaku",
          "Zaku supreme",
          "18.00",
          "30.00",
          "Thermonuclear reactor",
          "3000",
          "20",
          "1"
      ));
      databaseWorker.writeToMachineToPilotTable(new MachineToPilotEntity(
          "1",
          "1"
      ));
    });
  }

  @Test
  public void writeToPilotTest() {
    Entity entity = new PilotEntity(
        "Char",
        "M",
        "Zeon",
        "1"
    );
    assertDoesNotThrow(() -> {
      databaseWorker.writePilotToTable(entity);
    });
  }


  @Test
  public void readPilotsFromDBTest() {
    assertDoesNotThrow(() -> {
      List<Entity> pilots = databaseWorker.readPilotsFromDB();
      assertNotNull(pilots);
      assertFalse(pilots.isEmpty());
    });
  }

  @Test
  public void updatePilotsTableTest() {
    Entity entity = new PilotEntity(
        "Char Aznable",
        "M",
        "Zeon",
        "1"
    );
    assertDoesNotThrow(() -> {
      databaseWorker.updatePilotsTable(entity, "1");
    });
  }

  @Test
  public void deleteFromPilotsTableTest() {
    assertDoesNotThrow(() -> {
      String result = databaseWorker.deleteFromPilotsTable("id = 1");
      assertNotEquals("", result);
    });
  }

  @Test
  public void writeToMachineTableTest() {
    Entity entity = new MachineEntity(
        "Zaku",
        "Zaku supreme",
        "18.00",
        "30.00",
        "Thermonuclear reactor",
        "3000",
        "20",
        "1"
    );
    assertDoesNotThrow(() -> {
      databaseWorker.writeToMachineTable(entity);
    });
  }

  @Test
  public void readMachinesFromDBTest() {
    assertDoesNotThrow(() -> {
      List<Entity> machines = databaseWorker.readMachinesFromDB();
      assertNotNull(machines);
      assertFalse(machines.isEmpty());
    });
  }

  @Test
  public void updateMachinesTableTest() {
    Entity entity = new MachineEntity(
        "Zaku",
        "Zaku SUPER",
        "18.00",
        "30.00",
        "Thermonuclear reactor",
        "3000",
        "21",
        "1"
    );
    assertDoesNotThrow(() -> {
      databaseWorker.updateMachinesTable(entity, "1");
    });
  }

  @Test
  public void deleteFromMachinesTableTest() {
    assertDoesNotThrow(() -> {
      String result = databaseWorker.deleteFromMachinesTable("id = 1");
      assertNotEquals("", result);
    });
  }

  @Test
  public void writeToGovernmentsTableTest() {
    Entity entity = new GovernmentEntity(
        "Zeon",
        "Z",
        "Side 3"
    );
    assertDoesNotThrow(() -> {
      databaseWorker.writeToGovernmentsTable(entity);
    });
  }

  @Test
  public void readGovernmentsFromDBTest() {
    assertDoesNotThrow(() -> {
      List<Entity> governments = databaseWorker.readGovernmentsFromDB();
      assertNotNull(governments);
      assertFalse(governments.isEmpty());
    });
  }

  @Test
  public void updateGovernmentTableTest() {
    Entity entity = new GovernmentEntity(
        "Zeon",
        "Z",
        "Titan"
    );
    assertDoesNotThrow(() -> {
      databaseWorker.updateGovernmentsTable(entity, "1");
    });
  }


  @Test
  public void readMachineToPilotFromDBTest() {
    assertDoesNotThrow(() -> {
      List<Entity> machineToPilots = databaseWorker.readMachineToPilotFromDB();
      assertNotNull(machineToPilots);
      assertTrue(machineToPilots.isEmpty());
    });
  }

  @Test
  public void updateMachineToPilotTableOnPilotTest() {
    Entity entity = new MachineToPilotEntity(
        "1",
        "2"
    );

    assertDoesNotThrow(() -> {
      databaseWorker.updatePilotsTable(
          new PilotEntity(
              "Char",
              "M",
              "Zeon",
              "1"
          ),
          "1"
      );

      databaseWorker.updateMachinesToPilotsOnPilotsTable(entity, "1");
    });
  }

  @Test
  public void updateMachineToPilotTableOnMachineTest() {
    Entity entity = new MachineToPilotEntity(
        "1",
        "2"
    );
    assertDoesNotThrow(() -> {
      databaseWorker.updateMachinesTable(
          new MachineEntity(
              "Zaku",
              "Zaku supreme",
              "18.00",
              "30.00",
              "Thermonuclear reactor",
              "3000",
              "20",
              "1")
          ,
          "2"
      );

      databaseWorker.updateMachinesToPilotsOnMachinesTable(entity, "1");
    });
  }

  @Test
  public void deleteFromMachinesToPilotsTableTest() {
    assertDoesNotThrow(() -> {
      String result = databaseWorker.deleteFromMachinesToPilotsTable("pilot_id = 1");
      assertNotEquals("", result);
    });
  }
}