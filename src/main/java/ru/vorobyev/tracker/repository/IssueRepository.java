package ru.vorobyev.tracker.repository;

import java.util.List;

public interface IssueRepository<T> {
    T save(T issue);

    boolean delete(int id);

    T get(int id);

    T getByName(String name);

    List<T> getAll();
}
