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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class ShareManager {

    private static ShareManager instance;
    private final ArrayList<Share> shares;

    private ShareManager() throws SQLException {
        shares = new ArrayList<>();
     }

    public static ShareManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new ShareManager();
        }
        return instance;
    }

    public final void store(Share share) throws SQLException {
        shares.add(share);
    }

    public final Share retrieve(String id) throws SQLException {
        for(Share share : shares) {
            if(share.getId().equals(id)) {
                return share;
            }
        }
        return null;
    }

    public List<Share> getSharesFromAss(String assId) throws SQLException {
        List<Share> assShare = new ArrayList<>();
        for(Share share : shares) {
            if(share.getAssociation().equals(assId)) {
                assShare.add(share);
            }
        }
        return assShare;
    }

    public List<Share> getSharesForSale() throws SQLException {
        List<Share> assShare = new ArrayList<>();
        for(Share share : shares) {
            if(share.isForSale()) {
                assShare.add(share);
            }
        }
        return assShare;
    }
    
    public List<Share> getSharesForSale(String assId) throws SQLException {
        List<Share> assShare = new ArrayList<>();
        for(Share share : shares) {
            if(share.isForSale() && share.getId().equals(assId)) {
                assShare.add(share);
            }
        }
        return assShare;
    }
    
    public List<Share> getSharesFromOwner(String ownerId) throws SQLException {
        List<Share> assShare = new ArrayList<>();
        for(Share share : shares) {
            if(share.isForSale() && share.getOwnerId().equals(ownerId)) {
                assShare.add(share);
            }
        }
        return assShare;
    }

    public int getShareCount() throws SQLException {
        return shares.size();
    }
}
