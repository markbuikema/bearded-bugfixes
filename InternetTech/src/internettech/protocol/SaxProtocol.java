/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.protocol;

import internettech.json.JSONArray;
import internettech.json.JSONException;
import internettech.json.JSONObject;
import internettech.manager.AssociationManager;
import internettech.manager.ShareManager;
import internettech.model.Account;
import internettech.model.Association;
import internettech.model.Exchange;
import internettech.model.SaxResponse;
import internettech.model.SaxStatus;
import internettech.model.UserAccount;

/**
 * 
 * @author Christian
 */
public final class SaxProtocol {

	private SaxProtocol() {
	}

	public static SaxResponse processRequest(String input, UserAccount user) {
		String function = input.split("\\s")[0];

		if (user != null) {
			switch (function) {
			case "MONEY_WITHDRAW":
				return withdrawMoney(input, user);
			case "MONEY_DEPOSIT":
				return depositMoney(input, user);
			case "PURCHASE_SHARE":
				return purchaseShare(input, user);
			case "SELL_SHARE":
				return sellShares(input, user);
			case "GET_SHARES":
				break;
			case "GET_ASSOCIATIONS":
				return getAssociations();
			default:
				return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
			}
		} else {
			switch (function) {
			case "CREATE_ACCOUNT":
				return register();
			case "LOGIN_ACCOUNT":
				return login(input);
			default:
				return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
			}
		}
		return null;
	}

	private static SaxResponse getAssociations() {
		try {
			SaxResponse response = new SaxResponse(SaxStatus.DATA_SUCCES);
			JSONObject root = new JSONObject();
			JSONArray array = new JSONArray();
			for (Association a : AssociationManager.getInstance().getAssociations()) {
				JSONObject association = new JSONObject();
				association.put("id", a.getId());
				association.put("name", a.getName());
				association.put("shareCount", ShareManager.getInstance().getSharesFromAss(a.getId()).size());
				array.put(association);
			}
			root.put("associations", array);
			response.setContent(root.toString());
			return response;
		} catch (JSONException e) {
			return new SaxResponse(SaxStatus.DATA_FAIL);
		}
	}

	private static SaxResponse withdrawMoney(String function, UserAccount user) {
		String[] f = function.split("\\s");
		try {
			float amount = Float.valueOf(f[1]);
			if (amount <= 0.0f) {
				return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
			}
			if (Exchange.getInstance().withdraw(user, amount)) {
				SaxResponse response = new SaxResponse(SaxStatus.MONEY_WITHDRAWN);
				response.setContent(user.toString());
				return response;
			} else {
				return new SaxResponse(SaxStatus.WITHDRAWAL_FAIL);
			}

		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
		}

	}

	private static SaxResponse depositMoney(String function, UserAccount user) {
		String[] f = function.split("\\s");
		try {
			float amount = Float.valueOf(f[1]);

			if (amount <= 0.0f) {
				return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
			}
			if (Exchange.getInstance().deposit(user, amount)) {
				SaxResponse response = new SaxResponse(SaxStatus.MONEY_STORED);
				response.setContent(user.toString());
				return response;
			} else
				return new SaxResponse(SaxStatus.DEPOSIT_FAIL);

		} catch (ArrayIndexOutOfBoundsException e) {
			return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
		}
	}

	private static SaxResponse register() {
		Account account = Exchange.getInstance().generateUserAccount();
		SaxResponse response = new SaxResponse(SaxStatus.ACCOUNT_CREATED);
		response.setContent(account.toString());
		return response;
	}

	private static SaxResponse login(String function) {
		String[] values = function.split("\\s");
		if (values.length != 3 || !isValid(values)) {
			return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
		}

		String username = values[1];
		String password = values[2];
		Account account = Exchange.getInstance().login(username, password);
		if (account != null) {
			SaxResponse response = new SaxResponse(SaxStatus.LOGIN_SUCCES);
			response.setContent(account.toString());
			return response;
		} else {
			return new SaxResponse(SaxStatus.LOGIN_FAIL);
		}
	}
        
        

	private static SaxResponse purchaseShare(String input, Account user) {
		String[] values = input.split("\\s");

		
		String buyerId = values[1];
                
                /** Only continue if buyer == user **/
                if(!buyerId.equals(user.getId())) {
                    return new SaxResponse(SaxStatus.UNAUTHORIZED);
                }
                String seller = values[2];
                String assId = values [3];
                int amount = Integer.parseInt(values[4]);
                
                
                if(Exchange.getInstance().shareTransaction(buyerId, seller, assId, amount)){
                    return new SaxResponse(SaxStatus.SHARE_PURCHASE_SUCCES);
                }

		return new SaxResponse(SaxStatus.SHARE_PURCHASE_FAIL);

	}

	private static SaxResponse sellShares(String input, UserAccount user) {
            String[] values = input.split("\\s");
            
            
            int amount = Integer.parseInt(values[1]);
            String assId = values[2];
            float price = Float.parseFloat(values[3]);
            if(ShareManager.getInstance().setSharesForSale(user.getId(), assId, amount, price)) {
                return new SaxResponse(SaxStatus.SHARE_SOLD);
            } 
            return new SaxResponse(SaxStatus.SHARE_SALE_FAIL);
        }
        
        private static boolean isValid(String[] values) {
            for(String s : values) {
                if(s.trim().isEmpty()) {
                    return false;
                }
            }
            return true;
        }
}
