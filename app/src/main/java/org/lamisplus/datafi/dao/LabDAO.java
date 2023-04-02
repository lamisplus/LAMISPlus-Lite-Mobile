package org.lamisplus.datafi.dao;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import org.lamisplus.datafi.models.Lab;

import java.util.List;

public class LabDAO {

    public static List<Lab> getAllLabs(){
        List<Lab> labs = new Select().from(Lab.class).execute();
        return labs;
    }

    public static void setLab(String labName){
        new Update(Lab.class).set("selected = ?", 0).execute();
        new Update(Lab.class).set("selected = ?", 1).where("name = ?", labName).execute();
    }

    public static Lab getDefaultLab(){
        Lab lab = new Select().from(Lab.class).where("selected = ?", 1).executeSingle();
        if(lab != null){
            return lab;
        }
        return null;
    }

}
