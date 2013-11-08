/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.manager;

import internettech.model.Share;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian
 */
public class ShareManager {

    private static ShareManager instance;
    private final ArrayList<Share> shares;

    private ShareManager() {
        shares = new ArrayList<>();
    }

    public static ShareManager getInstance() {
        if (instance == null) {
            instance = new ShareManager();
        }
        return instance;
    }

    public final void store(Share share) {
        shares.add(share);
    }

    public final Share retrieve(String id) {
        for (Share share : shares) {
            if (share.getId().equals(id)) {
                return share;
            }
        }
        return null;
    }

    public List<Share> getSharesFromAss(String assId) {
        List<Share> assShare = new ArrayList<>();
        for (Share share : shares) {
            if (share.getAssociation().equals(assId)) {
                assShare.add(share);
            }
        }
        return assShare;
    }

    public List<Share> getSharesForSale() {
        List<Share> assShare = new ArrayList<>();
        for (Share share : shares) {
            if (share.isForSale()) {
                assShare.add(share);
            }
        }
        return assShare;
    }

    public boolean transaction(String buyerAccountId, String sellerAccountId, String assId, int amount) {
        List<Share> ownerShares = getSharesFromOwner(sellerAccountId,assId);
        if (ownerShares.size() > amount) {
            for(int i = 0; i < amount; i++) {
            Share share = ownerShares.get(i);
            share.setForSale(false);
            share.setOwnerId(buyerAccountId);
            share.setPrice(5.0f);
            setShare(share);
            }
            return true;
        }
        return false;
    }
    
    public void setShare(Share share) {
        for(int i = 0; i < shares.size(); i++) {
            if(shares.get(i).getId().equals(share.getId())) {
                 shares.set(i, share);
            }
        }
    }

    public List<Share> getSharesForSale(String assId) {
        List<Share> assShare = new ArrayList<>();
        for (Share share : shares) {
            if (share.isForSale() && share.getId().equals(assId)) {
                assShare.add(share);
            }
        }
        return assShare;
    }

    public List<Share> getSharesFromOwner(String ownerId) {
        List<Share> assShare = new ArrayList<>();
        for (Share share : shares) {
            if (share.isForSale() && share.getOwnerId().equals(ownerId)) {
                assShare.add(share);
            }
        }
        return assShare;
    }
    
    public List<Share> getSharesFromOwner(String ownerId, String assId) {
        List<Share> assShare = new ArrayList<>();
        for (Share share : shares) {
            if (share.isForSale() && share.getOwnerId().equals(ownerId)) {
                assShare.add(share);
            }
        }
        return assShare;
    }

    public int getShareCount() {
        return shares.size();
    }
}
