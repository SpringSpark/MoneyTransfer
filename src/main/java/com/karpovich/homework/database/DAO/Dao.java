package com.karpovich.homework.database.DAO;

import java.util.List;

public interface Dao<T> {

    T get(long id);

    void save(T t);
}