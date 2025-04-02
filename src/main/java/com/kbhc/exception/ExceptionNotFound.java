package com.kbhc.exception;

import com.kbhc.constant.enums.ErrorCode;
import com.kbhc.constant.interfaces.ExceptionResult;
import lombok.Getter;

@Getter
public class ExceptionNotFound extends RuntimeException implements ExceptionResult {

    final int errorCode;
    final String errorMessage;

    public ExceptionNotFound(ErrorCode code) {
        this.errorCode = code.getCode();
        this.errorMessage = code.getMessage();
    }

    public ExceptionNotFound(ErrorCode code, String errorMessage) {
        this.errorCode = code.getCode();
        this.errorMessage = code.getMessage() + (errorMessage.isEmpty() ? "" : "(" + errorMessage + ")");
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
