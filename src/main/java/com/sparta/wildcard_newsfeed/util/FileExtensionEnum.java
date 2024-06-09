package com.sparta.wildcard_newsfeed.util;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum FileExtensionEnum {
    IMAGE("jpg", "jpeg", "png"),
    VIDEO("gif", "mp4", "avi");

    private final String[] extensions;

    FileExtensionEnum(String... extensions) {
        this.extensions = extensions;
    }

    public static List<String> getSupportedExtensions() {
        return Arrays.stream(values()).flatMap(ext -> Stream.of(ext.getExtensions())).toList();
    }

    public static String joiningAllExtensions() {
        return getSupportedExtensions().stream().collect(Collectors.joining(", ", "[", "]"));
    }
}