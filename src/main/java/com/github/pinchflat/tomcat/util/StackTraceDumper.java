package com.github.pinchflat.tomcat.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceDumper {

    public String dump(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new IndentingPrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.close();
        return sw.getBuffer().toString();
    }

}
