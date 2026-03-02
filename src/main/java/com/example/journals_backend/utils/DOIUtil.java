package com.example.journals_backend.utils;

import java.time.Year;

public class DOIUtil {
    private static final String PUBLISHER_PREFIX = "10.1234";  // your publisher code
    private static final String JOURNAL_CODE = "jjp";           // your journal code

    public static String generateDOI(Long articleId) {
        String year = String.valueOf(Year.now().getValue());
        return PUBLISHER_PREFIX + "/" + JOURNAL_CODE + "." + year + "." + articleId;
    }
}
