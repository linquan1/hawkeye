package com.quan.hawkeye;

import com.quan.hawkeye.util.SimpleLogger;
import com.quan.hawkeye.util.SystemPropertyHelper;

public class HawkeyeConfig {
    private static SimpleLogger logger = SimpleLogger.getLogger(HawkeyeConfig.class);

    /**
     * the default collector work interval is 5000 millisecond
     */
    private static Long workInterval;

    private static Boolean hotLoading;

    private static Boolean baseResourceEnable;

    private static Boolean jvmMetricsEnable;

    private static Boolean netMetricsEnable;

    private static Boolean routeInfoEnable;

    static {
        workInterval = Math.max(500L,
                SystemPropertyHelper.getLong("com.quan.hawkeye.collecInterval", 5000L));
        hotLoading = SystemPropertyHelper.getBoolean("com.quan.hawkeye.hotLoading", false);
        baseResourceEnable = SystemPropertyHelper.getBoolean("com.quan.hawkeye.baseResourceEnable", true);
        jvmMetricsEnable = SystemPropertyHelper.getBoolean("com.quan.hawkeye.jvmMetricsEnable", true);
        netMetricsEnable = SystemPropertyHelper.getBoolean("com.quan.hawkeye.netMetricsEnable", true);
        routeInfoEnable = SystemPropertyHelper.getBoolean("com.quan.hawkeye.routeInfoEnable", true);

        logger.debug("-Dcom.quan.hawkeye.collecInterval: " + workInterval + System.lineSeparator() +
                "-Dcom.quan.hawkeye.hotLoading: " + hotLoading + System.lineSeparator() +
                "-Dcom.quan.hawkeye.baseResourceEnable: " + baseResourceEnable + System.lineSeparator() +
                "-Dcom.quan.hawkeye.jvmMetricsEnable: " + jvmMetricsEnable + System.lineSeparator() +
                "-Dcom.quan.hawkeye.netMetricsEnable: " + netMetricsEnable + System.lineSeparator() +
                "-Dcom.quan.hawkeye.routeInfoEnable: " + routeInfoEnable);
    }

    public static Long getWorkInterval() {
        return workInterval;
    }


    /**
     * todo 热生效配置
     */
    public void startHotLoading() {
        new Thread(() -> {

        }, "Thread-hawkeye-hotLoading").start();
    }

}
