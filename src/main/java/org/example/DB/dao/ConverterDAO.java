package org.example.db.dao;

import org.example.bl.Entity;

import java.util.List;

/**
 * Factory like class.
 */
public final class ConverterDAO {

  /**
   * Convert Entity to DAO
   *
   * @param entity valid Entity
   * @param name   correct name of the entity
   * @return new DAO entity that is ready to be written to.
   */
  public static DAO convert(Entity entity, String name) {
    List<String> data = entity.unwrap();
    return switch (name) {
      case "pilot" -> new PilotDAO(data.get(0), toGender(data.get(1)), data.get(2), data.get(3));
      case "machine" -> new MachineDAO(
          data.get(0),
          data.get(1),
          data.get(2),
          data.get(3),
          data.get(4),
          data.get(5),
          data.get(6),
          data.get(7)
      );
      case "government" -> new GovernmentDAO(
          data.get(0),
          data.get(1),
          data.get(2)
      );
      case "machineToPilot" -> new MachineToPilotDAO(
          data.get(0),
          data.get(1)
      );
      default -> null;
    };
  }

  public static gender toGender(String gen) {
    if (gen.equals("M"))
      return gender.M;
    else if (gen.equals("F"))
      return gender.F;
    else
      return gender.M;
  }
}
