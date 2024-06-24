package org.project.utils;

public class utils {
    public static String cleanFieldName(String field){
        field = field.toLowerCase();
        return field.replaceAll(" ", "");
    }
}
