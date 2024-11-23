package org.example.db;

import org.example.utils.ConverterEntity;
import org.example.bl.Entity;
import org.example.db.dao.*;
import org.example.timbering.Timberland;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for work with the database
 */
public class DatabaseWorker {

  private static final String TAG = "DatabaseWorker";

  // Names of the tables in the database.
  private static final String PILOTS_TABLE             = "pilots";
  private static final String MACHINES_TABLE           = "machines";
  private static final String GOVERNMENTS_TABLE        = "governments";
  private static final String MACHINES_TO_PILOTS_TABLE = "machines_pilots";

  private final DatabaseManager db;

  public DatabaseWorker(AuthObject authObject) throws SQLException {
    db = new DatabaseManager(authObject);
  }

  public List<Entity> readPilotsFromDB() throws SQLException {
    LinkedList<DAO> daos = new LinkedList<>();
    try (ResultSet resultSet = db.readAllFrom(PILOTS_TABLE)) {
      while (resultSet.next()) {
        daos.add(
            new PilotDAO(
                resultSet.getString("name"),
                ConverterDAO.toGender(resultSet.getString("sex")),
                resultSet.getString("nationality"),
                resultSet.getString("government_id") == null
                    ? "1"
                    : resultSet.getString("government_id")
            )
        );
      }
      return daos.stream().map(obj -> ConverterEntity.convert(obj, "pilot")).toList();
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Error on reading pilots from the database\n" + sqle, sqle);
      throw new SQLException("Couldn't read from the database!\nTable is: " + PILOTS_TABLE);
    }
  }

  public List<Entity> readMachinesFromDB() throws SQLException {
    LinkedList<DAO> daos = new LinkedList<>();
    try (ResultSet resultSet = db.readAllFrom(MACHINES_TABLE)) {
      while (resultSet.next()) {
        daos.add(
            new MachineDAO(
                resultSet.getString("name"),
                resultSet.getString("series"),
                resultSet.getString("head_height"),
                resultSet.getString("max_weight"),
                resultSet.getString("power_source"),
                resultSet.getString("power_output"),
                resultSet.getString("max_acceleration"),
                resultSet.getString("government_id")
            )
        );
      }
      return daos.stream().map(obj -> ConverterEntity.convert(obj, "machine")).toList();
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Error on reading machines from the database\n" + sqle, sqle);
      throw new SQLException("Couldn't read from the database!\nTable is: " + MACHINES_TABLE);
    }
  }

  public List<Entity> readGovernmentsFromDB() throws SQLException {
    LinkedList<DAO> daos = new LinkedList<>();
    try (ResultSet resultSet = db.readAllFrom(GOVERNMENTS_TABLE)) {
      while (resultSet.next()) {
        daos.add(
            new GovernmentDAO(
                resultSet.getString("name"),
                resultSet.getString("acronym"),
                resultSet.getString("capital")
            )
        );
      }
      return daos.stream().map(obj -> ConverterEntity.convert(obj, "government")).toList();
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Error on reading governments from the database\n" + sqle, sqle);
      throw new SQLException("Couldn't read from the database!\nTable is: " + GOVERNMENTS_TABLE);
    }
  }

  public List<Entity> readMachineToPilotFromDB() throws SQLException {
    LinkedList<DAO> daos = new LinkedList<>();
    try (ResultSet resultSet = db.readAllFrom(MACHINES_TO_PILOTS_TABLE)) {
      while (resultSet.next()) {
        daos.add(
            new MachineToPilotDAO(
                resultSet.getString("machine_id"),
                resultSet.getString("pilot_id")
                )
        );
      }
      return daos.stream().map(obj -> ConverterEntity.convert(obj, "machineToPilot")).toList();
    } catch (SQLException sqle) {
      Timberland.cutException(TAG, "Error on reading governments from the database\n" + sqle, sqle);
      throw new SQLException("Couldn't read from the database!\nTable is: " + MACHINES_TO_PILOTS_TABLE);
    }
  }

  /**
   * Method to write Entity to a table
   * @param entity valid entity object
   * @param tableName valid table name
   * @return true if entity was written to a table
   */
  private void writeToTable(Entity entity, String tableName) throws SQLException {
    DAO dao = entity.convertToDAO();
    db.writeToTable(tableName, dao);
  }

  /**
   * Method to write PilotEntity to a table
   * @param entity PilotEntity
   * @return true if entity was written to the table
   */
  public void writePilotToTable(Entity entity) throws SQLException {
    writeToTable(entity, PILOTS_TABLE);
  }

  /**
   * Method to write MachineEntity to a table
   * @param entity MachineEntity
   * @return true if entity was written to the table
   */
  public void writeToMachineTable(Entity entity) throws SQLException {
    writeToTable(entity, MACHINES_TABLE);
  }

  /**
   * Method to write GovernmentEntity to a table
   * @param entity GovernmentEntity
   * @return true if entity was written to the table
   */
  public void writeToGovernmentsTable(Entity entity) throws SQLException {
    writeToTable(entity, GOVERNMENTS_TABLE);
  }

  /**
   * Method to write MachineToPilotEntity to a table
   *
   * @param entity MachineToPilotEntity
   */
  public void writeToMachineToPilotTable(Entity entity) throws SQLException {
    db.writeToMachinesToPilotsTable(entity.convertToDAO(), MACHINES_TO_PILOTS_TABLE);
  }

  /**
   * Method to update data in a table
   * @param entity valid entity
   * @param table valid table name
   * @param id an id that is present in the db
   * @param isPilot for MachinesToTable
   * @return true if update was successful
   */
  private void updateTable(Entity entity, String table, String id, Boolean isPilot) throws SQLException {
    DAO dao = entity.convertToDAO();
    db.updateTable(
        table,
        dao,
        id,
        isPilot
    );
  }

  /**
   * Update pilots table with a Pilot entity
   * @param entity PilotEntity object
   * @param id an id of a row from the pilots table
   * @return true if operation resulted in successes
   */
  public void updatePilotsTable(Entity entity, String id) throws SQLException {
    updateTable(
        entity,
        PILOTS_TABLE,
        id,
        true
    );
  }

  /**
   * Update governments table with a Government entity
   * @param entity GovernmentEntity object
   * @param id an id of a row from the governments table
   * @return true if operation resulted in successes
   */
  public void updateGovernmentsTable(Entity entity, String id) throws SQLException {
    updateTable(
        entity,
        GOVERNMENTS_TABLE,
        id,
        true
    );
  }

  /**
   * Update machines table with a Machine entity
   * @param entity MachineEntity object
   * @param id an id of a row from the machines table
   * @return true if operation resulted in successes
   */
  public void updateMachinesTable(Entity entity, String id) throws SQLException {
    updateTable(
        entity,
        MACHINES_TABLE,
        id,
        true
    );
  }

  /**
   * Update data in machines_pilots table with a MachineToPilot entity
   * @param entity MachineToPilotEntity
   * @param id an id of a row from the machines_pilots table relative to pilot_id
   * @return true if operation resulted in successes
   */
  public void updateMachinesToPilotsOnPilotsTable(Entity entity, String id) throws SQLException {
    updateTable(
        entity,
        MACHINES_TO_PILOTS_TABLE,
        id,
        true
    );
  }

  /**
   * Update data in machines_pilots table with a MachineToPilot entity
   * @param entity MachineToPilotEntity
   * @param id an id of a row from the machines_pilots table relative to machine_id
   * @return true if operation resulted in successes
   */
  public void updateMachinesToPilotsOnMachinesTable(Entity entity, String id) throws SQLException {
    updateTable(
        entity,
        MACHINES_TO_PILOTS_TABLE,
        id,
        false
    );
  }

  private String deleteFromTable(String table, String clause) throws SQLException {
    ResultSet resultSet = db.deleteFromTable(
        table,
        clause
    );

    String result = resultSet.toString();
    resultSet.close();
    return result;
  }

  /**
   * Delete an entry from the pilots table
   * @param clause a correct sql clause for WHERE statement (Example: "id = 1")
   * @return a ResultSet converted to a String
   */
  public String deleteFromPilotsTable(String clause) throws SQLException {
    return deleteFromTable(PILOTS_TABLE, clause);
  }

  /**
   * Delete an entry from the machines table
   * @param clause a correct sql clause for WHERE statement (Example: "id = 1")
   * @return a ResultSet converted to a String
   */
  public String deleteFromMachinesTable(String clause) throws SQLException {
    return deleteFromTable(MACHINES_TABLE, clause);
  }

  /**
   * Delete an entry from the governments table
   * @param clause a correct sql clause for WHERE statement (Example: "id = 1")
   * @return a ResultSet converted to a String
   */
  public String deleteFromGovernmentsTable(String clause) throws SQLException {
    return deleteFromTable(GOVERNMENTS_TABLE, clause);
  }

  /**
   * Delete an entry from the machines_pilots table
   * @param clause a correct sql clause for WHERE statement (Example: "id = 1")
   * @return a ResultSet converted to a String
   */
  public String deleteFromMachinesToPilotsTable(String clause) throws SQLException {
    return deleteFromTable(MACHINES_TO_PILOTS_TABLE, clause);
  }

}
