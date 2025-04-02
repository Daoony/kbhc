package com.kbhc.exception;

import com.kbhc.constant.enums.ErrorCode;
import com.kbhc.constant.interfaces.ExceptionResult;
import lombok.Getter;

@Getter
public class ExceptionAlreadyExist extends RuntimeException implements ExceptionResult {

    final int errorCode;
    final String errorMessage;

    public ExceptionAlreadyExist(ErrorCode code) {
        this.errorCode = code.getCode();
        this.errorMessage = code.getMessage();
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
