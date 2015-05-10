package org.wzj.memcached;

/**
 * @author Wen
 */
public class StringUtils {

    public static boolean isJsonObject(String o) {
        if (o == null) return false;

        if (o.startsWith("{") && o.endsWith("}") || o.startsWith("[") && o.endsWith("]")) {
            return true;
        }

        return false;
    }

}
