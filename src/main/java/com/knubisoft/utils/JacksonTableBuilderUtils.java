package com.knubisoft.utils;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JacksonTableBuilderUtils {
    private static JacksonTableBuilderUtils builder;

    public static JacksonTableBuilderUtils getBuilder() {
        if (builder == null) {
            builder = new JacksonTableBuilderUtils();
        }
        return builder;
    }

    public List<String> createMapping(JsonNode jsonNode) {
        List<String> result = new ArrayList<>();
        Iterator<String> names = jsonNode.get(0).fieldNames();
        while (names.hasNext()) {
            result.add(names.next());
        }
        return result;
    }

    public Map<Integer, Map<String, String>> createTable(JsonNode jsonNode,
                                                         List<String> mapping) {
        return IntStream.range(0, jsonNode.size())
                .boxed()
                .collect(Collectors.toMap(num -> num,
                        num -> createRow(jsonNode.get(num), mapping)));
    }

    private Map<String, String> createRow(JsonNode jsonNode, List<String> mapping) {
        return IntStream.range(0, jsonNode.size())
                .boxed()
                .map(mapping::get)
                .collect(Collectors.toMap(name -> name, name -> jsonNode.get(name).asText()));
    }
}
