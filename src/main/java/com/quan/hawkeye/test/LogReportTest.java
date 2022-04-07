package com.quan.hawkeye.test;

import com.quan.hawkeye.reportor.LocalReportFormat;
import java.io.File;
import java.util.logging.*;

public class LogReportTest {

    private static volatile LogReportTest reportor;

    private boolean available = true;


    private LogReportTest() {
        init();
    }

    public static void main(String[] args) {
        LogReportTest reportor = LogReportTest.getReportor();
        reportor.onEvent("hello word!!!");
        System.out.println("结束");
    }

    public static LogReportTest getReportor() {
        if (reportor == null) {
            synchronized (LogReportTest.class) {
                if (reportor == null) {
                    reportor = new LogReportTest();
                }
            }
        }
        return reportor;
    }

    public void onEvent(String s) {
        if (!available) {
            System.out.println("LocalReportor init failed.");
            return;
        }


        Logger reportLogger = Logger.getLogger("hawkeye_system");

        try {
            reportLogger.info(s);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("failed to report msg.");
        }
    }

    private static final String logFilePath = "/var/log/hawkey/%s/";
    private static final String logFileName = "/var/log/hawkey/%s/%s";

    private void init() {
        try {
            Logger logger = Logger.getLogger("hawkeye_system");
            File dir = new File(String.format(logFilePath, "hawkeye_system"));
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Handler handler = new FileHandler(String.format(logFileName, "hawkeye_system", "hawkeye_system") + "%u.log", 10*1000*1000, 10);
            handler.setFormatter(new LocalReportFormat());

            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            e.printStackTrace();
            available = false;
            System.out.println("failed to init reportor.");
        }
    }

}
