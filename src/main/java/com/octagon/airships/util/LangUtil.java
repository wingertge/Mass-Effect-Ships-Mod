package com.octagon.airships.util;

import net.minecraft.util.StatCollector;

import java.util.IllegalFormatException;

public class LangUtil {
    public static final String prefix = "masseffectships.";

    public static String localize(String s, String... args) {
        return localize(s, true, args);
    }

    public static String localize(String s, boolean appendModPrefix, String... args) {
        if(appendModPrefix) {
            s = prefix + s;
        }
        String ret = StatCollector.translateToLocal(s);
        try {
            return String.format(ret, (Object[]) args);
        } catch (IllegalFormatException e) {
            return ret;
        }
    }

    public static String[] localizeList(String string) {
        String s = localize(string);
        return s.split("\\|");
    }
}
