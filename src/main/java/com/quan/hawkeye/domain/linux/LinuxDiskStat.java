package com.quan.hawkeye.domain.linux;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class LinuxDiskStat {

    /**
     * index 0
     * 主设备号码
     */
    @JsonIgnore
    private int major;

    /**
     * index 1
     * 次设备号码
     */
    @JsonIgnore
    private int minor;

    /**
     * index 2
     *
     * 设备名称
     */
    @JsonIgnore
    private String name;

    /**
     * index 3
     * number of read I/Os processed
     *
     * 读取成功 iops总
     */
    @JsonIgnore
    private long rd_ios;

    /**
     * index 4
     * number of read I/Os processed
     *
     * 为了效率可能会合并相邻的读和写。
     * 从而两次4K的读在它最终被处理到磁盘上之前可能会变成一次8K的读，才被计数（和排队），因此只有一次I/O操作。
     * 这个域使你知道这样的操作有多频繁。
     */
    @JsonIgnore
    private long rd_merges;

    /**
     * index 5
     * number of sectors read
     *
     * 读扇区大小，成功读过的扇区总次数。若是须要换算成KB等单位，须要除以2，1KB=2*512Bytes。512Bytes为1个扇区数。
     */
    @JsonIgnore
    private long rd_sectors;

    /**
     * index 6
     * total wait time for read requests
     *
     * 读花费的毫秒数，这是所有读操作所花费的毫秒数（用__make_request()到end_that_request_last()测量）
     */
    @JsonIgnore
    private long rd_ticks;

    /**
     * index 7
     * number of write I/Os processed
     *
     * 写入完成 iops总
     */
    @JsonIgnore
    private long wr_ios;

    /**
     * index 8
     * number of write I/Os merged with in-queue I/O
     *
     * 为了效率可能会合并相邻的读和写。
     * 从而两次4K的读在它最终被处理到磁盘上之前可能会变成一次8K的读，才被计数（和排队），因此只有一次I/O操作。
     * 这个域使你知道这样的操作有多频繁。
     */
    @JsonIgnore
    private long wr_merges;

    /**
     * index 9
     * number of sectors written
     *
     * 写扇区大小，成功写入的扇区总次数。若是须要换算成KB等单位，须要除以2，1KB=2*512Bytes。512Bytes为1个扇区数。
     */
    @JsonIgnore
    private long wr_sectors;

    /**
     * index 10
     * total wait time for write requests
     *
     * 写操作花费的毫秒数  ---  写花费的毫秒数，
     * 这是所有写操作所花费的毫秒数（用__make_request()到end_that_request_last()测量）。
     */
    @JsonIgnore
    private long wr_ticks;

    /**
     * index 11
     * number of I/Os currently in flight
     *
     * 正在处理的输入/输出请求数 -- -I/O的当前进度，只有这个域应该是0。
     * 当请求被交给适当的request_queue_t时增加和请求完成时减小。
     */
    @JsonIgnore
    private long in_flight;


    /**
     * index 12
     * total time this block device has been active
     *
     * 输入/输出操作花费的毫秒数  ----花在I/O操作上的毫秒数，
     * (io_ticks)该设备用于处理I/O的自然时间(wall-clock time)
     */
    @JsonIgnore
    private long io_ticks;

    /**
     * index 13
     * total wait time for all requests
     *
     * 输入/输出操作花费的加权毫秒数 -----  加权， 花在I/O操作上的毫秒数，
     * 在每次I/O开始，I/O结束，I/O合并时这个域都会增加。这可以给I/O完成时间和存储那些可以累积的提供一个便利的测量标准。
     * (time_in_queue): 对字段#10(io_ticks)的加权值
     */
    @JsonIgnore
    private long aveq;

    @JsonIgnore
    private static volatile Map<String, LinuxDiskStat> newStats;

    @JsonIgnore
    private static volatile Map<String, LinuxDiskStat> oldStats;

    private volatile IoStat ioStat;

//    todo 计算磁盘使用率
//    private Usage usage;

    private LinuxDiskStat(){}

    public static Map<String, LinuxDiskStat> deepCopyNewStats() {
        if (newStats == null) {
            synchronized (LinuxDiskStat.class) {
                if (newStats == null) {
                    newStats = new HashMap<>();
                }
            }
        }

        HashMap<String, LinuxDiskStat> copy = new HashMap<>();
        for (Map.Entry<String, LinuxDiskStat> entry : newStats.entrySet()) {
            copy.put(entry.getKey(), entry.getValue().clone());
        }

        return copy;
    }

    public LinuxDiskStat clone() {
        LinuxDiskStat copy = new LinuxDiskStat();
        copy.setMajor(this.getMajor());
        copy.setMinor(this.getMinor());
        copy.setName(this.getName());
        copy.setRd_ios(this.getRd_ios());
        copy.setRd_merges(this.getRd_merges());
        copy.setRd_sectors(this.getRd_sectors());
        copy.setRd_ticks(this.getRd_ticks());
        copy.setWr_ios(this.getWr_ios());
        copy.setWr_merges(this.getWr_merges());
        copy.setWr_sectors(this.getWr_sectors());
        copy.setWr_ticks(this.getWr_ticks());
        copy.setIn_flight(this.getIn_flight());
        copy.setIo_ticks(this.getIo_ticks());
        copy.setAveq(this.getAveq());
        IoStat ioStat = this.getIoStat();
        copy.setIoStat(ioStat.clone());
        return copy;
    }

    public static LinuxDiskStat getNewStat(String deviceName) {
        if (newStats == null) {
            synchronized (LinuxDiskStat.class) {
                if (newStats == null) {
                    newStats = new HashMap<>();
                }
            }
        }

        if (newStats.get(deviceName) == null) {
            synchronized (LinuxDiskStat.class) {
                if (newStats.get(deviceName) == null) {
                    newStats.put(deviceName, new LinuxDiskStat());
                }
            }
        }

        return newStats.get(deviceName);
    }

    public static LinuxDiskStat getOldStat(String deviceName) {
        if (oldStats == null) {
            synchronized (LinuxDiskStat.class) {
                if (oldStats == null) {
                    oldStats = new HashMap<>();
                }
            }
        }

        if (oldStats.get(deviceName) == null) {
            synchronized (LinuxDiskStat.class) {
                if (oldStats.get(deviceName) == null) {
                    oldStats.put(deviceName, new LinuxDiskStat());
                }
            }
        }
        return oldStats.get(deviceName);
    }

    public IoStat getIoStat() {
        if (ioStat == null) {
            synchronized (LinuxDiskStat.class) {
                if (ioStat == null) {
                    ioStat = new IoStat();
                }
            }
        }
        return ioStat;
    }

    public void setIoStat(IoStat ioStat) {
        this.ioStat = ioStat;
    }

    public boolean isEmpty() {
        return  (rd_ios | rd_merges | rd_sectors | rd_ticks |
                wr_ios | wr_merges | wr_sectors | wr_ticks |
                in_flight | io_ticks | aveq) == 0;
    }

    public void transferStat(LinuxDiskStat newStat) {
        this.setRd_ios(newStat.getRd_ios());
        this.setRd_merges(newStat.getRd_merges());
        this.setRd_sectors(newStat.getRd_sectors());
        this.setRd_ticks(newStat.getRd_ticks());
        this.setWr_ios(newStat.getWr_ios());
        this.setWr_merges(newStat.getWr_merges());
        this.setWr_sectors(newStat.getWr_sectors());
        this.setWr_ticks(newStat.getWr_ticks());
        this.setIn_flight(newStat.getIn_flight());
        this.setIo_ticks(newStat.getIo_ticks());
        this.setAveq(newStat.getAveq());
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRd_ios() {
        return rd_ios;
    }

    public void setRd_ios(long rd_ios) {
        this.rd_ios = rd_ios;
    }

    public long getRd_merges() {
        return rd_merges;
    }

    public void setRd_merges(long rd_merges) {
        this.rd_merges = rd_merges;
    }

    public long getRd_sectors() {
        return rd_sectors;
    }

    public void setRd_sectors(long rd_sectors) {
        this.rd_sectors = rd_sectors;
    }

    public long getRd_ticks() {
        return rd_ticks;
    }

    public void setRd_ticks(long rd_ticks) {
        this.rd_ticks = rd_ticks;
    }

    public long getWr_ios() {
        return wr_ios;
    }

    public void setWr_ios(long wr_ios) {
        this.wr_ios = wr_ios;
    }

    public long getWr_merges() {
        return wr_merges;
    }

    public void setWr_merges(long wr_merges) {
        this.wr_merges = wr_merges;
    }

    public long getWr_sectors() {
        return wr_sectors;
    }

    public void setWr_sectors(long wr_sectors) {
        this.wr_sectors = wr_sectors;
    }

    public long getWr_ticks() {
        return wr_ticks;
    }

    public void setWr_ticks(long wr_ticks) {
        this.wr_ticks = wr_ticks;
    }

    public long getIn_flight() {
        return in_flight;
    }

    public void setIn_flight(long in_flight) {
        this.in_flight = in_flight;
    }

    public long getIo_ticks() {
        return io_ticks;
    }

    public void setIo_ticks(long io_ticks) {
        this.io_ticks = io_ticks;
    }

    public long getAveq() {
        return aveq;
    }

    public void setAveq(long aveq) {
        this.aveq = aveq;
    }

    @Override
    public String toString() {
        return "LinuxDiskStat{" +
                "major=" + major +
                ", minor=" + minor +
                ", name='" + name + '\'' +
                ", rd_ios=" + rd_ios +
                ", rd_merges=" + rd_merges +
                ", rd_sectors=" + rd_sectors +
                ", rd_ticks=" + rd_ticks +
                ", wr_ios=" + wr_ios +
                ", wr_merges=" + wr_merges +
                ", wr_sectors=" + wr_sectors +
                ", wr_ticks=" + wr_ticks +
                ", in_flight=" + in_flight +
                ", io_ticks=" + io_ticks +
                ", aveq=" + aveq +
                ", ioStat=" + ioStat +
                '}';
    }

}

