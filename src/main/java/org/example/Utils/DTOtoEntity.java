package org.example.utils;

import org.example.bl.*;

import java.util.List;

public final class DTOtoEntity {

  public static Entity convert(Unwrapable wrapped, String target) {
    List<String> data = wrapped.unwrap();
    switch (target) {
      case "government" -> {
        return new GovernmentEntity(
            data.get(0),
            data.get(1),
            data.get(2)
            );
      }
      case "pilot" -> {
        return new PilotEntity(
            data.get(0),
            data.get(1),
            data.get(2),
            data.get(3)
        );
      }
      case "machine" -> {
        return new MachineEntity(
            data.get(0),
            data.get(1),
            data.get(2),
            data.get(3),
            data.get(4),
            data.get(5),
            data.get(6),
            data.get(7)
        );
      }
      case "machineToPilot" -> {
        return new MachineToPilotEntity(
            data.get(0),
            data.get(1)
        );
      }
    }
    return null;
  }
}
