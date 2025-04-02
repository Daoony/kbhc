package com.kbhc.constant.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Unit {

    KM("km", "DISTANCE"),
    KCAL("kcal", "CALORIES");

    private final String code;
    private final String type;

    Unit(String code, String type) {
        this.code = code;
        this.type = type;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static Unit fromCode(String code) {
        for (Unit unit : Unit.values()) {
            if (unit.code.equalsIgnoreCase(code)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unknown unit code: " + code);
    }
}
