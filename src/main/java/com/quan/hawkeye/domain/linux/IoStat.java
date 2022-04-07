package com.quan.hawkeye.domain.linux;

public class IoStat{

    private String device;

    /**
     * 每秒进行 merge 的读操作数目。即 rmerge/s
     */
    private float rrqm_s;

    /**
     * 每秒进行 merge 的写操作数目。即 wmerge/s
     */
    private float wrqm_s;

    /**
     * 每秒完成的读 I/O 设备次数。即 rio/s
     */
    private float r_s;

    /**
     * 每秒完成的写 I/O 设备次数。即 wio/s
     */
    private float w_s;

    /**
     * 每秒读K字节数。是 rsect/s 的一半，因为每扇区大小为512字节。
     */
    private float rkB_s;

    /**
     * 每秒写K字节数。是 wsect/s 的一半。
     */
    private float wkB_s;

    /**
     * 平均每次设备I/O操作的数据大小 (扇区)。即 (rsect+wsect)/(rio+wio)
     */
    private float avgrq_sz;

    /**
     * 平均I/O队列长度。即 aveq/1000 (因为aveq的单位为毫秒)。
     */
    private float avgqu_sz;

    /**
     * 平均每次设备I/O操作的等待时间 (毫秒)。即 (ruse+wuse)/(rio+wio)
     */
    private float await;

    /**
     * 平均每次设备读I/O操作的等待时间 (毫秒)。
     */
    private float r_await;

    /**
     * 平均每次设备写I/O操作的等待时间 (毫秒)。
     */
    private float w_await;

    /**
     *  平均每次设备I/O操作的服务时间 (毫秒)。即 use/(rio+wio)
     */
    private float svctm;

    /**
     * 一秒中有百分之多少的时间用于 I/O 操作，或者说一秒中有多少时间
     * I/O队列是非空的,即use/1000 (因为use的单位为毫秒),
     * 如果 %util 接近 100%，说明产生的I/O请求太多，I/O系统已经满负荷，该磁盘可能存在瓶颈。
     */
    private float util;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public float getRrqm_s() {
        return rrqm_s;
    }

    public void setRrqm_s(float rrqm_s) {
        this.rrqm_s = rrqm_s;
    }

    public float getWrqm_s() {
        return wrqm_s;
    }

    public void setWrqm_s(float wrqm_s) {
        this.wrqm_s = wrqm_s;
    }

    public float getR_s() {
        return r_s;
    }

    public void setR_s(float r_s) {
        this.r_s = r_s;
    }

    public float getW_s() {
        return w_s;
    }

    public void setW_s(float w_s) {
        this.w_s = w_s;
    }

    public float getRkB_s() {
        return rkB_s;
    }

    public void setRkB_s(float rkB_s) {
        this.rkB_s = rkB_s;
    }

    public float getWkB_s() {
        return wkB_s;
    }

    public void setWkB_s(float wkB_s) {
        this.wkB_s = wkB_s;
    }

    public float getAvgrq_sz() {
        return avgrq_sz;
    }

    public void setAvgrq_sz(float avgrq_sz) {
        this.avgrq_sz = avgrq_sz;
    }

    public float getAvgqu_sz() {
        return avgqu_sz;
    }

    public void setAvgqu_sz(float avgqu_sz) {
        this.avgqu_sz = avgqu_sz;
    }

    public float getAwait() {
        return await;
    }

    public void setAwait(float await) {
        this.await = await;
    }

    public float getR_await() {
        return r_await;
    }

    public void setR_await(float r_await) {
        this.r_await = r_await;
    }

    public float getW_await() {
        return w_await;
    }

    public void setW_await(float w_await) {
        this.w_await = w_await;
    }

    public float getSvctm() {
        return svctm;
    }

    public void setSvctm(float svctm) {
        this.svctm = svctm;
    }

    public float getUtil() {
        return util;
    }

    public void setUtil(float util) {
        this.util = util;
    }

    public IoStat clone() {
        IoStat ioStat = new IoStat();
        ioStat.setDevice(this.getDevice());
        ioStat.setRrqm_s(this.getRrqm_s());
        ioStat.setWrqm_s(this.getWrqm_s());
        ioStat.setR_s(this.getR_s());
        ioStat.setW_s(this.getW_s());
        ioStat.setRkB_s(this.getRkB_s());
        ioStat.setWkB_s(this.getWkB_s());
        ioStat.setAvgrq_sz(this.getAvgrq_sz());
        ioStat.setAvgqu_sz(this.getAvgqu_sz());
        ioStat.setAwait(this.getAwait());
        ioStat.setR_await(this.getR_await());
        ioStat.setW_await(this.getW_await());
        ioStat.setSvctm(this.getSvctm());
        ioStat.setUtil(this.getUtil());
        return ioStat;
    }

    @Override
    public String toString() {
        return "IoStat{" +
                "device='" + device + '\'' +
                ", rrqm_s=" + rrqm_s +
                ", wrqm_s=" + wrqm_s +
                ", r_s=" + r_s +
                ", w_s=" + w_s +
                ", rkB_s=" + rkB_s +
                ", wkB_s=" + wkB_s +
                ", avgrq_sz=" + avgrq_sz +
                ", avgqu_sz=" + avgqu_sz +
                ", await=" + await +
                ", r_await=" + r_await +
                ", w_await=" + w_await +
                ", svctm=" + svctm +
                ", util=" + util +
                '}';
    }
}