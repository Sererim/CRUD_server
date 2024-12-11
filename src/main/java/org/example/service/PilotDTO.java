package org.example.service;

import org.example.utils.Unwrapable;

import java.util.List;

public class PilotDTO implements Unwrapable {
  private String name;
  private String gender;
  private String nationality;
  private String governmentId;

  public PilotDTO(String name, String gender, String nationality, String governmentId) {
    this.name = name;
    this.gender = gender;
    this.nationality = nationality;
    this.governmentId = governmentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public String getGovernmentId() {
    return governmentId;
  }

  public void setGovernmentId(String governmentId) {
    this.governmentId = governmentId;
  }

  @Override
  public List<String> unwrap() {
    return List.of(
        this.name,
        this.gender,
        this.nationality,
        this.governmentId
    );
  }
}
