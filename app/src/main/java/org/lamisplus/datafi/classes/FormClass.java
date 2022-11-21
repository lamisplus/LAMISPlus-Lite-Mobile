package org.lamisplus.datafi.classes;

import java.util.HashMap;
import java.util.Map;

public class FormClass extends HashMap<String, String> {

    public FormClass(String fieldName, String fieldValue){
        this.put(fieldName, fieldValue);
    }

}
