package com.notarin.pride_craft_network;

/**
 * A class that contains methods to log messages.
 */
@SuppressWarnings("unused")
public class LogHandler {

    static void logInfo(final String loggerName, final String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isInfoEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).info(message);
        }
    }

    /**
     * Logs a warning message.
     *
     * @param loggerName The name of the logger
     * @param message The message to log
     */
    public static void logWarn(final String loggerName, final String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isWarnEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).warn(message);
        }
    }

    /**
     * Logs an error message, and exits the program.
     *
     * @param loggerName The name of the logger
     * @param message The message to log
     */
    public static void logError(final String loggerName, final String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isErrorEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).error(message);
        }
        System.exit(1);
    }

    static void logDebug(final String loggerName, final String message) {
        if (org.slf4j.LoggerFactory.getLogger(loggerName).isDebugEnabled()) {
            org.slf4j.LoggerFactory.getLogger(loggerName).debug(message);
        }
    }

}
