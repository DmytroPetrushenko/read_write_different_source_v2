package com.knubisoft.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Table {
    private Map<Integer, Map<String, String>> table;

    Map<String, String> getRowByIndex(int index) {
        Map<String, String> row = this.table.get(index);
        return row == null ? null : new LinkedHashMap<>(row);
    }
}
