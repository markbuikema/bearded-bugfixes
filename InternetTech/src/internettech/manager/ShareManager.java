/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.manager;

import internettech.model.Share;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class ShareManager {
    private static ShareManager instance;
    
    private ShareManager () throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS share(ID,ASSOCIATION,PRICE,FORSALE,ACCOUNT);";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        ps.close();
        conn.close();
    }
    
    public static ShareManager getInstance() throws SQLException {
        if(instance == null){
            instance = new ShareManager();
        } 
        return instance;
    }
    
    public final Connection getConnection() throws SQLException  {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ShareManager.class.getName()).log(Level.SEVERE, null, ex);
        }
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:saxExchange.db");
	    return conn;
    }
    
    public final void create(Share share) throws SQLException {
        String sql = "INSERT INTO share VALUES(?,?,?,?,?);";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, share.getId());
        ps.setString(2, share.getAssociation());
        ps.setFloat(3, share.getPrice());
        ps.setBoolean(4, share.isForSale());
        ps.setString(5, share.getOwner().getId());
        ps.execute();
        ps.close();
        conn.close();
    }
    
    public final Share retrieve(String id) throws SQLException {
        String sql = "SELECT * FROM share WHERE id='"+ id +"';";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Share share = null;
        if(rs.next()) {
            share = new Share(
                    rs.getString("id"),
                    rs.getString("association")
                    );
        }
        rs.close();
        ps.close();
        conn.close();
        return share;
    }
    
    public List<Share> getSharesFromAss(String assId) {
//        String sql = "SELECT * FROM share WHERE association='"+ assId +"';"; 
//        Connection
//        PreparedStatement ps = 
        
        return null;
    }
    
    
    
    
    
    
}
