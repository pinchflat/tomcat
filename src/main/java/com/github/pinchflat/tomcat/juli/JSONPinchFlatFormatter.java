package com.github.pinchflat.tomcat.juli;


import com.github.pinchflat.tomcat.util.JsonDateTimeFormatter;
import com.github.pinchflat.tomcat.util.StackTraceDumper;
import com.github.pinchflat.tomcat.util.ThreadNameLoader;
import com.jsoniter.output.JsonStream;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * JSON Formatter for tomcat JUL Loggers.
 * Provides JSON format for catalina.out.
 *
 * @see Formatter
 */
public class JSONPinchFlatFormatter extends Formatter {

    private static final String LABEL = "label";
    private static final String TOMCAT = "tomcat";
    private static final String TIMESTAMP = "timestamp";
    private static final String LOGGER = "logger";
    private static final String LEVEL = "level";
    private static final String CLASS = "class";
    private static final String METHOD = "method";
    private static final String MESSAGE = "message";
    private static final String EXCEPTION = "exception";
    private static final String THREAD = "thread";

    private final JsonDateTimeFormatter dateTimeFormatter;
    private final StackTraceDumper stackTraceDumper;
    private final ThreadNameLoader threadNameLoader;

    public JSONPinchFlatFormatter() {
        this.dateTimeFormatter = new JsonDateTimeFormatter();
        this.stackTraceDumper = new StackTraceDumper();
        this.threadNameLoader = new ThreadNameLoader();
    }

    @Override
    public String format(final LogRecord logRecord) {
        final Map<String,String> logData = new LinkedHashMap<>();
        logData.put(LABEL, TOMCAT);
        logData.put(TIMESTAMP, dateTimeFormatter.format(logRecord.getMillis()));
        logData.put(LOGGER, logRecord.getLoggerName());
        logData.put(LEVEL, logRecord.getLevel().getLocalizedName());
        logData.put(CLASS, logRecord.getSourceClassName());
        logData.put(METHOD, logRecord.getSourceMethodName());
        logData.put(MESSAGE, formatMessage(logRecord));
        logData.put(THREAD, threadNameLoader.populateNameById(logRecord.getThreadID()));

        if (logRecord.getThrown() != null) {
            logData.put(EXCEPTION, stackTraceDumper.dump(logRecord.getThrown()));
        }
        return JsonStream.serialize(logData)+ System.lineSeparator();
    }

}
