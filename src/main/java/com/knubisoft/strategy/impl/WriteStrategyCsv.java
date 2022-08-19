package com.knubisoft.strategy.impl;

import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.WriteStrategy;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

public class WriteStrategyCsv implements WriteStrategy {
    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof FileReadWriteSource
                && FilenameUtils.getExtension(((FileReadWriteSource) data).getFile().getName())
                .equals("csv");
    }

    @Override
    @SneakyThrows
    public <T> void writeDataToFile(DataReadWriteSource<?> data, List<T> list) {
        String path = ((FileReadWriteSource) data).getFile().getPath();
        CSVWriter writer = new CSVWriter(new FileWriter(path));
        String[] names = getRowNamesFromEntity(list);
        writer.writeAll(convertEntitiesToList(names, list));
        writer.flush();
        writer.close();
    }

    private <T> List<String[]> convertEntitiesToList(String[] names, List<T> list) {
        List<String[]> result = new ArrayList<>();
        result.add(names);
        IntStream.range(0, list.size())
                .boxed()
                .forEach(index -> result.add(createRowValuesFromEntity(names, list.get(index))));
        return result;
    }

    private <T> String[] createRowValuesFromEntity(String[]names, T object) {
        return Arrays.stream(names)
                .map(name -> getValues(object, name))
                .toArray(String[]::new);
    }

    @SneakyThrows
    private <T> String getValues(T object, String name) {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return String.valueOf(field.get(object));
    }

    private <T> String[] getRowNamesFromEntity(List<T> list) {
        return Arrays.stream(list.get(0).getClass().getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
    }
}
