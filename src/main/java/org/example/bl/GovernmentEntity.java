package org.example.bl;

import org.example.db.dao.ConverterDAO;
import org.example.db.dao.DAO;
import org.example.utils.Unwrapable;

import java.util.List;

public class GovernmentEntity extends Entity implements Unwrapable {

  private final String name;
  private final String acronym;
  private final String capital;

  public GovernmentEntity(String name, String acronym, String capital) {
    this.name = name;
    this.acronym = acronym;
    this.capital = capital;
  }

  @Override
  public DAO convertToDAO() {
    return ConverterDAO.convert(this, "government");
  }

  @Override
  public List<String> unwrap() {
    return List.of(
        this.name,
        this.acronym,
        this.capital
    );
  }

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
