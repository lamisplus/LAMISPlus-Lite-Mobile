package org.lamisplus.datafi.classes;

import java.util.ArrayList;
import java.util.List;

public class ContactPointClass {

    String name;

    List<ContactPointItems> contactPointItems = new ArrayList<>();

    public ContactPointClass(List<ContactPointItems> items) {
        this.contactPointItems = items;
    }

    public static class ContactPointItems {
        String type;
        String value;

        public ContactPointItems(String type, String value) {
            this.type = type;
            this.value = value;
        }
    }

}
