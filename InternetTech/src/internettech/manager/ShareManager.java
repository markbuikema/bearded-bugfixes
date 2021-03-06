package internettech.manager;

import internettech.model.Account;
import internettech.model.Association;
import internettech.model.Share;
import internettech.model.UserAccount;

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

	/**
	 * Store a share.
	 * 
	 * @param share
	 *            The share to store
	 */
	public final void save(Share share) {
		shares.add(share);
	}

	/**
	 * Retrieve the share by id.
	 * 
	 * @param id
	 *            Gets the share by id
	 * @return the share that matches the id, otherwise returns null
	 */
	public final Share retrieve(String id) {
		for (Share share : shares) {
			if (share.getId().equals(id)) {
				return share;
			}
		}
		return null;
	}

	/**
	 * Gets the share from an Association
	 * 
	 * @param assId
	 *            The id of the association
	 * @return List of shares from an association
	 */
	public List<Share> getSharesFromAss(String assId) {
		List<Share> assShare = new ArrayList<>();
		for (Share share : shares) {
			if (share.getAssociationId().equals(assId)) {
				assShare.add(share);
			}
		}
		return assShare;
	}

	public List<Share> getSharesFromAssForSale(String assId) {
		List<Share> assShare = new ArrayList<>();
		for (Share share : shares) {
			if (share.getAssociationId().equals(assId) && share.isForSale()) {
				assShare.add(share);
			}
		}
		return assShare;
	}

	/**
	 * Transacts an amount of shares between two users from an specific
	 * association.
	 * 
	 * @param buyerAccountId
	 *            The accountId of the buyer
	 * @param sellerAccountId
	 *            The accountId of the selller
	 * @param assId
	 *            The id of the association
	 * @param amount
	 *            The amount of shares to transact
	 * @return true if transaction was succesfull, otherwise returns false
	 */
	public boolean transaction(String buyerAccountId, String sellerAccountId, String assId, int amount) {
		List<Share> ownerShares = getSharesFromOwnerForSale(sellerAccountId, assId);
                
                
		UserAccount buyer = AccountManager.getInstance().retrieveUserAccount(buyerAccountId);
		
                // Get the user that sells the share
                Account seller = AccountManager.getInstance().retrieveUserAccount(sellerAccountId);
                
                // if seller is still null, the seller might be an association
		if (seller == null) seller = AssociationManager.getInstance().retrieve(assId);
                
                // Now perform security checks
                if(seller == null) return false; // couldn't find seller
                
                
                if(buyer == null) return false; // buyer doesnt exist
                
                

		if (ownerShares.size() >= amount && (buyer.getBalance() >= (amount * getPrice(sellerAccountId, assId)))) {
			float price = amount * getPrice(sellerAccountId, assId);

			/** Pay **/
			buyer.setBalance(buyer.getBalance() - price);       // Lose money
			AccountManager.getInstance().saveAccount(buyer);    // 

			/** Profit **/
			if (seller instanceof UserAccount) {
				UserAccount user = (UserAccount) seller;
				user.setBalance(user.getBalance() + price);
                                AccountManager.getInstance().saveAccount(user);
			} else {
				Association ass = (Association) seller;
				ass.setBalance(ass.getBalance() + price);
                                AssociationManager.getInstance().save(ass);
			}

			/** Deliver **/
			for (int i = 0; i < amount; i++) {
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

	private float getPrice(String ownerId, String assID) {
		return getSharesFromOwnerForSale(ownerId, assID).get(0).getPrice();
	}

	/**
	 * Transacts an amount of shares between two users from an specific
	 * association.
	 * 
	 * @param sellerAccountId
	 *            The accountId of the selller
	 * @param assId
	 *            The id of the association
	 * @param amount
	 *            The amount of shares to transact
	 * @param price
	 *            The price of one share
	 * @return true if transaction was succesfull, otherwise returns false
	 */
	public boolean setSharesForSale(String sellerAccountId, String assId, int amount, float price) {
		List<Share> ownerShares = getSharesFromOwnerForSale(sellerAccountId, assId);
		if (ownerShares.size() >= amount) {
			for (int i = 0; i < amount; i++) {
				Share share = ownerShares.get(i);
				share.setForSale(true);
				share.setPrice(price);
				setShare(share);
			}
			return true;
		}
		return false;
	}

	/**
	 * Sets a share
	 * 
	 * @param share
	 *            The share to set
	 */
	public void setShare(Share share) {
		for (int i = 0; i < shares.size(); i++) {
			if (shares.get(i).getId().equals(share.getId())) {
				shares.set(i, share);
			}
		}
	}

	/**
	 * Gets all the shares from a user
	 * 
	 * @param ownerId
	 *            The id of the UserAccount
	 * @return List of shares from an user
	 */
	public List<Share> getSharesFromOwner(String ownerId) {
		List<Share> assShare = new ArrayList<>();
		for (Share share : shares) {
			if (share.getOwnerId().equals(ownerId)) {
				assShare.add(share);
			}
		}
		return assShare;
	}

	/**
	 * Gets all the shares from a user from a specific association
	 * 
	 * @param ownerId
	 *            The id of the UserAccount
	 * @param assId
	 *            The id of the association
	 * @return List of shares from an user
	 */
	public List<Share> getSharesFromOwner(String ownerId, String assId) {
		List<Share> assShare = new ArrayList<>();
		for (Share share : shares) {
			if (share.getOwnerId().equals(ownerId) && share.getAssociationId().equals(assId)) {
				assShare.add(share);
			}
		}
		return assShare;
	}

	/**
	 * Gets all the shares for sale from a user
	 * 
	 * @param ownerId
	 *            The id of the UserAccount that owns the share
	 * @param assId
	 *            The association id
	 * @return List of shares that are for sale from a specific user and
	 *         association
	 */
	public List<Share> getSharesFromOwnerForSale(String ownerId, String assId) {
		List<Share> assShare = new ArrayList<>();
		for (Share share : shares) {
			if (share.isForSale() && share.getOwnerId().equals(ownerId) && share.getAssociationId().equals(assId)) {
				assShare.add(share);
			}
		}
		return assShare;
	}
        
}
