package com.knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.Table;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.ReadStrategy;
import com.knubisoft.utils.JacksonMapperUtil;
import com.knubisoft.utils.JacksonTableBuilderUtils;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

public class ReadStrategyJson implements ReadStrategy {
    private final JacksonTableBuilderUtils builder = JacksonTableBuilderUtils.getBuilder();
    private final JacksonMapperUtil mapperUtil = new JacksonMapperUtil();

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
        ObjectMapper mapper = mapperUtil.getJsonMapper();
        JsonNode jsonNode = mapper.readTree(file);
        List<String> mapping = builder.createMapping(jsonNode);
        return new Table(builder.createTable(jsonNode, mapping));
    }
}
