package kz.danke.test.project.service;

import java.util.List;

public interface CrudOperations<T> {

    List<T> findAll();

    T findById(String id);

    T save(T t);

    void delete(String id);
}
