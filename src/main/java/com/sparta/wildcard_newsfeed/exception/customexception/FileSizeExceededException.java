package com.sparta.wildcard_newsfeed.exception.customexception;

public class FileSizeExceededException extends RuntimeException {

    public FileSizeExceededException(String fileName, String extension, long currentSize, long maxSize) {
        super(createMessage(fileName, extension, currentSize, maxSize));
    }

    public static String createMessage(String fileName, String extension, long currentSize, long maxSize) {
        long decimal = currentSize % (1024 * 1024);
        long integerPart = currentSize / (1024 * 1024);
        maxSize /= (1024 * 1024);
        double decimalPart = (double) decimal / (1024 * 1024);
        double combinedSize = integerPart + decimalPart;
        double roundedSize = Math.round(combinedSize * 100.0) / 100.0;
        return String.format("%s의 확장자는 최대 %dMB까지 저장가능합니다. 문제 생긴 파일: %s(%.2fMB)",
                extension, maxSize, fileName, roundedSize);
    }
}