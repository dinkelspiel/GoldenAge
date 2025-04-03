package dev.keii.goldenage.utils;

import java.util.Map;
import java.util.regex.Pattern;

public class StringSubstitutor {
    private final Map<String, Object> map;

    public StringSubstitutor(Map<String, Object> map) {
        this.map = map;
    }

    public String replace(String str) {
        String finalString  = str;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            finalString = finalString.replace("${" + entry.getKey() +  "}", String.valueOf(entry.getValue()));
        }
        return finalString;
    }
}