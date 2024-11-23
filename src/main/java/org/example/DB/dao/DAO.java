package org.example.db.dao;

import java.util.List;

/**
 * Abstract class for all Entities.
 */
public abstract class DAO {
  abstract public List<String> getDataToDatabase();
}
