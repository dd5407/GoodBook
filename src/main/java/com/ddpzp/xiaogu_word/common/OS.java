package com.ddpzp.xiaogu_word.common;

/**
 * Created by dd
 * Date 2020/2/23 19:33
 */
public enum OS {
    LINUX, WINDOWS, MAC, OTHER;
    private static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public OS getOSType() {
        if (isLinux()) {
            return LINUX;
        } else if (isWindows()) {
            return WINDOWS;
        } else if (isMacOS()) {
            return MAC;
        }
        return OTHER;
    }

    public String getOSName() {
        return OS_NAME;
    }

    public boolean isLinux() {
        return OS_NAME.indexOf("linux") >= 0;
    }

    public static boolean isWindows() {
        return OS_NAME.indexOf("windows") >= 0;
    }

    public static boolean isMacOS() {
        return OS_NAME.indexOf("mac") >= 0 && OS_NAME.indexOf("os") > 0;
    }
}
