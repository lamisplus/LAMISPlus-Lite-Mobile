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
     * @return String
     */
    public static String findCodesetsDisplayById(int id){
        Codesets codesets = new Select().from(Codesets.class).where("codeset_id=?", id).executeSingle();
        return codesets.getDisplay();
    }


    /**
     *This function returns the display when a code is passed in as parameter
     * @param code
     * @return
     */
    public static String findCodesetsDisplayByCode(String code){
        Codesets codesets = new Select().from(Codesets.class).where("code=?", code).executeSingle();
        return codesets.getDisplay();
    }

    /**
     *This function returns the code when a display is passed in as parameter to the function
     * @param display
     * @return
     */
    public static String findCodesetsCodeByDisplay(String display){
        Codesets codesets = new Select().from(Codesets.class).where("display=?", display).executeSingle();
        return codesets.getCode();
    }

}
