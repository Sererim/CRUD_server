package org.example.db.dao;

import java.util.List;

public class MachineToPilotDAO extends DAO {
  private final String machineId;
  private final String pilotId;

  public MachineToPilotDAO(String machineId, String pilotId) {
    this.machineId = machineId;
    this.pilotId = pilotId;
  }

  @Override
  public List<String> getDataToDatabase() {
    return List.of(
        this.machineId,
        this.pilotId
    );
  }

  @Override
  public String toString() {
    return String.join(" | ",
        this.pilotId,
        this.machineId
    );
  }

  // Generated getters
  public String getMachineId() {
    return machineId;
  }

  public String getPilotId() {
    return pilotId;
  }
}
