package com.github.pinchflat.tomcat.util;

import java.io.PrintWriter;
import java.io.Writer;

class IndentingPrintWriter extends PrintWriter {

    IndentingPrintWriter(Writer out) {
        super(out);
    }

    @Override
    public void println(Object x) {
        super.print('\t');
        super.println(x);
    }
}
