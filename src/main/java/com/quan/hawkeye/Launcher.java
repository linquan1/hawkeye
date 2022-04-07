package com.quan.hawkeye;

import java.lang.instrument.Instrumentation;

/**
 * @author quanlin
 */
public class Launcher {
    public static void premain(String args, Instrumentation inst) {
        System.out.println( "Hello World!" );
        HawkeyeEngine.HawkeyeEngineBuilder.builder().build().start();
    }
}
