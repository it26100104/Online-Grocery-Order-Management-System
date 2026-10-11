package com.grocery.payment.util;

/** Small helper used by JSP pages so user text cannot break the HTML. */
public class HtmlUtil {
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
