package com.github.tigernitw.logging.servlet.response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CachedHttpServletResponse extends HttpServletResponseWrapper {

    private CachedServletOutputStream cachedServletOutputStream;
    private PrintWriter writer;
    private ServletOutputStream outputStream;

    public CachedHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("CachedHttpServletResponse ::getOutputStream() has already been called on this response.");
        }
        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            cachedServletOutputStream = new CachedServletOutputStream(outputStream);
        }
        return cachedServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("CachedHttpServletResponse ::getWriter() has already been called on this response.");
        }
        if (writer == null) {
            cachedServletOutputStream = new CachedServletOutputStream(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(cachedServletOutputStream, getResponse().getCharacterEncoding()), true);
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            cachedServletOutputStream.flush();
        }
    }

    public byte[] getContent() {
        if (cachedServletOutputStream != null) {
            return cachedServletOutputStream.getCopy();
        } else {
            return new byte[0];
        }
    }

    public Map<String, String> getAllHeaders() {
        final Map<String, String> headers = new HashMap<>();
        getHeaderNames().forEach(key -> headers.put(key, getHeader(key)));
        return  headers;
    }
}
