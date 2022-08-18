package com.knubisoft.strategy.impl;

import com.knubisoft.dao.Dao;
import com.knubisoft.dao.impl.DaoImpl;
import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.impl.SqlReadWriteSource;
import com.knubisoft.strategy.WriteStrategy;
import java.util.List;

public class WriteStrategySql implements WriteStrategy {
    private final Dao dao = new DaoImpl();

    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof SqlReadWriteSource;
    }

    @Override
    public <T> void writeDataToFile(DataReadWriteSource<?> data, List<T> list) {
        dao.createTable(list);
    }
}
