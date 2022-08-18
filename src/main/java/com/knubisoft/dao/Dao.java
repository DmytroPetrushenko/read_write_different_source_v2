package com.knubisoft.dao;

import java.util.List;

public interface PersonDao {
    <T> void createTable(List<T> list);

    void insertInDatabase(Object object);
}
