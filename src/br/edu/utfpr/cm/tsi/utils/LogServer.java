/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.tsi.utils;

import com.m4rc310.utils.DateUtils;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Marcelo
 */
public class LogServer {

    public enum EnumLogType {
        ERROR,
        INFO,
        WARNING,
        DEBUG,
        DEBUG_DETAILS
    }

    private final Collection<LogListener> listeners;

    private LogServer() {
        this.listeners = new ArrayList<>();
    }

    public void addLogListener(LogListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeLogListener(LogListener listener) {
        listeners.remove(listener);
    }

    public void debug(Object target, String message, Object... args) {
        fireStatus(target, EnumLogType.DEBUG, message, args);
    }

    public void error(Throwable e) {
        fireStatus(e, EnumLogType.ERROR, e.getMessage());
    }

    public void error(Object target, String message, Object... args) {
        fireStatus(target, EnumLogType.ERROR, message, args);
    }

    public void info(Object target, String message, Object... args) {
        fireStatus(target, EnumLogType.INFO, message, args);
    }

    public void warning(Object target, String message, Object... args) {
        fireStatus(target, EnumLogType.WARNING, message, args);
    }

    private void fireStatus(Object target, EnumLogType type, String _message, Object... args) {

        String mt1 = Thread.currentThread().getStackTrace()[2].getMethodName();
        String mt2 = Thread.currentThread().getStackTrace()[3].getMethodName();
        String mt3 = Thread.currentThread().getStackTrace()[3].getClassName();
        int mt4 = Thread.currentThread().getStackTrace()[3].getLineNumber();

        final String message = MessageFormat.format(_message, args);
//        final String debug = String.format("[%s] %s %s [%s - Linha: %d]\n ~> %s", mt1, DateUtils.dateToString(new Date(), "yyyyMMdd HH:mm:ss"), mt3, mt2, mt4, message);
        final String debug = String.format("[%s] %s %s [%s - Linha: %d]\n ~> %s", mt1, DateUtils.dateToString(new Date(), "yyyyMMdd HH:mm:ss"), mt3, mt2, mt4, message);

        listeners.stream().forEach((listener) -> {

            listener.debugMessageDetails(type, target, debug);
            switch (type) {
                case DEBUG:
                    listener.debugMessage(target, message);
                    break;
                case ERROR:
                    listener.errorMessage(target, message);
                    break;
                case INFO:
                    listener.infoMessage(target, message);
                    break;
                case WARNING:
                    listener.warningMessage(target, message);
                    break;
            }
        });

    }

    public static LogServer getInstance() {
        return LogServerHolder.INSTANCE;
    }

    private static class LogServerHolder {

        private static final LogServer INSTANCE = new LogServer();
    }
}
