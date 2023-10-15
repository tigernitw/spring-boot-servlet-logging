package com.github.tigernitw.logging.servlet.response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CachedServletOutputStream extends ServletOutputStream {

    private final OutputStream cachedOutputStream;
    private final ByteArrayOutputStream copyStream;

    public CachedServletOutputStream(OutputStream outputStream) throws IOException {
        this.cachedOutputStream = outputStream;
        this.copyStream = new ByteArrayOutputStream(1024);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int b) throws IOException {
        cachedOutputStream.write(b);
        copyStream.write(b);
    }

    public byte[] getCopy() {
        return copyStream.toByteArray();
    }

}
