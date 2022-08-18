package com.knubisoft.strategy;

import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;

public interface Strategy {
    boolean isApplied(DataReadWriteSource<?> data);

    Table createTableByStrategy(DataReadWriteSource<?> data, Class<?> clazz);
}
