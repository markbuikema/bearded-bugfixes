/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.manager;

import internettech.model.Account;
import internettech.model.UserAccount;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian
 */
public class AccountManager {

    private static AccountManager instance;
    private final List<UserAccount> accounts;

    private AccountManager()  {
        accounts = new ArrayList<>(); 
        accounts.add(new UserAccount("admin", "admin", 1337));
    }

    public static AccountManager getInstance()  {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public final boolean createUser(UserAccount account)  {
        if (!accountExists(account)) {
            accounts.add(account);
            return true;
        } else {
            return false;
        }
    }

    public final UserAccount retrieveUserAccount(String id)  {
        for (UserAccount account : accounts) {
            if(account.getId().equals(id)) {
                return account;
            }
        }
        return null;
    }
    
     public void saveAccount(UserAccount account) {
        for(int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).getId().equals(account.getId())){
                accounts.set(i, account);
            }
        }
    }

    public final Account login(String username, String password)  {
        for(UserAccount account : accounts) {
            if(account.getName().equals(username) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }
    
    public boolean accountExists(Account account)  {
        for(Account acc : accounts) {
            if(acc.getName().equals(account.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public List<UserAccount> getUserAccounts() {
        return accounts;
    }
}
