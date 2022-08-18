package com.knubisoft.dto.impl;

import com.knubisoft.dto.DataReadWriteSource;
import java.sql.ResultSet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SqlReadWriteSource implements DataReadWriteSource<ResultSet> {
    private ResultSet resultSet;

    @Override
    public ResultSet getContent() {
        return this.resultSet;
    }
}
