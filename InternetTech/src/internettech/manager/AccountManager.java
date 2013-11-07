/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.manager;

import internettech.model.Account;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class AccountManager {
    private static AccountManager instance;
    
    private AccountManager () throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS account(ID,USERNAME,PASSWORD,BALANCE);";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        ps.close();
        conn.close();
    }
    
    public static AccountManager getInstance() throws SQLException {
        if(instance == null){
            instance = new AccountManager();
        } 
        return instance;
    }
    
    public final Connection getConnection() throws SQLException  {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:saxExchange.db");
	    return conn;
    }
    
    public final void create(Account account) throws SQLException {
        String sql = "INSERT INTO account VALUES(?,?,?,?);";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, account.getId());
        ps.setString(2, account.getUsername());
        ps.setString(3, account.getPassword());
        ps.setFloat(4, account.getSaldo());
        ps.execute();
        ps.close();
        conn.close();
    }
    
    public final Account retrieve(String id) throws SQLException {
        String sql = "SELECT * FROM account WHERE id='"+ id +"';";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Account account = null;
        if(rs.next()) {
            account = new Account(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("password"), 
                    rs.getFloat("balance"));
        }
        rs.close();
        ps.close();
        conn.close();
        return account;
    }
    
    public final Account login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM account WHERE username='"+ username +"' AND password='"+ password +"';";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Account account = null;
        if(rs.next()) {
            account = new Account(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("password"), 
                    rs.getFloat("balance"));
        }
        rs.close();
        ps.close();
        conn.close();
        return account;
    }
    
    
    
}
