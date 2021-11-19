package com.alibaba.csp.sentinel.dashboard.service;

import java.io.*;

/**
 * @author jiaxm
 * @date 2021/4/16
 */
public class CustomPrintStream extends PrintStream {
    public CustomPrintStream(OutputStream out) {
        super(out);
    }

    public CustomPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public CustomPrintStream(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public CustomPrintStream(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public CustomPrintStream(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public CustomPrintStream(File file) throws FileNotFoundException {
        super(file);
    }

    public CustomPrintStream(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }
}
