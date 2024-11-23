package org.example.bl;

import org.example.db.dao.ConverterDAO;
import org.example.db.dao.DAO;

import java.util.List;

public class PilotEntity extends Entity {

  private final String name;
  private final String nationality;
  private final String gender;
  private final String govId;


  public PilotEntity(String name, String gender, String nationality, String govId) {
    this.name = name;
    this.nationality = nationality;
    this.gender = gender;
    this.govId = govId;
  }

  @Override
  public DAO convertToDAO() {
    return ConverterDAO.convert(this, "pilot");
  }

  @Override
  public List<String> unwrap() {
    return List.of(
        this.name,
        this.gender,
        this.nationality,
        this.govId
    );
  }

  public String getName() {
    return name;
  }

  public String getNationality() {
    return nationality;
  }

  public String getGender() {
    return gender;
  }

  public String getGovId() {
    return govId;
  }
}
