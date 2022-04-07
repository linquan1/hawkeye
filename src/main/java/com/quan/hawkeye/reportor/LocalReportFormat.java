package com.quan.hawkeye.reportor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LocalReportFormat extends Formatter {
    @Override
    public String format(LogRecord record) {
        String var3 = this.formatMessage(record);
        String var4 = "";
        if (record.getThrown() != null) {
            StringWriter var5 = new StringWriter();
            PrintWriter var6 = new PrintWriter(var5);
            var6.println();
            record.getThrown().printStackTrace(var6);
            var6.close();
            var4 = var5.toString();
        }
        return String.format("%1$s%2$s%n", var3, var4);
    }
}
