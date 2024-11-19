package org.example.db;


import java.util.List;

/**
 * User <strong>Entity</strong> class.
 * Is used for work with the Database.
 */
public class Pilot extends Entity {
  private final String name;
  private final gender sex;
  private final String nationality;
  private final Long govId;

  public Pilot(String name, gender sex, String nationality, Long govId) {
    this.name = name;
    this.sex = sex;
    this.nationality = nationality;
    this.govId = govId;
  }

  public Long getGovId() {
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
        this.govId.toString()
    );
  }

  @Override
  public List<String> getDataToDatabase() {
    return List.of(
        this.name,
        this.sex.name(),
        this.nationality,
        this.govId.toString()
    );
  }
}
