/*
 * Copyright (c) 2010-2013 the original author or authors
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.quan.hawkeye.util;

import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * todo 优化日志框架使用
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SimpleLogger {

    public static SimpleLogger getLogger(String name) {
        return new SimpleLogger(name);
    }

    public static SimpleLogger getLogger(Class klass) { return new SimpleLogger(klass.getName()); }
    private final String name;

    public static PrintStream out;
    private static final Level default_level = Level.INFO;

    static {
        if ("stderr".equalsIgnoreCase(System.getProperty(SimpleLogger.class.getName() + ".console"))) {
            SimpleLogger.out = System.err;
        } else {
            SimpleLogger.out = System.out;
        }
    }

    public static Level level = Level.INFO;

    public static Level parseLevel(String level, Level defaultValue) {

        Map<String, Level> julLevelsByName = new HashMap<String, Level>() {
            {
                put("TRACE", Level.FINEST);
                put("FINEST", Level.FINEST);
                put("FINER", Level.FINER);
                put("FINE", Level.FINE);
                put("DEBUG", Level.FINE);
                put("INFO", Level.INFO);
                put("WARNING", Level.WARNING);
                put("WARN", Level.WARNING);
                put("SEVERE", Level.SEVERE);
                put("ERROR", Level.SEVERE);
                put("ALL", Level.ALL);

            }
        };

        for(Map.Entry<String, Level> entry: julLevelsByName.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(level))
                return entry.getValue();
        }
        return defaultValue;
    }

    static {
        String level = System.getProperty(SimpleLogger.class.getName() + ".level", "INFO");
        System.out.println(SimpleLogger.class.getName() + ".level is " + level);
        SimpleLogger.level = parseLevel(level, default_level);
    }

    public SimpleLogger(String name) {
        this.name = name;
    }

    public void log(Level level, String msg) {
        log(level, msg, null);
    }

    public void log(Level level, String msg, Throwable thrown) {
        if (!isLoggable(level)) {
            return;
        }
        SimpleLogger.out.println(new Timestamp(System.currentTimeMillis()) + " " + level + " [" + Thread.currentThread().getName() + "] " + name + " - " + msg);
        if (thrown != null) {
            thrown.printStackTrace(SimpleLogger.out);
        }
    }

    public void finest(String msg) {
        log(Level.FINEST, msg);
    }

    public void finer(String msg) {
        log(Level.FINER, msg);
    }

    public void fine(String msg) {
        log(Level.FINE, msg);
    }

    public void info(String msg) {
        log(Level.INFO, msg);
    }

    public void warning(String msg) {
        log(Level.WARNING, msg);
    }

    public void debug(String msg) { fine(msg); }

    public void warn(String msg) { warning(msg); }

    public void warn(String msg, Throwable thrown){
        log(Level.WARNING, msg, thrown);
    }

    public void error(String msg) { log(Level.SEVERE, msg); }

    public void error(String msg, Throwable thrown){
        log(Level.SEVERE, msg, thrown);
    }

    public boolean isLoggable(Level level) {
        if (level.intValue() < this.level.intValue()) {
            return false;
        }
        return true;
    }
}