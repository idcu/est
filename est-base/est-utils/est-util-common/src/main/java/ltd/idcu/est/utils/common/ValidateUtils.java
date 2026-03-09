package ltd.idcu.est.utils.common;

import java.util.regex.Pattern;

public class ValidateUtils {

    private ValidateUtils() {
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private static final Pattern MOBILE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^0\\d{2,3}-?\\d{7,8}$"
    );

    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$"
    );

    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$"
    );

    private static final Pattern IPV4_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );

    private static final Pattern CHINESE_PATTERN = Pattern.compile(
        "^[\\u4e00-\\u9fa5]+$"
    );

    private static final Pattern NUMBER_PATTERN = Pattern.compile(
        "^-?\\d+(\\.\\d+)?$"
    );

    private static final Pattern INTEGER_PATTERN = Pattern.compile(
        "^-?\\d+$"
    );

    private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile(
        "^[1-9]\\d*$"
    );

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z][a-zA-Z0-9_]{3,15}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,20}$"
    );

    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    public static boolean isPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isPhoneOrMobile(String str) {
        return isPhone(str) || isMobile(str);
    }

    public static boolean isIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            return false;
        }
        return checkIdCardCheckCode(idCard);
    }

    private static boolean checkIdCardCheckCode(String idCard) {
        char[] chars = idCard.toCharArray();
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (chars[i] - '0') * weights[i];
        }
        
        int mod = sum % 11;
        return chars[17] == checkCodes[mod];
    }

    public static boolean isUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }

    public static boolean isIpv4(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        return IPV4_PATTERN.matcher(ip).matches();
    }

    public static boolean isChinese(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return CHINESE_PATTERN.matcher(str).matches();
    }

    public static boolean containsChinese(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (c >= 0x4e00 && c <= 0x9fa5) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return NUMBER_PATTERN.matcher(str).matches();
    }

    public static boolean isInteger(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return INTEGER_PATTERN.matcher(str).matches();
    }

    public static boolean isPositiveInteger(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return POSITIVE_INTEGER_PATTERN.matcher(str).matches();
    }

    public static boolean isUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isLength(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    public static boolean isInRange(Number num, Number min, Number max) {
        if (num == null || min == null || max == null) {
            return false;
        }
        double n = num.doubleValue();
        double mi = min.doubleValue();
        double ma = max.doubleValue();
        return n >= mi && n <= ma;
    }

    public static boolean matches(String str, String regex) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(regex)) {
            return false;
        }
        return Pattern.matches(regex, str);
    }
}
