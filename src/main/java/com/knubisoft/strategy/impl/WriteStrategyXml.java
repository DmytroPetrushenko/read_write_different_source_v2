package com.knubisoft.strategy.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.knubisoft.dto.DataReadWriteSource;
import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.strategy.WriteStrategy;
import com.knubisoft.utils.JacksonMapperUtil;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

public class WriteStrategyXml implements WriteStrategy {
    private final JacksonMapperUtil mapperUtil = new JacksonMapperUtil();

    @Override
    public boolean isApplied(DataReadWriteSource<?> data) {
        return data instanceof FileReadWriteSource
                && FilenameUtils.getExtension(((FileReadWriteSource) data).getFile().getName())
                .equals("xml");
    }

    @Override
    @SneakyThrows
    public <T> void writeDataToFile(DataReadWriteSource<?> data, List<T> list) {
        File file = ((FileReadWriteSource) data).getFile();
        XmlMapper mapper = mapperUtil.getXmlMapper();
        mapper.writeValue(file, list);
    }
}
