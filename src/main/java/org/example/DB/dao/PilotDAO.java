package org.example.db.dao;


import java.util.List;

/**
 * Pilot <strong>DAO</strong> class.
 * Is used for work with the Database.
 */
public class PilotDAO extends DAO {
  private final String name;
  private final gender sex;
  private final String nationality;
  private final String govId;

  public PilotDAO(String name, gender sex, String nationality, String govId) {
    this.name = name;
    this.sex = sex;
    this.nationality = nationality;
    this.govId = govId;
  }

  public String getGovId() {
    return govId;
  }

  public String getNationality() {
    return nationality;
  }

  public String getSex() {
    return sex.name();
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return String.join(" | ",
        this.name,
        this.sex.name(),
        this.nationality,
        this.govId
    );
  }

  @Override
  public List<String> getDataToDatabase() {
    return List.of(
        this.name,
        this.sex.name(),
        this.nationality,
        this.govId
    );
  }
}
