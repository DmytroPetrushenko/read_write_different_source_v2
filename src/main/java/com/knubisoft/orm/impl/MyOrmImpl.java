package com.knubisoft.orm.impl;

import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;
import com.knubisoft.orm.MyOrm;
import com.knubisoft.utils.StrategyUtil;
import com.knubisoft.utils.TypeConvertorUtil;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class MyOrmImpl implements MyOrm {
    private final StrategyUtil strategyUtil;

    public MyOrmImpl() {
        this.strategyUtil = StrategyUtil.getStrategyUtil();
    }

    @Override
    public <T> List<T> readAll(DataReadWriteSource<?> data, Class<T> clazz) {
        Table table = convertToTable(data, clazz);
        return createListEntitiesFromTable(table, clazz);
    }

    private <T> List<T> createListEntitiesFromTable(Table table, Class<T> clazz) {
        return table.getTable().values().stream()
                .map(map -> createEntity(map, clazz))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private <T> T createEntity(Map<String, String> entityData, Class<T> clazz) {
        T instance = clazz.getConstructor().newInstance();
        entityData.entrySet()
                        .forEach(entry -> uploadField(entry, instance));
        return instance;
    }

    @SneakyThrows
    private <T> void uploadField(Map.Entry<String, String> entry, T instance) {
        Field field = instance.getClass().getDeclaredField(entry.getKey());
        field.setAccessible(true);
        field.set(instance, TypeConvertorUtil.getObject()
                .transformStringToTypeField(entry.getValue(), field));
    }

    private Table convertToTable(DataReadWriteSource<?> data, Class<?> clazz) {
        return strategyUtil.getReadStrategyList().stream()
                .filter(strategy -> strategy.isApplied(data))
                .map(strategy -> strategy.createTableByStrategy(data, clazz))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public <T> void writeAll(DataReadWriteSource<?> data, List<T> list) {
        strategyUtil.getWriteStrategyList().stream()
                .filter(writeStrategy -> writeStrategy.isApplied(data))
                .peek(writeStrategy -> writeStrategy.writeDataToFile(data, list))
                .findFirst();
    }
}
