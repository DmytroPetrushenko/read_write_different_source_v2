package com.knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.Strategy;
import com.knubisoft.utils.JacksonTableBuilderUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

public class StrategyJson implements Strategy {
    private final JacksonTableBuilderUtils builder = JacksonTableBuilderUtils.getBuilder();

    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof FileReadWriteSource
                && FilenameUtils.getExtension(((FileReadWriteSource) data).getFile().getName())
                .equals("json");
    }

    @Override
    @SneakyThrows
    public Table createTableByStrategy(DataReadWriteSource<?> data, Class<?> clazz) {
        String file = ((FileReadWriteSource) data).getContent();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(file);
        List<String> mapping = builder.createMapping(jsonNode);
        return new Table(builder.createTable(jsonNode, mapping));
    }
}