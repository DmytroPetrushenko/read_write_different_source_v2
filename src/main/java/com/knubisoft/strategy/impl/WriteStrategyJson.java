package com.knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.WriteStrategy;
import com.knubisoft.utils.JacksonMapperUtil;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

public class WriteStrategyJson implements WriteStrategy {
    private final JacksonMapperUtil mapperUtil = new JacksonMapperUtil();

    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof FileReadWriteSource
                && FilenameUtils.getExtension(((FileReadWriteSource) data).getFile().getName())
                .equals("json");
    }

    @Override
    @SneakyThrows
    public <T> void writeDataToFile(DataReadWriteSource<?> data, List<T> list) {
        File file = ((FileReadWriteSource) data).getFile();
        ObjectMapper mapper = mapperUtil.getJsonMapper();
        mapper.writeValue(file, list);
    }
}
