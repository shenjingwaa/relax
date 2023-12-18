package com.relax.relax.common.utils;

public class RegexUtil {
    public static String camelCaseToUnderscore(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
