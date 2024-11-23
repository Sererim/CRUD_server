package org.example.service.dto;

import org.example.utils.Unwrapable;

import java.util.List;

public class MachineDTO implements Unwrapable {

  private String name;
  private String series;
  private String headHeight;
  private String maxWeight;
  private String powerSource;
  private String powerOutput;
  private String maxAcceleration;
  private String governmentId;

  public MachineDTO(
      String name, String series, String headHeight,
      String maxWeight, String powerSource, String powerOutput,
      String maxAcceleration
  ) {
    this.name = name;
    this.series = series;
    this.headHeight = headHeight;
    this.maxWeight = maxWeight;
    this.powerSource = powerSource;
    this.powerOutput = powerOutput;
    this.maxAcceleration = maxAcceleration;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public String getHeadHeight() {
    return headHeight;
  }

  public void setHeadHeight(String headHeight) {
    this.headHeight = headHeight;
  }

  public String getMaxWeight() {
    return maxWeight;
  }

  public void setMaxWeight(String maxWeight) {
    this.maxWeight = maxWeight;
  }

  public String getPowerSource() {
    return powerSource;
  }

  public void setPowerSource(String powerSource) {
    this.powerSource = powerSource;
  }

  public String getPowerOutput() {
    return powerOutput;
  }

  public void setPowerOutput(String powerOutput) {
    this.powerOutput = powerOutput;
  }

  public String getMaxAcceleration() {
    return maxAcceleration;
  }

  public void setMaxAcceleration(String maxAcceleration) {
    this.maxAcceleration = maxAcceleration;
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
        this.series,
        this.headHeight,
        this.maxWeight,
        this.powerSource,
        this.powerOutput,
        this.maxAcceleration,
        "1"
    );
  }
}
