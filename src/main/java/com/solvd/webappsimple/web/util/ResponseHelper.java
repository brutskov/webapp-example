package com.solvd.webappsimple.web.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseHelper {

    public static void writeResponseObject(HttpServletResponse response, String message, int code) {
        response.setStatus(code);
        writeResponseObject(response, message);
    }

    public static void writeResponseObject(HttpServletResponse response, String message) {
        try {
            response.resetBuffer();
            response.getWriter().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
