package org.lamisplus.datafi.dao;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import org.lamisplus.datafi.databases.DBOpenHelper;
import org.lamisplus.datafi.databases.LamisPlusDBOpenHelper;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.LamisCustomHandler;

import java.util.List;

public class PersonDAO {


    public static List<Person> getAllPatients(){
        List<Person> person = new Select().from(Person.class).execute();
        return person;
    }

    public static Person findPersonById(String id) {
        if(id != null) {
            Person person = new Select().from(Person.class).where("id=?", id).executeSingle();
            if (person != null) {
                return person;
            }
        }
        return null;
    }

    public static Boolean checkPersonIdExists(String id) {
        if(id != null) {
            Person person = new Select().from(Person.class).where("personId=?", id).executeSingle();
            if (person != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void updatePatient(Person person, String id){
        Person foo = Person.load(Person.class, Long.parseLong(id));
        person.save();
    }

    public static void deletePatient(long id){
        new Delete().from(Person.class).where("id=?", id).execute();
    }

    public List<Person> getUnsyncedPatients(){
        List<Person> person = new Select().from(Person.class).where("synced=?", 0).execute();
        LamisCustomHandler.showJson(person);
        return person;
    }

}
