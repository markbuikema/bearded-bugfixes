/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.manager;

import internettech.model.Account;
import internettech.model.Share;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian
 */
public class AccountManager {

    private static AccountManager instance;
    private List<Account> accounts;

    private AccountManager()  {
        accounts = new ArrayList<>(); 
        accounts.add(new Account("admin", "admin", 1337));
    }

    public static AccountManager getInstance()  {
        if (instance == null) {
            instance = new AccountManager();
            
        }
        return instance;
    }

    public final boolean store(Account account)  {
        if (!accountExists(account)) {
            accounts.add(account);
            return true;
        } else {
            return false;
        }
    }

    public final Account retrieve(String id)  {
        for (Account account : accounts) {
            if(account.getId().equals(id)) {
                return account;
            }
        }
        return null;
    }

    public final Account login(String username, String password)  {
        for(Account account : accounts) {
            if(account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }
    
    public boolean accountExists(Account account)  {
        for(Account acc : accounts) {
            if(acc.getUsername().equals(account.getUsername())) {
                return true;
            }
        }
        return false;
    }
    
    public List<Account> getAccounts() {
        return accounts;
    }
}
