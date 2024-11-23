package org.example.utils;

import org.example.bl.*;
import org.example.db.dao.DAO;

import java.util.List;

public final class ConverterEntity {

  public static Entity convert(DAO dao, String name) {
    List<String> data = dao.getDataToDatabase();
    return switch (name) {
      case "pilot" -> new PilotEntity(
          data.get(0),
          data.get(1),
          data.get(2),
          data.get(3)
      );
      case "machine" -> new MachineEntity(
          data.get(0),
          data.get(1),
          data.get(2),
          data.get(3),
          data.get(4),
          data.get(5),
          data.get(6),
          data.get(7)
      );
      case "government" -> new GovernmentEntity(
          data.get(0),
          data.get(1),
          data.get(2)
      );
      case "machineToPilot" -> new MachineToPilotEntity(
          data.get(0),
          data.get(1)
      );
      default -> null;
    };
  }
}
