package com.knubisoft.strategy.impl;

import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.Strategy;
import com.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StrategyCsv implements Strategy {
    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof FileReadWriteSource
                && FilenameUtils.getExtension(((FileReadWriteSource) data).getFile().getName())
                .equals("csv");
    }

    @Override
    @SneakyThrows
    public Table createTableByStrategy(DataReadWriteSource<?> data, Class<?> clazz) {
        String content = ((FileReadWriteSource) data).getContent();
        CSVReader reader = new CSVReader(new StringReader(content));
        List<String[]> strings = reader.readAll();
        String[] names = strings.get(0);
        List<Map<String, String>> rows = IntStream.range(1, strings.size())
                .boxed()
                .map(num -> createRow(names, strings.get(num)))
                .collect(Collectors.toList());
        Map<Integer, Map<String, String>> table = IntStream.range(0, rows.size())
                .boxed()
                .collect(Collectors.toMap(num -> num, rows::get));
        return new Table(table);
    }

    private Map<String, String> createRow(String[] names, String[] values) {
        return IntStream.range(0, names.length)
                .boxed()
                .collect(Collectors.toMap(num -> names[num], num -> values[num]));
    }
}
