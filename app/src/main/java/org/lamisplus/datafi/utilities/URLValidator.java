package org.lamisplus.datafi.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class URLValidator {
    private static final String URL_PATTERN = "^((https|http)?://)([\\da-z.-]*)\\.([a-z.]*)([\\w .-]*)*(:([0-9]{2,5}))?((/(\\w*(-\\w+)?))*?)/*$";
    private static final String SLASH = "/";
    private static final String SPACE = " ";

    private URLValidator() {
    }

    public static ValidationResult validate(String url) {
        ValidationResult result;
        String ensuredUrl = ensureProtocol(url);
        Pattern urlPattern = Pattern.compile(URL_PATTERN);
        Matcher matcher = urlPattern.matcher(trimLastSpace(ensuredUrl));
        String validURL = trimLastSpace(ensuredUrl);
        if (matcher.matches()) {
            validURL = trimLastSlash(validURL);
            validURL = toLowerCase(validURL);
            result = new ValidationResult(true, validURL);
        } else {
            result = new ValidationResult(false, validURL);
        }
        return result;
    }

    public static String trimLastSpace(String url) {
        String trimmedUrl = url;
        while (trimmedUrl.endsWith(SPACE)) {
            trimmedUrl = trimmedUrl.substring(0, trimmedUrl.lastIndexOf(SPACE));
        }
        return trimmedUrl;
    }

    public static String toLowerCase(String url){
        return url.toLowerCase();
    }

    public static String trimLastSlash(String url) {
        String validUrl = url;
        while (validUrl.endsWith(SLASH)) {
            validUrl = validUrl.substring(0, validUrl.lastIndexOf(SLASH));
        }
        return validUrl;
    }

    public static String ensureProtocol(String url)
    {
        if (!url.matches("\\w+:.*"))
        {
            return "http://" + url;
        }
        return url;
    }

    public static class ValidationResult {
        private final boolean isURLValid;
        private final String url;

        public ValidationResult(boolean isValid, String url) {
            this.isURLValid = isValid;
            this.url = url;
        }

        public boolean isURLValid() {
            return isURLValid;
        }

        public String getUrl() {
            return url;
        }

    }
}

