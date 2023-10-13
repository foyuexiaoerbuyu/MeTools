package com.other;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.yifan.hao.FileUtils;


import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils {

    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> List<T> fromJsonToList(String json, Class<T> elementClass) {
        Type listType = TypeToken.getParameterized(List.class, elementClass).getType();
        return new Gson().fromJson(json, listType);
    }

    public static <T> String toJson(T obj) {
        return new Gson().toJson(obj);
    }


    public static JsonArray getJsonArray(JsonObject asJsonObject, String key) {
        if (!asJsonObject.get(key).isJsonNull()) {
            return asJsonObject.get(key).getAsJsonArray();
        }
        return new JsonArray();
    }

    public static String getString(JsonObject asJsonObject, String id) {
        if (!asJsonObject.get(id).isJsonNull()) {
            return asJsonObject.get(id).getAsString();
        }
        return "";
    }

    public static void putJoStrToFile(String filePath, String joStr) {
        if (FileUtils.exists(filePath)) {
            FileUtils.createDir(filePath);
            FileUtils.createFile(filePath);
        }
        FileUtils.writeFile(filePath, joStr);
    }

    public static <T> T getJoStrForFile(String filePath, Class<T> clazz) {
        String content = FileUtils.readFile(filePath);
        if (content != null) {
            return fromJson(content, clazz);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String json = "[{\"name\":\"Alice\",\"age\":20},{\"name\":\"Bob\",\"age\":25}]";
//        List<Person> persons = GsonUtils.fromJsonToList(json, Person.class);
//        System.out.println(persons);
    }

}
