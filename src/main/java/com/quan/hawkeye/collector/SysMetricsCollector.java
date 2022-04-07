package com.quan.hawkeye.collector;

import com.quan.hawkeye.common.CustomThreadFactory;
import com.quan.hawkeye.constant.Constants;
import com.quan.hawkeye.HawkeyeConfig;
import com.quan.hawkeye.domain.linux.*;
import com.quan.hawkeye.publisher.EventPublisher;
import com.quan.hawkeye.publisher.MetricsEvent;
import com.quan.hawkeye.domain.SysMetrics;
import com.quan.hawkeye.util.SimpleLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SysMetricsCollector implements Collector{
    static SimpleLogger logger = SimpleLogger.getLogger(SysMetricsCollector.class);

    static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1,
            new CustomThreadFactory("sys-collector"));

    private AtomicBoolean started = new AtomicBoolean(false);

    private static volatile SysMetricsCollector collector;

    private SysMetricsCollector() {}

    public static SysMetricsCollector getCollector() {
        if (collector == null) {
            synchronized (SysMetricsCollector.class) {
                if (collector == null) {
                    collector = new SysMetricsCollector();
                }
            }
        }
        return collector;
    }


    private EventPublisher eventPublisher;

    public void init(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void start() {

        if (eventPublisher == null) {
            logger.warn("uninitialized, call init method before start.");
            return;
        }

        if (!started.compareAndSet(false, true)) {
            logger.warn("SysMetricsCollector already started");
            return;
        }
        doCollect();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopHook));
    }


    private void doCollect() {
        executor.schedule(() -> {
            try {
                String os = System.getProperty("os.name");
                SysMetrics metrics;
                if (os.toLowerCase().equals("linux")) {
                    LinuxSysMetrics linuxSysMetrics = new LinuxSysMetrics();
                    linuxSysMetrics.setMem(getLinuxMemInfo());
                    linuxSysMetrics.setCpu(getLinuxCpuStat());
                    linuxSysMetrics.setDisk(getLinuxDiskStat());
                    metrics = linuxSysMetrics;
                } else {
                    logger.warn("not supported system: " + os);
                    return;
                } // todo Adapt to other systems

                MetricsEvent<SysMetrics> event = new MetricsEvent<>(metrics, Constants.EventType.SYSTEM);
                eventPublisher.publish(event);
            } catch (Throwable t) {
                logger.warn("Collect system metrics failed.", t);
            }
            doCollect();
        }, HawkeyeConfig.getWorkInterval(), TimeUnit.MILLISECONDS);
    }

    public static int getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName(); // format: "pid@hostname"
        try {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * first
     */
    public static LinuxMemInfo getLinuxMemInfo() {
        BufferedReader in = null;
        try {
            String memInfoFile = "/proc/meminfo";
            in = new BufferedReader(new FileReader(memInfoFile));

            String line;
            long memTotal = 0, memFree = 0, memAvailable = 0, buffers = 0, cached = 0, swapTotal = 0, swapFree = 0;
            while((line=in.readLine()) != null){
                String[] fields = line.split("\\s+");

                if (fields[0].startsWith("Dirty")) {
                    break;
                }

                if(fields[0].startsWith("MemTotal")){
                    memTotal = Long.parseLong(fields[1]);
                }

                if(fields[0].startsWith("MemFree")){
                    memFree = Long.parseLong(fields[1]);
                }

                if(fields[0].startsWith("MemAvailable")){
                    memAvailable = Long.parseLong(fields[1]);
                }

                if(fields[0].startsWith("Buffers")){
                    buffers = Long.parseLong(fields[1]);
                }

                if(fields[0].startsWith("Cached")){
                    cached = Long.parseLong(fields[1]);
                }

                if(fields[0].startsWith("SwapTotal")){
                    swapTotal = Long.parseLong(fields[1]);
                }

                if(fields[0].startsWith("SwapFree")){
                    swapFree = Long.parseLong(fields[1]);
                }
            }
            LinuxMemInfo linuxMemInfo = new LinuxMemInfo();
            linuxMemInfo.setMemTotal(memTotal);
            linuxMemInfo.setMemFree(memFree);
            linuxMemInfo.setMemAvailable(memAvailable);
            linuxMemInfo.setBuffers(buffers);
            linuxMemInfo.setCached(cached);
            linuxMemInfo.setSwapTotal(swapTotal);
            linuxMemInfo.setSwapFree(swapFree);
            linuxMemInfo.setFreeRatio((float)memFree / (float)memTotal);
            linuxMemInfo.setAvaRatio((float)memAvailable / (float)memTotal);
            return linuxMemInfo;
        } catch (IOException e) {
           logger.error("failed to read meminfo.", e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                logger.warn("failed to close inputStream.", e);
            }
        }
    }


    /**
     * second
     */
    public static LinuxCpuStat getLinuxCpuStat() {
        BufferedReader sFileReader = null;
        BufferedReader psFileReader = null;
        BufferedReader cpuFileReader = null;
        LinuxCpuStat newStat = LinuxCpuStat.getNewStat();
        try {
            String sFile = "/proc/stat";
            String psFile = "/proc/%d/stat";

            int pid = getPid();
            if (pid == -1) {
                return null;
            }

            sFileReader = new BufferedReader(new FileReader(sFile));
            String line = sFileReader.readLine();

            long user, system, idle, iowait, total = 0;
            if (line != null && line.startsWith("cpu")){
                line = line.trim();
                String[] fields  = line.split("\\s+");

                user = Long.parseLong(fields [1]);
                system = Long.parseLong(fields [3]);
                idle = Long.parseLong(fields [4]);
                iowait = Long.parseLong(fields [5]);
                for (String field : fields) {
                    if (!field.equals("cpu")) {
                        total += Long.parseLong(field);
                    }
                }
                newStat.setUser(user);
                newStat.setSystem(system);
                newStat.setIdle(idle);
                newStat.setIowait(iowait);
                newStat.setTotal(total);
            } else {
                // todo record warn log
                return null;
            }

            psFileReader = new BufferedReader(new FileReader(String.format(psFile, pid)));
            line = psFileReader.readLine();
            if (line != null) {
                String[] fields = line.split("\\s+");

                if (fields.length >= 17) {
                    newStat.setuTime(Long.parseLong(fields[13]));
                    newStat.setsTime(Long.parseLong(fields[14]));
                    newStat.setCuTime(Long.parseLong(fields[15]));
                    newStat.setCsTime(Long.parseLong(fields[16]));
                }
            } else {
                // todo record warn log
                return null;
            }

            LinuxCpuStat oldStat = LinuxCpuStat.getOldStat();
            if (!oldStat.isEmpty()) {
                float systemCpuUsage = 1 - (float)(idle - oldStat.getIdle()) / (float)(total - oldStat.getTotal());
                float processCpuUsage = (float)(newStat.getTotalProcessTime() - oldStat.getTotalProcessTime())/(float)(total - oldStat.getTotal());
                // for iostat
                float USII = (user + system + idle + iowait) - (oldStat.getUser() + oldStat.getSystem() + oldStat.getIdle() + oldStat.getIowait());
                newStat.setUSII(USII);
                newStat.setSystemCpuUsage(systemCpuUsage);
                newStat.setProcessCpuUsage(processCpuUsage);
            }

            if (newStat.getNcpu() == 0 || newStat.getHZ() == 0) {
                String cpuFile = "/proc/cpuinfo";
                cpuFileReader = new BufferedReader(new FileReader(cpuFile));
                String cLine; int ncpu = 0; float HZ = 0;
                while ((cLine = cpuFileReader.readLine()) != null && !cLine.trim().equals("")) {
                    String[] fields = cLine.split("\\s+");

                    if ("MHz".equals(fields[1])) {
                        ncpu ++;
                        if (HZ == 0) {
                            HZ = Float.valueOf(fields[3]);
                        }
                    }
                }

                newStat.setNcpu(ncpu);
                newStat.setHZ(HZ);
            }

            oldStat.transferStat(newStat);
            return newStat;
        } catch (IOException e) {
            logger.error("failed to read cpu stat.", e);
            return null;
        } finally {
            try {
                if (sFileReader != null) {
                    sFileReader.close();
                }
                if (psFileReader != null) {
                    psFileReader.close();
                }
                if (cpuFileReader != null) {
                    cpuFileReader.close();
                }
            } catch (Exception e) {
                logger.warn("failed to close inputStream.", e);
            }
        }
    }

    /**
     * third
     */
    public static Map<String, LinuxDiskStat> getLinuxDiskStat() {
        Long interval = HawkeyeConfig.getWorkInterval();
        BufferedReader in = null;
        try {
            String memInfoFile = "/proc/diskstats";
            in = new BufferedReader(new FileReader(memInfoFile));

            String line;
            while ((line = in.readLine()) != null) {
                String[] fields = line.split("\\s+");

                if (fields.length >=15) {
                    LinuxDiskStat newStat = LinuxDiskStat.getNewStat(String.valueOf(fields[3]));
                    newStat.setMajor(Integer.valueOf(fields[1].equals("")?"0":fields[1]));
                    newStat.setMinor(Integer.valueOf(fields[2].equals("")?"0":fields[2]));
                    newStat.setName(String.valueOf(fields[3].equals("")?"0":fields[3]));
                    newStat.setRd_ios(Long.valueOf(fields[4].equals("")?"0":fields[4]));
                    newStat.setRd_merges(Long.valueOf(fields[5].equals("")?"0":fields[5]));
                    newStat.setRd_sectors(Long.valueOf(fields[6].equals("")?"0":fields[6]));
                    newStat.setRd_ticks(Long.valueOf(fields[7].equals("")?"0":fields[7]));
                    newStat.setWr_ios(Long.valueOf(fields[8].equals("")?"0":fields[8]));
                    newStat.setWr_merges(Long.valueOf(fields[9].equals("")?"0":fields[9]));
                    newStat.setWr_sectors(Long.valueOf(fields[10].equals("")?"0":fields[10]));
                    newStat.setWr_ticks(Long.valueOf(fields[11].equals("")?"0":fields[11]));
                    newStat.setIn_flight(Long.valueOf(fields[12].equals("")?"0":fields[12]));
                    newStat.setIo_ticks(Long.valueOf(fields[13].equals("")?"0":fields[13]));
                    newStat.setAveq(Long.valueOf(fields[14].equals("")?"0":fields[14]));

                    LinuxDiskStat oldStat = LinuxDiskStat.getOldStat(String.valueOf(fields[3]));
                    IoStat ioStat = newStat.getIoStat();
                    ioStat.setDevice(String.valueOf(fields[3]));
                    if (!oldStat.isEmpty()) {
                        LinuxCpuStat cpuStat = LinuxCpuStat.getNewStat();
                        float deltams = 1000.0f * cpuStat.getUSII() / cpuStat.getNcpu() / cpuStat.getHZ();

                        long rd_ios = newStat.getRd_ios() - oldStat.getRd_ios();
                        long wr_ios = newStat.getWr_ios() - oldStat.getWr_ios();
                        long rd_sectors = newStat.getRd_sectors() - oldStat.getRd_sectors();
                        long wr_sectors = newStat.getWr_sectors() - oldStat.getWr_sectors();
                        long rd_ticks = newStat.getRd_ticks() - oldStat.getRd_ticks();
                        long wr_ticks = newStat.getWr_ticks() - oldStat.getWr_ticks();
                        long aveq = newStat.getAveq() - oldStat.getAveq();
                        long io_ticks = newStat.getIo_ticks() - oldStat.getIo_ticks();

                        long n_ios  = rd_ios + wr_ios;
                        long n_ticks = rd_ticks + wr_ticks;
                        float n_kbytes = (float)(rd_sectors + wr_sectors) / 2.0f;
                        float queue = aveq / deltams;
                        float size = n_ios != 0 ? n_kbytes / n_ios : 0.0f;
                        float wait = n_ios != 0  ? (float)n_ticks / n_ios : 0.0f;
                        float svc_t = n_ios != 0  ? (float)io_ticks / n_ios : 0.0f;
                        float busy = 100.0f * (float)io_ticks / deltams;
                        if (busy > 100.0) busy = 100.0f;

                        ioStat.setRrqm_s(Float.valueOf(String.format("%.2f", (float)(newStat.getRd_merges() - oldStat.getRd_merges()) / interval)));
                        ioStat.setWrqm_s(Float.valueOf(String.format("%.2f", (float)(newStat.getWr_merges() - oldStat.getWr_merges()) / interval)));
                        ioStat.setR_s(Float.valueOf(String.format("%.2f", (float)rd_ios / interval)));
                        ioStat.setW_s(Float.valueOf(String.format("%.2f", (float)wr_ios / interval)));
                        ioStat.setRkB_s(Float.valueOf(String.format("%.2f", (float)rd_sectors / 2 / interval)));
                        ioStat.setWkB_s(Float.valueOf(String.format("%.2f", (float)wr_sectors / 2 / interval)));
                        ioStat.setR_await(rd_ios != 0 ? (float)rd_ticks / (float)rd_ios : 0.0f);
                        ioStat.setW_await(wr_ios != 0 ? (float)wr_ticks / (float)wr_ios : 0.0f);
                        ioStat.setAvgrq_sz(size);
                        ioStat.setAvgqu_sz(queue);
                        ioStat.setAwait(wait);
                        ioStat.setSvctm(svc_t);
                        ioStat.setUtil(busy);
                    }
                    oldStat.transferStat(newStat);
                }
            }

            return LinuxDiskStat.deepCopyNewStats();
        } catch (IOException e) {
            logger.error("failed to read disk stat.", e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                logger.warn("failed to close inputStream.", e);
            }
        }
    }

    private void stopHook() {
        stop();
    }

    public void stop() {
        executor.shutdown();
    }

}
