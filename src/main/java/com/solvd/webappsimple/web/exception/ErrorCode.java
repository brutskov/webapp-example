package com.solvd.webappsimple.web.exception;

import javax.servlet.http.HttpServletResponse;

public enum ErrorCode {

    BAD_REQUEST("Data is malformed", HttpServletResponse.SC_BAD_REQUEST, IllegalArgumentException.class),
    INTERNAL_SERVER_ERROR("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Exception.class);

    private final String message;
    private final int code;
    private final Class<? extends Throwable>[] exceptionClasses;

    ErrorCode(String message, int code, Class<? extends Throwable>... exceptionClasses) {
        this.message = message;
        this.code = code;
        this.exceptionClasses = exceptionClasses;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Class<? extends Throwable>[] getExceptionClasses() {
        return exceptionClasses;
    }

}
