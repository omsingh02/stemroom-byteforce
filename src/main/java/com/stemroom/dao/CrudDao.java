package com.stemroom.dao;

import java.util.List;
import java.util.Optional;

/**
 * The basic operations each table gets. Every DAO implements this for its own
 * model class, so the servlets always see the same shape of API.
 */
public interface CrudDao<T> {

    List<T> findAll();

    Optional<T> findById(int id);

    /** Saves a new row and returns the id the database generated for it. */
    int insert(T item);

    void deleteById(int id);
}
