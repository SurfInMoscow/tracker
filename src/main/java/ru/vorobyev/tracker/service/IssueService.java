package ru.vorobyev.tracker.service;

import java.util.List;

public interface IssueService<T> {
    T save(T issue);

    boolean delete(int id);

    T get(int id);

    T getByName(String name);

    List<T> getAll();
}
