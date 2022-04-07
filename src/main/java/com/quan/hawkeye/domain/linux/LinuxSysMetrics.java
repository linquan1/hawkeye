package com.quan.hawkeye.domain.linux;

import com.quan.hawkeye.domain.SysMetrics;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LinuxSysMetrics extends SysMetrics {

    private LinuxCpuStat cpu;

    private LinuxMemInfo mem;

    private Map<String, LinuxDiskStat> disk;

    public LinuxCpuStat getCpu() {
        return cpu;
    }

    public void setCpu(LinuxCpuStat cpu) {
        this.cpu = cpu;
    }

    public LinuxMemInfo getMem() {
        return mem;
    }

    public void setMem(LinuxMemInfo mem) {
        this.mem = mem;
    }

    public Map<String, LinuxDiskStat> getDisk() {
        return disk;
    }

    public void setDisk(Map<String, LinuxDiskStat> disk) {
        this.disk = disk;
    }
}
