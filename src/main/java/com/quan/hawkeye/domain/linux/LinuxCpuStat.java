package com.quan.hawkeye.domain.linux;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class LinuxCpuStat {

    private int ncpu;

    private float HZ;

    private long user;

    private long system;

    private long idle;

    private long iowait;

    private long total;

    private long uTime;

    private long sTime;

    private long cuTime;

    private long csTime;

    private float systemCpuUsage;

    private float processCpuUsage;

    @JsonIgnore
    private float USII;

    @JsonIgnore
    private static volatile LinuxCpuStat newStat;

    @JsonIgnore
    private static volatile LinuxCpuStat oldStat;

    private LinuxCpuStat(){}

    public static LinuxCpuStat getNewStat() {
        if (newStat == null) {
            synchronized (LinuxCpuStat.class) {
                if (newStat == null) {
                    newStat = new LinuxCpuStat();
                }
            }
        }
        return newStat;
    }

    public static LinuxCpuStat getOldStat() {
        if (oldStat == null) {
            synchronized (LinuxCpuStat.class) {
                if (oldStat == null) {
                    oldStat = new LinuxCpuStat();
                }
            }
        }
        return oldStat;
    }

    public int getNcpu() {
        return ncpu;
    }

    public void setNcpu(int ncpu) {
        this.ncpu = ncpu;
    }

    public float getHZ() {
        return HZ;
    }

    public void setHZ(float HZ) {
        this.HZ = HZ;
    }

    public float getUSII() {
        return USII;
    }

    public void setUSII(float USII) {
        this.USII = USII;
    }

    public float getSystemCpuUsage() {
        return systemCpuUsage;
    }

    public void setSystemCpuUsage(float systemCpuUsage) {
        this.systemCpuUsage = systemCpuUsage;
    }

    public float getProcessCpuUsage() {
        return processCpuUsage;
    }

    public void setProcessCpuUsage(float processCpuUsage) {
        this.processCpuUsage = processCpuUsage;
    }

    public static void setOldStat(LinuxCpuStat lastStat) {
        oldStat = lastStat;
    }


    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getSystem() {
        return system;
    }

    public void setSystem(long system) {
        this.system = system;
    }

    public long getIowait() {
        return iowait;
    }

    public void setIowait(long iowait) {
        this.iowait = iowait;
    }

    public long getIdle() {
        return idle;
    }

    public void setIdle(long idle) {
        this.idle = idle;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getuTime() {
        return uTime;
    }

    public void setuTime(long uTime) {
        this.uTime = uTime;
    }

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public long getCuTime() {
        return cuTime;
    }

    public void setCuTime(long cuTime) {
        this.cuTime = cuTime;
    }

    public long getCsTime() {
        return csTime;
    }

    public void setCsTime(long csTime) {
        this.csTime = csTime;
    }

    public long getTotalProcessTime() {
        return uTime + sTime + cuTime + csTime;
    }

    public boolean isEmpty(){
        return (user | system | idle | iowait | total | uTime | sTime | cuTime | csTime) == 0;
    }

    public void transferStat(LinuxCpuStat newStat) {
        this.setUser(newStat.getUser());
        this.setSystem(newStat.getSystem());
        this.setIdle(newStat.getIdle());
        this.setIowait(newStat.getIowait());
        this.setTotal(newStat.getTotal());
        this.setuTime(newStat.getuTime());
        this.setsTime(newStat.getsTime());
        this.setCuTime(newStat.getCuTime());
        this.setCsTime(newStat.getCsTime());
    }

    @Override
    public String toString() {
        return "LinuxCpuStat{" +
                "user=" + user +
                ", system=" + system +
                ", idle=" + idle +
                ", iowait=" + iowait +
                ", total=" + total +
                ", uTime=" + uTime +
                ", sTime=" + sTime +
                ", cuTime=" + cuTime +
                ", csTime=" + csTime +
                ", systemCpuUsage=" + systemCpuUsage +
                ", processCpuUsage=" + processCpuUsage +
                ", totalProcessTime=" + getTotalProcessTime() +
                '}';
    }
}