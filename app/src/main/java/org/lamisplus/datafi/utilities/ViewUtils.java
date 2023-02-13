package org.lamisplus.datafi.utilities;

import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.regex.Pattern;

public class ViewUtils {

    public static final String ILLEGAL_CHARACTERS = "[$&+:;=\\\\?@#|/'<>^*()%!]";

    public static final String ILLEGAL_ADDRESS_CHARACTERS = "[$&+:;=\\\\?@|<>^%!]";
    public static final String ILLEGAL_NAME_CHARACTERS = "[1234567890$&+:;=\\\\?@|<>^%!]";

    public static String getInput(EditText e) {
        if(e.getText() == null) {
            return null;
        } else if (isEmpty(e)) {
            return null;
        } else {
            return e.getText().toString();
        }
    }

    public static String getInput(AutoCompleteTextView e){
        if(e.getText() == null) {
            return null;
        } else if (isEmpty(e)) {
            return null;
        } else {
            return e.getText().toString();
        }
    }

    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


    public static boolean isSpinnerEmpty(Spinner spinner) {
        return spinner.getCount() == 0;

    }


    /**
     * Validate a String for invalid characters
     * @param toValidate    the String to check
     * @return true if String is appropriate
     */
    public static boolean validateText(String toValidate, String invalidCharacters) {
        //TODO: Add more checks to the String
        return !containsCharacters(toValidate, invalidCharacters);
    }

    /**
     * Check if a name contains a character from a string param
     * @param toExamine     the String to check
     * @param characters    characters checked against toExamine
     * @return true if the String contains a character from a sequence of characters
     */
    private static boolean containsCharacters(String toExamine, String characters) {

        if (StringUtils.isBlank(toExamine)) {
            return false;
        }

        Pattern charPattern = Pattern.compile(characters);

        return charPattern.matcher(toExamine).find();
    }

}
