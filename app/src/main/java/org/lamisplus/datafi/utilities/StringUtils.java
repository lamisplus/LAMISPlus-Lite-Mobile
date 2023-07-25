package org.lamisplus.datafi.utilities;

import android.util.Log;

public class StringUtils {
    private static final String NULL_AS_STRING = "null";
    private static final String SPACE_CHAR = " ";

    public static boolean notNull(String string) {
        return null != string && !NULL_AS_STRING.equals(string.trim());
    }

    public static boolean notZero(int intValue) {
        return 0 != intValue;
    }

    public static boolean isBlank(String string) {
        return null == string || SPACE_CHAR.equals(string);
    }

    public static boolean notEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static String unescapeJavaString(String st) {

        StringBuilder sb = new StringBuilder(st.length());

        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == st.length() - 1) ? '\\' : st
                        .charAt(i + 1);
                // Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    // Hex Unicode: u????
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5), 16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                    default:
                        // Do nothing
                        break;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    public static boolean changeStringToBoolean(String s){
        if(s.toLowerCase().equals("yes")){
            return true;
        }
        return false;
    }

    public static String changeBooleanToString(boolean bool){
        if(bool){
            return "Yes";
        }
        return "No";
    }

    public static String changeYesNoToTrueFalse(String s){
        if (s != null) {
            if (s.toLowerCase().equals("yes")) {
                return "true";
            }
            return "false";
        }
        return "";
    }

    public static String changeBooleanToString(String s) {
        if (s != null) {
            if (s.toLowerCase().equals("true")) {
                return "Yes";
            }
            return "No";
        }
        return "";
    }

    public static String changePosNegtoBooleanString(String s){
        if (s != null) {
            if (s.toLowerCase().equals("positive")) {
                return "true";
            }
            return "false";
        }
        return "";
    }

    public static String changeBooleanToPosNeg(String s){
        if (s != null) {
            if (s.toLowerCase().equals("true")) {
                return "Positive";
            }
            return "Negative";
        }
        return "";
    }

    public static String changeRequestResultToBool(String value){
        if(notEmpty(value)){
            if(value.toLowerCase().equals("positive") || value.toLowerCase().equals("reactive")){
                return "Yes";
            }else{
                return "No";
            }
        }
        return "";
    }

    public static String changeRequestResultBoolToReactNon(String value){
        if(notEmpty(value)){
            if(value.toLowerCase().equals("yes")){
                return "Reactive";
            }else{
                return "Non Reactive";
            }
        }
        return "";
    }

    public static String changeRequestResultBoolToPosNeg(String value){
        if(notEmpty(value)){
            if(value.toLowerCase().equals("yes")){
                return "Positive";
            }else{
                return "Negative";
            }
        }
        return "";
    }

    public static boolean changeBooleanStringtoBoolean(String s){
        if(s.toLowerCase().equals("true")){
            return true;
        }
        return false;
    }

}
