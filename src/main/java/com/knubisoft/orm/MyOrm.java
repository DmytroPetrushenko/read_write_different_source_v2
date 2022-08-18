package com.knubisoft.orm;

import com.knubisoft.dto.DataReadWriteSource;
import java.util.List;

public interface MyOrm {

    <T> List<T> readAll(DataReadWriteSource<?> data, Class<T> clazz);

    <T> void writeAll(DataReadWriteSource<?> data, List<T> clazz);
}
