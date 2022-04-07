package com.quan.hawkeye;

import com.quan.hawkeye.collector.SysMetricsCollector;
import com.quan.hawkeye.publisher.EventPublisher;
import com.quan.hawkeye.publisher.MetricsEventPublisher;
import com.quan.hawkeye.reportor.LocalReportor;
import com.quan.hawkeye.publisher.MetricsEvent;
import com.quan.hawkeye.test.MetricsEventFactory;
import com.quan.hawkeye.util.SimpleLogger;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class HawkeyeEngine {

    private static SimpleLogger logger = SimpleLogger.getLogger(HawkeyeEngine.class);

    private EventPublisher publisher;

    private static volatile HawkeyeEngine engine;

    /**
     * flag of started
     */
    private static AtomicBoolean started = new AtomicBoolean(false);

    private HawkeyeEngine(){}

    private static HawkeyeEngine getEngine() {
        if (engine == null) {
            synchronized (HawkeyeEngine.class) {
                if (engine == null) {
                    engine = new HawkeyeEngine();
                }
            }
        }
        return engine;
    }


    public void start() {
        logger.info("================ Hawkeye Engine Starting ================");
        if (!started.compareAndSet(false,true)) {
            logger.warn("HawkeyeEngine already started.");
            return;
        }
        publisher = MetricsEventPublisher.getPublisher();

        // todo 根据配置开启采集器
        ExecutorService executor = Executors.newCachedThreadPool();
        EventFactory<MetricsEvent> eventFactory = new MetricsEventFactory();
        int ringBufferSize = 1024 * 1024;
        Disruptor<MetricsEvent> localReportDisruptor = new Disruptor<>(eventFactory, ringBufferSize, executor,
                ProducerType.SINGLE, new SleepingWaitStrategy());

        localReportDisruptor.handleEventsWith(LocalReportor.getReportor());
        publisher.register(localReportDisruptor.start());

        SysMetricsCollector sysMetricsCollector = SysMetricsCollector.getCollector();
        sysMetricsCollector.init(publisher);
        sysMetricsCollector.start();
        logger.info("================ Hawkeye Engine Start Success ================");
    }



    public static class HawkeyeEngineBuilder{

        private static final HawkeyeEngineBuilder hawkeyeEngineBuilder = new HawkeyeEngineBuilder();

        private HawkeyeEngineBuilder(){}

        public static HawkeyeEngineBuilder builder(){
            return  hawkeyeEngineBuilder;
        }


        public HawkeyeEngine build(){
            return HawkeyeEngine.getEngine();
        }
    }




}
