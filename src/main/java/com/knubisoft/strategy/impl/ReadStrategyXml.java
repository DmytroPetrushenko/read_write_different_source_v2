package com.knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.knubisoft.annotation.Xml;
import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.ReadStrategy;
import com.knubisoft.utils.JacksonMapperUtil;
import com.knubisoft.utils.JacksonTableBuilderUtils;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

public class ReadStrategyXml implements ReadStrategy {
    private final JacksonTableBuilderUtils builder = JacksonTableBuilderUtils.getBuilder();
    private final JacksonMapperUtil mapperUtil = new JacksonMapperUtil();

    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof FileReadWriteSource
                && FilenameUtils.getExtension(((FileReadWriteSource) data).getFile().getName())
                .equals("xml");
    }

    @Override
    @SneakyThrows
    public Table createTableByStrategy(DataReadWriteSource<?> data, Class<?> clazz) {
        JsonNode tree = createNodeTree(((FileReadWriteSource) data).getContent(),
                getClassName(clazz));
        List<String> mapping = builder.createMapping(tree);
        Map<Integer, Map<String, String>> table = builder.createTable(tree, mapping);
        return new Table(table);
    }

    private String getClassName(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().equals(Xml.class))
                .map(annotation -> ((Xml) annotation).name())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The name of this class: "
                        + clazz + " is absent for XML"));
    }

    @SneakyThrows
    private JsonNode createNodeTree(String file, String name) {
        XmlMapper mapper = mapperUtil.getXmlMapper();
        JsonNode mainNode = mapper.readTree(file);
        return mainNode.get(name);
    }
}
