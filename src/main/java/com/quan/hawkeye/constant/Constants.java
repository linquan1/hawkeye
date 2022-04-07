package com.quan.hawkeye.constant;


public class Constants {

    public static class EventType {
        /**
         * the type of system metrics
         */
        public final static int SYSTEM = 1 << 1;

        /**
         * the type of jvm metrics
         */
        public final static int JVM = 1 << 2;

        /**
         * the type of net metrics
         */
        public final static int NET = 1 << 3;

        /**
         * thr type of route info
         */
        public final static int ROUTE = 1 << 4;

    }

    public static class LogReportName {
        public final static String SYSTEM = "hawkeye_system";

        public final static String JVM = "hawkeye_jvm_metrics";

        public final static String NET = "hawkeye_net_metrics";

        public final static String ROUTE = "hawkeye_route_info";

        public final static String[] ALL = new String[]{SYSTEM, JVM, NET, ROUTE};

    }


}
