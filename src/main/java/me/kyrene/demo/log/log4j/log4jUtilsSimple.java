package me.kyrene.demo.log.log4j;


import org.apache.log4j.Logger;

/**
 * Created by wanglin on 2017/11/27.
 */
public class log4jUtilsSimple {

    private static log4jUtilsSimple instance = getInstance();
    private static Logger logger = null;

    static {
        logger = Logger.getLogger(log4jUtilsSimple.class);
    }

    private log4jUtilsSimple() {
    }

    private static log4jUtilsSimple getInstance() {
        synchronized (log4jUtilsSimple.class) {
            if (instance == null) {
                instance = new log4jUtilsSimple();
            }
        }
        return instance;
    }

    public static void debug(String str) {
        logger.debug(str);
    }

    public static void debug(String str, Exception e) {
        logger.debug(str, e);
    }

    public static void info(String str) {
        logger.info(str);
    }

    public static void info(String str, Exception e) {
        logger.info(str, e);
    }

    public static void warn(String str) {
        logger.warn(str);
    }

    public static void warn(String str, Exception e) {
        logger.warn(str, e);
    }

    public static void error(String str) {
        logger.error(str);
    }

    public static void error(String str, Exception e) {
        logger.error(str, e);
    }


}
