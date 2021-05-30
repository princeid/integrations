package com.prince.integrations.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtils {

    public static Collection<Object> castToCollection(String json){

        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to Map
        try {
            return mapper.readValue(json, new TypeReference<Collection<Object>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static Map<String,Object> castToMap(String json){

        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to Map
        try {
            return mapper.readValue(json, new TypeReference<Map<String,Object>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LinkedHashMap<>();
    }

    public static Object cast(String json){

        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to Map
        try {
            return mapper.readValue(json, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(String json, Class<?> typeT){

        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to Map
        try {
            return (T) mapper.readValue(json, typeT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T cast(String json, TypeReference typeReference){

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        // convert JSON string to Map
        try {
            return (T) mapper.readValue(json, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String castToJson(Object player) {

        if(player == null) return "";

        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to Map
        JsonNode node = mapper.valueToTree(player);

        return  node.toString();

    }
}
