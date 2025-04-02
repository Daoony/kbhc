package com.kbhc.constant.enums;

import lombok.Getter;

@Getter
public enum ApiStatus {

    OK(200);

    final int code;

    ApiStatus(int code) {
        this.code = code;
    }
}