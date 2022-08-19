package com.knubisoft.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonMapperUtil {
    private static XmlMapper xmlMapper;
    private static ObjectMapper jsonMapper;

    private XmlMapper initXmlMapper() {
        XmlMapper newXmlMapper = new XmlMapper();
        newXmlMapper.registerModule(new JavaTimeModule());
        newXmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return newXmlMapper;
    }

    private ObjectMapper initJsonMapper() {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return jsonMapper;
    }

    public ObjectMapper getJsonMapper() {
        if (jsonMapper == null) {
            jsonMapper = initJsonMapper();
        }
        return jsonMapper;
    }

    public XmlMapper getXmlMapper() {
        if (xmlMapper == null) {
            xmlMapper = initXmlMapper();
        }
        return xmlMapper;
    }
}
