package com.quan.hawkeye.reportor;

import com.quan.hawkeye.constant.Constants;
import com.quan.hawkeye.publisher.MetricsEvent;
import com.quan.hawkeye.util.SimpleLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmax.disruptor.EventHandler;
import java.io.File;
import java.util.HashMap;
import java.util.logging.*;

public class LocalReportor implements EventHandler<MetricsEvent> {

    private static SimpleLogger logger = SimpleLogger.getLogger(LocalReportor.class);

    private static volatile LocalReportor reportor;

    private boolean available = true;

    private static ObjectMapper mapper = new ObjectMapper();

    private HashMap<String, Logger> reportLoggers;

    private LocalReportor() {
        init();
    }

    public static LocalReportor getReportor() {
        if (reportor == null) {
            synchronized (LocalReportor.class) {
                if (reportor == null) {
                    reportor = new LocalReportor();
                }
            }
        }
        return reportor;
    }

    @Override
    public void onEvent(MetricsEvent event, long sequence, boolean endOfBatch) {
        if (!available) {
            logger.warn("LocalReportor init failed.");
            return;
        }

        if (event.getValue() == null) {
            logger.error("event value is null.");
            return;
        }

        if (event.getEventType() == null) {
            logger.error("event type is null.");
            return;
        }

        Logger reportLogger;
        switch (event.getEventType()) {
            case Constants.EventType.SYSTEM:
                reportLogger = reportLoggers.get(Constants.LogReportName.SYSTEM);
                break;
            case Constants.EventType.JVM:
                reportLogger = reportLoggers.get(Constants.LogReportName.JVM);
                break;
            case Constants.EventType.NET:
                reportLogger = reportLoggers.get(Constants.LogReportName.NET);
                break;
            case Constants.EventType.ROUTE:
                reportLogger = reportLoggers.get(Constants.LogReportName.ROUTE);
                break;
            default:
                logger.error("unknown event type:" + event.getEventType());
                return;
        }

        try {
            String msg = mapper.writeValueAsString(event.getValue());
            reportLogger.info(msg);
        } catch (Exception e){
            logger.error("failed to report msg.", e);
        }
    }

    private static final String logFilePath = "/var/log/hawkey/%s/";
    private static final String logFileName = "/var/log/hawkey/%s/%s";

    private void init() {
        reportLoggers = new HashMap<>();
        try {
            for (String logName : Constants.LogReportName.ALL) {
                Logger reportLogger = Logger.getLogger(logName);
                File dir = new File(String.format(logFilePath, logName));
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                Handler handler = new FileHandler(String.format(logFileName, logName, logName) + ".log", 10*1000*1000, 10);
                handler.setFormatter(new LocalReportFormat());

                reportLogger.addHandler(handler);
                reportLogger.setLevel(Level.ALL);
                reportLogger.setUseParentHandlers(false);
                reportLoggers.put(logName, reportLogger);

            }
        } catch (Exception e) {
            available = false;
            logger.warn("failed to init reportor.", e);
        }
    }

}
