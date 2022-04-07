package com.quan.hawkeye.domain.linux;

public class LinuxMemInfo {

    private long memTotal;

    private long memFree;

    private long memAvailable;

    private long buffers;

    private long cached;

    private long swapTotal;

    private long swapFree;

    private float avaRatio;

    private float freeRatio;

    public float getAvaRatio() {
        return avaRatio;
    }

    public void setAvaRatio(float avaRatio) {
        this.avaRatio = avaRatio;
    }

    public float getFreeRatio() {
        return freeRatio;
    }

    public void setFreeRatio(float freeRatio) {
        this.freeRatio = freeRatio;
    }

    public long getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(long memTotal) {
        this.memTotal = memTotal;
    }

    public long getMemFree() {
        return memFree;
    }

    public void setMemFree(long memFree) {
        this.memFree = memFree;
    }

    public long getMemAvailable() {
        return memAvailable;
    }

    public void setMemAvailable(long memAvailable) {
        this.memAvailable = memAvailable;
    }

    public long getBuffers() {
        return buffers;
    }

    public void setBuffers(long buffers) {
        this.buffers = buffers;
    }

    public long getCached() {
        return cached;
    }

    public void setCached(long cached) {
        this.cached = cached;
    }

    public long getSwapTotal() {
        return swapTotal;
    }

    public void setSwapTotal(long swapTotal) {
        this.swapTotal = swapTotal;
    }

    public long getSwapFree() {
        return swapFree;
    }

    public void setSwapFree(long swapFree) {
        this.swapFree = swapFree;
    }

    @Override
    public String toString() {
        return "LinuxMemInfo{" +
                "memTotal=" + memTotal +
                ", memFree=" + memFree +
                ", memAvailable=" + memAvailable +
                ", buffers=" + buffers +
                ", cached=" + cached +
                ", swapTotal=" + swapTotal +
                ", swapFree=" + swapFree +
                ", avaRatio=" + avaRatio +
                ", freeRatio=" + freeRatio +
                '}';
    }
}
