package com.ltyc.sms.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/16
 */
public class LoggerNameBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent> {
    private static final String KEY = "loggerName";
    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public void setKey() {
        throw new UnsupportedOperationException("Key not settable. Using " + KEY);
    }

    @Override
    public String getDiscriminatingValue(ILoggingEvent e) {
        String loggerName = e.getLoggerName();

        if (loggerName == null) {
            return defaultValue;
        }

        return loggerName;
    }
}
