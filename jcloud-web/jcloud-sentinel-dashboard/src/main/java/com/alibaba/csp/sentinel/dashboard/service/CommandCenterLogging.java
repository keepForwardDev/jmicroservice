package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.log.LogTarget;
import com.alibaba.csp.sentinel.log.jul.JavaLoggingAdapter;
import com.alibaba.csp.sentinel.transport.log.CommandCenterLog;

/**
 * use HttpEventTask
 * @author jiaxm
 * @date 2021/4/16
 */
@LogTarget(value = CommandCenterLog.LOGGER_NAME)
public class CommandCenterLogging extends JavaLoggingAdapter {

    public CommandCenterLogging() {
        super(CommandCenterLog.LOGGER_NAME, CommandCenterLog.DEFAULT_LOG_FILENAME);
    }

    public CommandCenterLogging(String loggerName, String fileNamePattern) {
        super(loggerName, fileNamePattern);
    }

    @Override
    public void info(String msg, Throwable e) {
        super.info(msg, e);
    }

    @Override
    public void info(String format, Object... arguments) {
        // do nothing
        //super.info(format, arguments);
    }
}
