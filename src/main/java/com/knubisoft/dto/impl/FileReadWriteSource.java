package com.knubisoft.dto.impl;

import com.knubisoft.dto.DataReadWriteSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

@AllArgsConstructor
@Getter
public class FileReadWriteSource implements DataReadWriteSource<String> {
    private File file;

    @Override
    @SneakyThrows
    public String getContent() {
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}
