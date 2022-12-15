package org.lamisplus.datafi.dao;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.models.Account;

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

}
