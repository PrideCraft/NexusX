package com.notarin.pride_craft_network;

@SuppressWarnings("unused")
public class LogHandler {

    static void logInfo(String loggerName, String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isInfoEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).info(message);
        }
    }

    static void logWarn(String loggerName, String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isWarnEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).warn(message);
        }
    }

    public static void logError(String loggerName, String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isErrorEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).error(message);
        }
        System.exit(1);
    }

    static void logDebug(String loggerName, String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isDebugEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).debug(message);
        }
    }

}
