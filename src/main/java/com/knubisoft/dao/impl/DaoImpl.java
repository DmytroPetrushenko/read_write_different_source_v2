package com.knubisoft.dao.impl;

import com.knubisoft.Main;
import com.knubisoft.annotation.Table;
import com.knubisoft.dao.PersonDao;
import com.knubisoft.utils.ConnectionUtil;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PersonDaoImpl implements PersonDao {
    ConnectionUtil util = new ConnectionUtil();

    @Override
    @SneakyThrows
    public <T> void createTable(List<T> list) {
        Class<?> clazz = list.get(0).getClass();
        String name = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        String drop = "drop table if exists " + name + ";";
        String table = "create table " + name + "(id bigint auto_increment primary key);" ;
        try (Connection con = util.getConnection();
                Statement statement = con.createStatement()) {
            statement.executeUpdate(drop);
            statement.executeUpdate(table);
            Arrays.stream(fields)
                    .forEach(field -> addColumn(statement, name, field.getName(), field.getType()));
        }
    }

    @SneakyThrows
    private void addColumn(Statement statement, String tableName, String name, Class<?> type) {
        if (name.equals("id")) {
            return;
        }
        String column = "ALTER TABLE " + tableName + " ADD " + name + " " + convertFieldTypeToSQLValue(type) + ";";
        statement.executeUpdate(column);
    }

    @Override
    public void insertInDatabase(Object object) {

    }

    private String convertFieldTypeToSQLValue(Class<?> type){
        Map<Class<?>, String> typeValueMap = new LinkedHashMap<>();
        typeValueMap.put(String.class, "varchar(255) not null");
        typeValueMap.put(LocalDate.class, "varchar(10)  not null");
        return typeValueMap.get(type);
    }
}
