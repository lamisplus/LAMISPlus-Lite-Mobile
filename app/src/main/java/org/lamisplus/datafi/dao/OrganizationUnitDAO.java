package org.lamisplus.datafi.dao;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.models.Codesets;
import org.lamisplus.datafi.models.OrganizationUnit;

public class OrganizationUnitDAO {

    /**
     * This function passes the display of the Organization Unit and returns its id
     * @param name
     * @return int
     */
    public static Integer findOrganizationUnitIdByName(String name) {
        OrganizationUnit organizationUnit = new Select().from(OrganizationUnit.class).where("name=?", name).executeSingle();
        if(organizationUnit != null) {
            return organizationUnit.getUnit_id();
        }
        return null;
    }

    /**
     * This function passes the id of the codesets and returns its display
     * @param id
     * @return String
     */
    public static String findOrganizationUnitNameById(int id){
        OrganizationUnit organizationUnit = new Select().from(OrganizationUnit.class).where("unit_id=?", id).executeSingle();
        return organizationUnit.getName();
    }

}
