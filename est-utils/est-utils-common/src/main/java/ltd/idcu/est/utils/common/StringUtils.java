package ltd.idcu.est.utils.common;

import java.util.Objects;

public final class StringUtils {

    private static final String EMPTY = "";
    private static final String NULL_STRING = "null";
    private static final int INDEX_NOT_FOUND = -1;

    private StringUtils() {
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        if (cs == null || cs.isEmpty()) {
            return true;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String trimToNull(String str) {
        String trimmed = trim(str);
        return isEmpty(trimmed) ? null : trimmed;
    }

    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static int compare(String str1, String str2) {
        return compare(str1, str2, true);
    }

    public static int compare(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : -1;
        }
        return str1.compareTo(str2);
    }

    public static int compareIgnoreCase(String str1, String str2) {
        return compareIgnoreCase(str1, str2, true);
    }

    public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : -1;
        }
        return str1.compareToIgnoreCase(str2);
    }

    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        return str.startsWith(prefix);
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.substring(0, prefix.length()).equalsIgnoreCase(prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        return str.endsWith(suffix);
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        return str.substring(str.length() - suffix.length()).equalsIgnoreCase(suffix);
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.contains(searchStr);
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        String lowerStr = str.toLowerCase();
        String lowerSearchStr = searchStr.toLowerCase();
        return lowerStr.contains(lowerSearchStr);
    }

    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        return str.indexOf(searchStr);
    }

    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        return str.lastIndexOf(searchStr);
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }
        return str.substring(start);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        if (start > end) {
            return EMPTY;
        }
        if (start > str.length()) {
            return EMPTY;
        }
        if (end > str.length()) {
            end = str.length();
        }
        return str.substring(start, end);
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return EMPTY;
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= pos + len) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }

    public static String[] split(String str, String separator) {
        return split(str, separator, -1, false);
    }

    public static String[] split(String str, String separator, int max, boolean preserveAllTokens) {
        if (str == null) {
            return new String[0];
        }
        if (separator == null || separator.isEmpty()) {
            return splitByWhitespace(str, max, preserveAllTokens);
        }
        if (max == 0) {
            max = -1;
        }

        java.util.List<String> list = new java.util.ArrayList<>();
        int start = 0;
        int separatorLen = separator.length();
        int count = 0;

        while (true) {
            int end = str.indexOf(separator, start);
            if (end == INDEX_NOT_FOUND) {
                if (preserveAllTokens || start < str.length()) {
                    list.add(str.substring(start));
                }
                break;
            }
            if (preserveAllTokens || end > start) {
                if (++count == max) {
                    list.add(str.substring(start));
                    break;
                }
                list.add(str.substring(start, end));
            }
            start = end + separatorLen;
        }

        return list.toArray(new String[0]);
    }

    private static String[] splitByWhitespace(String str, int max, boolean preserveAllTokens) {
        java.util.List<String> list = new java.util.ArrayList<>();
        int start = 0;
        int count = 0;
        boolean match = false;
        boolean lastMatch = false;

        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                if (match || preserveAllTokens) {
                    lastMatch = true;
                    if (++count == max) {
                        i = str.length();
                        list.add(str.substring(start, i));
                        break;
                    }
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = i + 1;
            } else {
                lastMatch = false;
                match = true;
            }
        }

        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start));
        }

        return list.toArray(new String[0]);
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String join(java.util.Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        StringBuilder buf = new StringBuilder(256);
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        if (repeat == 1) {
            return str;
        }
        StringBuilder buf = new StringBuilder(str.length() * repeat);
        for (int i = 0; i < repeat; i++) {
            buf.append(str);
        }
        return buf.toString();
    }

    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toTitleCase(str.charAt(0)) + str.substring(1);
    }

    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    public static String replace(String str, String target, String replacement) {
        if (str == null) {
            return null;
        }
        if (target == null || replacement == null) {
            return str;
        }
        return str.replace(target, replacement);
    }

    public static String replaceAll(String str, String regex, String replacement) {
        if (str == null || regex == null || replacement == null) {
            return str;
        }
        return str.replaceAll(regex, replacement);
    }

    public static String replaceFirst(String str, String regex, String replacement) {
        if (str == null || regex == null || replacement == null) {
            return str;
        }
        return str.replaceFirst(regex, replacement);
    }

    public static String remove(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        return str.replace(remove, EMPTY);
    }

    public static String removeStart(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static String removeEnd(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static String padLeft(String str, int length, char padChar) {
        if (str == null) {
            str = EMPTY;
        }
        int padLength = length - str.length();
        if (padLength <= 0) {
            return str;
        }
        return repeat(String.valueOf(padChar), padLength) + str;
    }

    public static String padRight(String str, int length, char padChar) {
        if (str == null) {
            str = EMPTY;
        }
        int padLength = length - str.length();
        if (padLength <= 0) {
            return str;
        }
        return str + repeat(String.valueOf(padChar), padLength);
    }

    public static String center(String str, int length, char padChar) {
        if (str == null) {
            str = EMPTY;
        }
        int padLength = length - str.length();
        if (padLength <= 0) {
            return str;
        }
        int leftPad = padLength / 2;
        int rightPad = padLength - leftPad;
        return repeat(String.valueOf(padChar), leftPad) + str + repeat(String.valueOf(padChar), rightPad);
    }

    public static String truncate(String str, int maxLength) {
        return truncate(str, maxLength, "...");
    }

    public static String truncate(String str, int maxLength, String truncationIndicator) {
        if (str == null) {
            return null;
        }
        if (truncationIndicator == null) {
            truncationIndicator = "...";
        }
        if (maxLength < 0) {
            maxLength = 0;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        int truncationLength = maxLength - truncationIndicator.length();
        if (truncationLength < 0) {
            truncationLength = 0;
        }
        return str.substring(0, truncationLength) + truncationIndicator;
    }

    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        int subLen = sub.length();
        while ((idx = str.indexOf(sub, idx)) != INDEX_NOT_FOUND) {
            count++;
            idx += 1;
        }
        return count;
    }

    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlpha(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String toString(Object obj) {
        return obj == null ? NULL_STRING : obj.toString();
    }

    public static String defaultString(String str) {
        return str == null ? EMPTY : str;
    }

    public static String defaultString(String str, String defaultValue) {
        return str == null ? defaultValue : str;
    }

    public static String strip(String str) {
        return strip(str, null);
    }

    public static String strip(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }

    public static String stripStart(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        int start = 0;
        int strLen = str.length();
        if (stripChars == null) {
            while (start < strLen && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.isEmpty()) {
            return str;
        } else {
            while (start < strLen && stripChars.indexOf(str.charAt(start)) != INDEX_NOT_FOUND) {
                start++;
            }
        }
        return str.substring(start);
    }

    public static String stripEnd(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        int end = str.length();
        if (stripChars == null) {
            while (end > 0 && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.isEmpty()) {
            return str;
        } else {
            while (end > 0 && stripChars.indexOf(str.charAt(end - 1)) != INDEX_NOT_FOUND) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    public static String[] stripAll(String[] strs) {
        return stripAll(strs, null);
    }

    public static String[] stripAll(String[] strs, String stripChars) {
        if (strs == null || strs.length == 0) {
            return strs;
        }
        String[] result = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            result[i] = strip(strs[i], stripChars);
        }
        return result;
    }

    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }

    public static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            maxWidth = 4;
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset >= str.length()) {
            return "..." + str.substring(str.length() - maxWidth + 3);
        }
        if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + "...";
        }
        return "..." + abbreviate(str.substring(offset), maxWidth - 3);
    }

    public static String chomp(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            char ch = str.charAt(0);
            if (ch == '\r' || ch == '\n') {
                return EMPTY;
            }
            return str;
        }
        int lastIdx = str.length() - 1;
        char last = str.charAt(lastIdx);
        if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r') {
                lastIdx--;
            }
        } else if (last != '\r') {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }

    public static String chop(String str) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return EMPTY;
        }
        if (strLen == 1) {
            return EMPTY;
        }
        int lastIdx = strLen - 1;
        String ret = str.substring(0, lastIdx);
        char last = str.charAt(lastIdx);
        if (last == '\n' && ret.charAt(lastIdx - 1) == '\r') {
            return ret.substring(0, lastIdx - 1);
        }
        return ret;
    }
}
