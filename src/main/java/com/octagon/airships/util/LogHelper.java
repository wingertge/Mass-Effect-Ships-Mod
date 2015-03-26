package com.octagon.airships.util;

import com.octagon.airships.reference.Reference;
import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class LogHelper {
    public static void log(Level logLevel, Object message) {
        FMLLog.log(Reference.NAME, logLevel, String.valueOf(message));
    }

    public static void all(Object o) { log(Level.ALL, o); }

    public static void info(Object o) { log(Level.INFO, o); }

    public static void debug(Object o) { log(Level.DEBUG, o); }

    public static void error(Object o) { log(Level.ERROR, o); }

    public static void fatal(Object o) { log(Level.FATAL, o); }

    public static void trace(Object o) { log(Level.TRACE, o); }

    public static void warn(Object o) { log(Level.WARN, o); }
}
