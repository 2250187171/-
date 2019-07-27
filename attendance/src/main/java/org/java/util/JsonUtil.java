package org.java.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @Author: 马果
 * @Date: 2019/7/3 11:47
 * @Description:
 */
public class JsonUtil {

    private static ObjectMapper objectMapper =new ObjectMapper();

    public static JsonNode  stringToJsonNode(String json){
        try {
            JsonNode node = objectMapper.readTree(json);
            return node;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> stringToList(String json){
        try {
            List list = objectMapper.readValue(json, List.class);
            return list;
        }catch (Exception ex){
            ex.getMessage();
        }
        return null;

    }
}
