package com.knubisoft.dao;

import java.util.List;

public interface Dao {
    <T> void createTable(List<T> list);
}
