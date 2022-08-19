package com.knubisoft.dao.impl;

import com.knubisoft.annotation.Table;
import com.knubisoft.dao.Dao;
import com.knubisoft.utils.ConnectionUtil;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;

public class DaoImpl implements Dao {
    private final ConnectionUtil util = new ConnectionUtil();

    @Override
    @SneakyThrows
    public <T> void createTable(List<T> list) {
        Class<?> clazz = list.get(0).getClass();
        String name = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        String drop = "DROP TABLE IF EXISTS " + name + ";";
        String table = "CREATE TABLE " + name + "(id INTEGER PRIMARY KEY AUTOINCREMENT);";
        try (Connection con = util.getConnection();
                Statement statement = con.createStatement()) {
            statement.executeUpdate(drop);
            statement.executeUpdate(table);
            Arrays.stream(fields)
                    .forEach(field -> addColumn(statement, name, field.getName(), field.getType()));
            list.forEach(object -> insertInDatabase(statement, name, object));

        }
    }

    @SneakyThrows
    private void addColumn(Statement statement, String tableName, String name, Class<?> type) {
        if (name.equals("id")) {
            return;
        }
        String column = "ALTER TABLE " + tableName + " ADD " + name + " "
                + convertFieldTypeToSqlValue(type) + ";";
        statement.executeUpdate(column);
    }

    @SneakyThrows
    private void insertInDatabase(Statement statement, String name, Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder("INSERT INTO " + name + " (");
        addColumnsNames(fields, builder);
        builder.append("VALUES (");
        addColumnsValues(fields, object, builder);
        String string = builder.toString();
        statement.executeUpdate(string);
    }

    @SneakyThrows
    private void addColumnsValues(Field[] fields, Object object, StringBuilder builder) {
        Iterator<Field> fieldIterator = Arrays.stream(fields).iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            if (field.getName().equals("id")) {
                continue;
            }
            field.setAccessible(true);
            String value = String.valueOf(field.get(object));
            Class<?> fieldType = field.getType();
            if (fieldType.equals(String.class) || fieldType.equals(LocalDate.class)) {
                builder.append("'").append(value).append("'");
            } else {
                builder.append(value);
            }
            if (fieldIterator.hasNext()) {
                builder.append(", ");
            } else {
                builder.append(");");
            }
        }
    }

    private void addColumnsNames(Field[] fields, StringBuilder builder) {
        Iterator<Field> fieldIterator = Arrays.stream(fields).iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            if (field.getName().equals("id")) {
                continue;
            }
            builder.append(field.getName());
            if (fieldIterator.hasNext()) {
                builder.append(", ");
            } else {
                builder.append(") ");
            }
        }
    }

    private String convertFieldTypeToSqlValue(Class<?> type) {
        Map<Class<?>, String> typeValueMap = new LinkedHashMap<>();
        typeValueMap.put(String.class, "varchar(255) not null");
        typeValueMap.put(LocalDate.class, "varchar(10)  not null");
        return typeValueMap.get(type);
    }
}
