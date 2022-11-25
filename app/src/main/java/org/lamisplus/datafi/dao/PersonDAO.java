package org.lamisplus.datafi.dao;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import org.lamisplus.datafi.databases.DBOpenHelper;
import org.lamisplus.datafi.databases.LamisPlusDBOpenHelper;
import org.lamisplus.datafi.models.Person;

import java.util.List;

public class PersonDAO {

    public Person findPersonById(String id) {
        Person person = new Select().from(Person.class).where("id=?", id).executeSingle();
        return person;
    }

    public void updatePatient(Person person, String id){
        Person foo = Person.load(Person.class, Long.parseLong(id));
        person.save();
    }

    public void deletePatient(long id){
        new Delete().from(Person.class).where("id=?", id).executeSingle();
    }

    public List<Person> getUnsyncedPatients(){
        List<Person> person = new Select().from(Person.class).where("synced=?", 0).execute();
        return person;
    }

}
