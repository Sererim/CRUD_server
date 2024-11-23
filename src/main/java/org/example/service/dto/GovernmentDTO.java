package org.example.service.dto;

import org.example.utils.Unwrapable;

import java.util.List;

public class GovernmentDTO implements Unwrapable {
  private String name;
  private String acronym;
  private String capital;

  public GovernmentDTO(String name, String acronym, String capital) {
    this.name = name;
    this.acronym = acronym;
    this.capital = capital;
  }

  // Getters and setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getAcronym() { return acronym; }
  public void setAcronym(String acronym) { this.acronym = acronym; }
  public String getCapital() { return capital; }
  public void setCapital(String capital) { this.capital = capital; }

  @Override
  public List<String> unwrap() {
    return List.of(
        this.name,
        this.acronym,
        this.capital
    );
  }
}
