package com.knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.knubisoft.annotation.XML;
import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.Strategy;
import com.knubisoft.utils.JacksonTableBuilderUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StrategyXml implements Strategy {
    private final JacksonTableBuilderUtils builder = JacksonTableBuilderUtils.getBuilder();

    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof FileReadWriteSource
                && FilenameUtils.getExtension(((FileReadWriteSource) data).getFile().getName())
                .equals("xml");
    }

    @Override
    @SneakyThrows
    public Table createTableByStrategy(DataReadWriteSource<?> data, Class<?> clazz) {
        JsonNode tree = createNodeTree(((FileReadWriteSource) data).getContent(), getClassName(clazz));
        List<String> mapping =  builder.createMapping(tree);
        Map<Integer, Map<String, String>> table = builder.createTable(tree, mapping);
        return new Table(table);
    }

    private String getClassName(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().equals(XML.class))
                .map(annotation -> ((XML) annotation).name())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The name of this class: "
                        + clazz + " is absent for XML"));
    }

    @SneakyThrows
    private JsonNode createNodeTree(String file, String name) {
        XmlMapper mapper = new XmlMapper();
        JsonNode mainNode = mapper.readTree(file);
        return mainNode.get(name);
    }


}
