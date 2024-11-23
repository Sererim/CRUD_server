package org.example.bl;

import org.example.db.dao.ConverterDAO;
import org.example.db.dao.DAO;
import org.example.utils.Unwrapable;

import java.util.List;

public class MachineToPilotEntity extends Entity implements Unwrapable {

  private final String machineId;
  private final String pilotId;

  public MachineToPilotEntity(String machineId, String pilotId) {
    this.machineId = machineId;
    this.pilotId = pilotId;
  }

  @Override
  public DAO convertToDAO() {
    return ConverterDAO.convert(this, "machineToPilot");
  }

  @Override
  public List<String> unwrap() {
    return List.of(
        this.machineId,
        this.pilotId
    );
  }

  public String getMachineId() {
    return machineId;
  }

  public String getPilotId() {
    return pilotId;
  }
}
