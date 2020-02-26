package com.karpovich.homework.database.DAO;

public interface Dao<T> {

    T get(long id);

    void save(T t);
}