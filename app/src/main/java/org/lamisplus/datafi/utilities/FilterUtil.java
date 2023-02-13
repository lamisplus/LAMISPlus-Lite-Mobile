package org.lamisplus.datafi.utilities;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.lamisplus.datafi.application.LamisPlusLogger;
import org.lamisplus.datafi.models.Person;

import java.util.ArrayList;
import java.util.List;

public class FilterUtil {

    /**
     * Used to filter list by specified query
     * Its possible to filter patients by: Name, Surname (Family Name) or ID.
     *
     * @param patientList list of patients to filter
     * @param query query that needs to be contained in Name, Surname or ID.
     * @return patient list filtered by query
     */
    static LamisPlusLogger logger = new LamisPlusLogger();

    public static List<Person> getPatientsFilteredByQuery(List<Person> patientList, String query) {
        List<Person> filteredList = new ArrayList<>();

        for (Person person : patientList) {

            List<String> searchableWords = getPatientSearchableWords(person);

            if (doesAnySearchableWordFitQuery(searchableWords, query)) {
                filteredList.add(person);
            }

        }
        return filteredList;
    }


    public static List<Person> getPatientsFilteredByQuery(List<Person> personList) {
        List<Person> filteredList = new ArrayList<>();
        try {
            for (Person person : personList) {
                    filteredList.add(person);
            }
        } catch (Exception e) {
            logger.e("Error syncing: ", e);
        }
        return filteredList;
    }


    private static List<String> getPatientSearchableWords(Person person) {
        String patientIdentifier = person.getIdentifiers().getValue();
        String fullName =  person.getFirstName();
        String givenFamilyName = person.getFirstName() + " "
                + person.getSurname();

        List<String> searchableWords = new ArrayList<>();
        searchableWords.add(patientIdentifier);
        searchableWords.add(fullName);
        searchableWords.add(givenFamilyName);

        return searchableWords;
    }


    private static boolean doesAnySearchableWordFitQuery(List<String> searchableWords, String query) {
        for (String searchableWord : searchableWords) {
            if (searchableWord != null) {
                int queryLength = query.trim().length();
                searchableWord = searchableWord.toLowerCase();
                query = query.toLowerCase().trim();
                boolean fits = searchableWord.length() >= queryLength && searchableWord.contains(query);
                if (fits) {
                    return true;
                }
            }
        }
        return false;
    }

}
