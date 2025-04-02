package com.kbhc.constant.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    FILE_NOT_FOUND(1001, "FILE_NOT_FOUND"),

    EXCEPTION(9999, "UNDEFINED_EXCEPTION");

    final int code;
    final String message;

    ErrorCode(
        int code,
        String message
    ) {
        this.code = code;
        this.message = message;
    }
}