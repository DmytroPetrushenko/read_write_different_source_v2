package com.knubisoft.strategy;

import com.knubisoft.dto.DataReadWriteSource;
import java.util.List;

public interface WriteStrategy extends Strategy {

    boolean isApplied(DataReadWriteSource<?> data);

    <T> void writeDataToFile(DataReadWriteSource<?> data, List<T> list);
}
