package com.github.pinchflat.tomcat.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;

public class ThreadNameLoader {
    private static final String UNKNOWN_THREAD_NAME = "Unknown thread with ID ";
    private static final Object threadMxBeanLock = new Object();
    private static volatile ThreadMXBean threadMxBean = null;

    private ThreadLocal<ThreadNameCache> threadNameCache = new ThreadLocal<ThreadNameCache>() {
        @Override
        protected ThreadNameCache initialValue() {
            return new ThreadNameCache();
        }
    };

    public String populateNameById(final int threadId){
        if (Thread.currentThread().getId() == threadId) {
            return Thread.currentThread().getName();
        } else {
            return getThreadName(threadId);
        }
    }

    /**
     * LogRecord has threadID but no thread name.
     * LogRecord uses an int for thread ID but thread IDs are longs.
     * If the real thread ID > (Integer.MAXVALUE / 2) LogRecord uses it's own
     * ID in an effort to avoid clashes due to overflow.
     */
    private String getThreadName(final int logRecordThreadId) {
        Map<Integer,String> cache = threadNameCache.get();
        String result = null;

        if (logRecordThreadId > (Integer.MAX_VALUE / 2)) {
            result = cache.get(Integer.valueOf(logRecordThreadId));
        }

        if (result != null) {
            return result;
        }

        if (logRecordThreadId > Integer.MAX_VALUE / 2) {
            result = UNKNOWN_THREAD_NAME + logRecordThreadId;
        } else {
            // Double checked locking OK as threadMxBean is volatile
            if (threadMxBean == null) {
                synchronized (threadMxBeanLock) {
                    if (threadMxBean == null) {
                        threadMxBean = ManagementFactory.getThreadMXBean();
                    }
                }
            }
            ThreadInfo threadInfo = threadMxBean.getThreadInfo(logRecordThreadId);
            if (threadInfo == null) {
                return Long.toString(logRecordThreadId);
            }
            result = threadInfo.getThreadName();
        }

        cache.put(Integer.valueOf(logRecordThreadId), result);

        return result;
    }
}
