package com.github.pinchflat.tomcat.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class ThreadNameCache extends LinkedHashMap<Integer,String> {

    private static final int THREAD_NAME_CACHE_SIZE = 10000;

    @Override
    protected boolean removeEldestEntry(Entry<Integer, String> eldest) {
        return (size() > THREAD_NAME_CACHE_SIZE);
    }

}
