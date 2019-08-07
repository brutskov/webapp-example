package com.solvd.webappsimple.web.exception;

import com.solvd.webappsimple.web.util.ResponseHelper;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class ExceptionResolver {

    private static final ErrorCode[] ERROR_CODES = new ErrorCode[] {
            ErrorCode.BAD_REQUEST,
            ErrorCode.INTERNAL_SERVER_ERROR
    };

    public static void handle(Throwable e, HttpServletResponse response) {
        Arrays.stream(ERROR_CODES)
              .filter(ec -> hasExceptionClass(ec, e.getClass()))
              .findFirst()
              .ifPresent(errorCode -> ResponseHelper.writeResponseObject(response, errorCode.getMessage(), errorCode.getCode()));
    }

    private static boolean hasExceptionClass(ErrorCode errorCode, Class<? extends Throwable> exClass) {
        return Arrays.stream(errorCode.getExceptionClasses()).anyMatch(cls -> cls.isAssignableFrom(exClass));
    }

}
