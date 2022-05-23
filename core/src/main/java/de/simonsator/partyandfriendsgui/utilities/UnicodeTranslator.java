package de.simonsator.partyandfriendsgui.utilities;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class UnicodeTranslator {
    public static String UnicodeTranslator(String text) {
        return StringEscapeUtils.unescapeJava(text);
    }
}
