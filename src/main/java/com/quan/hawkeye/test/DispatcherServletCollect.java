package com.quan.hawkeye.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServletCollect {

    public static void begin(Object[] params) {
        HttpServletRequest request = (HttpServletRequest)params[0];
        HttpServletResponse response = (HttpServletResponse)params[1];
        System.out.println("======agent======");
        System.out.println(request.getRemoteAddr());
    }
}
