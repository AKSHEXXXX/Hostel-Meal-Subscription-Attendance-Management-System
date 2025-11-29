package com.hostelms.util;

public class FileCorruptionException extends DataAccessException {
    public FileCorruptionException(String message) {
        super(message);
    }
}
