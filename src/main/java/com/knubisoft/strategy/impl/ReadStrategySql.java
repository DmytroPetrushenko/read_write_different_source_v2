package com.knubisoft.strategy.impl;

import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;
import com.knubisoft.dto.impl.SqlReadWriteSource;
import com.knubisoft.strategy.ReadStrategy;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;

public class ReadStrategySql implements ReadStrategy {
    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof SqlReadWriteSource;
    }

    @Override
    @SneakyThrows
    public Table createTableByStrategy(DataReadWriteSource<?> data, Class<?> clazz) {
        List<Map<String, String>> rowList = new ArrayList<>();
        List<String> namesList = getFieldsName(clazz);
        ResultSet resultSet = ((SqlReadWriteSource) data).getContent();
        while (resultSet.next()) {
            rowList.add(createRow(resultSet, namesList));
        }
        Map<Integer, Map<String, String>> table = IntStream.range(0, rowList.size())
                .boxed()
                .collect(Collectors.toMap(num -> num, rowList::get));
        return new Table(table);
    }

    private Map<String, String> createRow(ResultSet resultSet, List<String> namesList) {
        return namesList.stream()
                .collect(Collectors.toMap(name -> name, name -> getValue(resultSet, name)));
    }

    @SneakyThrows
    private String getValue(ResultSet resultSet, String name) {
        return String.valueOf(resultSet.getObject(name));
    }

    private List<String> getFieldsName(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
    }
}
