package org.example.bl;

import org.example.db.dao.ConverterDAO;
import org.example.db.dao.DAO;
import org.example.utils.Unwrapable;

import java.util.List;

public class MachineEntity extends Entity implements Unwrapable {

  private final String name;
  private final String series;
  private final String headHeight;
  private final String maxWeight;
  private final String powerSource;
  private final String powerOutput;
  private final String maxAcceleration;
  private final String govId;

  public MachineEntity(String name, String series, String headHeight,
                    String maxWeight, String powerSource, String powerOutput,
                    String maxAcceleration, String govId
  ) {
    this.name            = name;
    this.series          = series;
    this.headHeight      = headHeight;
    this.maxWeight       = maxWeight;
    this.powerSource     = powerSource;
    this.powerOutput     = powerOutput;
    this.maxAcceleration = maxAcceleration;
    this.govId           = govId;
  }

  @Override
  public DAO convertToDAO() {
    return ConverterDAO.convert(this, "machine");
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
        this.govId
    );
  }

  public String getName() {
    return name;
  }

  public String getSeries() {
    return series;
  }

  public String getHeadHeight() {
    return headHeight;
  }

  public String getMaxWeight() {
    return maxWeight;
  }

  public String getPowerSource() {
    return powerSource;
  }

  public String getPowerOutput() {
    return powerOutput;
  }

  public String getMaxAcceleration() {
    return maxAcceleration;
  }

  public String getGovId() {
    return govId;
  }
}
