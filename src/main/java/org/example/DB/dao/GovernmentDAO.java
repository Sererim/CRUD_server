package org.example.db.dao;

import java.util.List;

public class GovernmentDAO extends DAO {
  private final String name;
  private final String acronym;
  private final String capital;

  public GovernmentDAO(String name, String acronym, String capital) {
    this.name    = name;
    this.acronym = acronym;
    this.capital = capital;
  }

  @Override
  public List<String> getDataToDatabase() {
    return List.of(
        this.name,
        this.acronym,
        this.capital
    );
  }

  @Override
  public String toString() {
    return String.join(" | ",
        this.name,
        this.acronym,
        this.capital
    );
  }

  // Generated getters.
  public String getName() {
    return name;
  }

  public String getAcronym() {
    return acronym;
  }

  public String getCapital() {
    return capital;
  }
}
