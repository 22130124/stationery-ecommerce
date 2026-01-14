package com.tqk.productservice.util;

import java.text.Normalizer;
import java.util.Locale;

// Hỗ trợ chuyển đổi chuỗi thành chữ thường không dấu
public class TextNormalizeUtil {
    public static String normalize(String input) {
        if (input == null) return "";
        String text = input.toLowerCase(Locale.ROOT);
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return text;
    }
}
