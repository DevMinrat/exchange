package com.devminrat.exchange.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class ResponseUtil {
    private ResponseUtil() {
    }

    public static void writeBadRequestResponse(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("{\"error\":\"Bad Request.\"}");
    }

    public static void writeBadRequestResponse(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("{\"error\":\"Bad Request." + msg + "\"}");
    }

    public static void writeInternalServerErrorResponse(Exception e, HttpServletResponse resp) throws IOException {
        e.printStackTrace();
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().write("{\"error\":\"Internal Server Error\"}");
    }

    public static void writeInternalServerErrorResponse(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().write("{\"error\":\"Internal Server Error\"}");
    }

    public static void writeNotFoundResponse(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write("{\"error\":\"" + msg + "\"}");
    }

    public static void writeConflictResponse(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
        resp.getWriter().write("{\"error\":\"" + msg + "\"}");
    }

    public static void writeCreatedResponse(HttpServletResponse resp, String json) throws IOException {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(json);
    }

}
