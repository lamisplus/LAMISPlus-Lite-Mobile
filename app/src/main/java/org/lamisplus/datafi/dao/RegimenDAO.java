package org.lamisplus.datafi.dao;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.models.Codesets;
import org.lamisplus.datafi.models.Regimen;

public class RegimenDAO {

    public static Integer findRegimenIdByDescription(String description){
        if(description != null) {
            Regimen regimen = new Select().from(Regimen.class).where("description=?", description).executeSingle();
            if (regimen != null) {
                return regimen.getRegimen_id();
            }
        }
        return null;
    }

    public static String findRegimenDescriptionById(Integer regimenId){
        if(regimenId != null) {
            Regimen regimen = new Select().from(Regimen.class).where("regimen_id=?", regimenId).executeSingle();
            if (regimen != null) {
                return regimen.getDescription();
            }
        }
        return null;
    }

    public static Integer findRegimenTypeIdByDescription(String description){
        Integer value = 0;
        if(description.equals("Adult 3rd Line")){
            value = 14;
        } else if (description.equals("Adult 2nd Line")) {
            value = 2;
        }else if(description.equals("Adult 1st Line")){
            value = 1;
        }
        return value;
    }

    public static String findRegimenDescriptionByRegimenTypeId(Integer regimenId){
        String regimen = "";
        if(regimenId.equals(14)) {
            regimen = "Adult 3rd Line";
        } else if (regimenId.equals(2)) {
            regimen = "Adult 2nd Line";
        } else if (regimenId.equals(1)) {
            regimen = "Adult 1st Line";
        }
        return regimen;
    }

}
