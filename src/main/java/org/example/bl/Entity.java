package org.example.bl;

import org.example.db.dao.DAO;
import org.example.utils.Unwrapable;

import java.util.List;

/**
 * Abstract entity class.
 */
abstract public class Entity implements Unwrapable {
  /**
   * Method to convert Entity to DAO.
   * @return a DAO object
   */
  abstract public DAO convertToDAO();

  /**
   * Method to unwrap entity's fields
   * @return a list of strings of fields of Entity
   */
  abstract public List<String> unwrap();
}
