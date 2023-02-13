package org.lamisplus.datafi.dao;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import org.lamisplus.datafi.models.Account;
import org.lamisplus.datafi.models.Encounter;

import java.util.List;

public class AccountDAO {

    public static Account checkUserExists(String username, String password){
        Account account = new Select().from(Account.class).where("username = ? AND password = ?", username, password).executeSingle();
        return account;
    }

    public static int countUsers(){
        List<Account> account = new Select().from(Account.class).execute();
        return account.size();
    }

    public static Account getUserDetails(){
        Account account = new Select().from(Account.class).executeSingle();
        return account;
    }

    public static List<Account> getAllAccounts(){
        List<Account> accounts = new Select().from(Account.class).execute();
        return accounts;
    }

    public static void setLocation(String locationName){
        new Update(Account.class).set("selected = ?", 0).execute();
        new Update(Account.class).set("selected = ?", 1).where("currentOrganisationUnitName = ?", locationName).execute();
    }

    public static Account getDefaultLocation(){
        Account account = new Select().from(Account.class).where("selected = ?", 1).executeSingle();
        if(account != null){
            return account;
        }
        return null;
    }
}
