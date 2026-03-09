package ltd.idcu.est.data.api;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class LambdaUtils {

    public static String getFieldName(SFunction<?, ?> func) {
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(func);
            String implMethodName = serializedLambda.getImplMethodName();
            if (implMethodName.startsWith("get")) {
                return toLowerCaseFirst(implMethodName.substring(3));
            } else if (implMethodName.startsWith("is")) {
                return toLowerCaseFirst(implMethodName.substring(2));
            }
            return toLowerCaseFirst(implMethodName);
        } catch (Exception e) {
            throw new DataException("Failed to get field name from lambda", e);
        }
    }

    public static String getColumnName(SFunction<?, ?> func) {
        String fieldName = getFieldName(func);
        return camelToUnderscore(fieldName);
    }

    public static <T, V> void validateType(SFunction<T, V> func, V value) {
    }

    private static String toLowerCaseFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    private static String camelToUnderscore(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
