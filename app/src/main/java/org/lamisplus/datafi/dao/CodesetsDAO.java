package org.lamisplus.datafi.dao;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.models.Codesets;
import org.lamisplus.datafi.models.Person;

public class CodesetsDAO {

    /**
     * This function passes the display of the codesets and returns its id
     * @param display
     * @return int
     */
    public static int findCodesetsIdByDisplay(String display) {
        Codesets codesets = new Select().from(Codesets.class).where("display=?", display).executeSingle();
        return codesets.getCodeset_id();
    }

    /**
     * This function passes the id of the codesets and returns its display
     * @param id
     * @return
     */
    public static String findCodesetsDisplayById(int id){
        Codesets codesets = new Select().from(Codesets.class).where("codeset_id=?", id).executeSingle();
        return codesets.getDisplay();
    }


}
