/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.protocol;

import internettech.json.JSONArray;
import internettech.json.JSONException;
import internettech.json.JSONObject;
import internettech.manager.AccountManager;
import internettech.manager.AssociationManager;
import internettech.manager.ShareManager;
import internettech.model.Account;
import internettech.model.Association;
import internettech.model.Exchange;
import internettech.model.ShareItem;
import internettech.model.UserAccount;

import java.util.List;

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
                case "BALANCE":
                    return getBalance(input, user);
                case "GET_ASSOCIATIONS":
                    return getAssociations(user);
                case "GET_USER_SHARES":
                    return getSharesOfUser(user);
                case "GET_ASSOCIATION_SHARES":
                    return getSharesFromAss(input,user);
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
    }
    
    private static SaxResponse getBalance(String input, UserAccount account) {
        String[] values = input.split("\\s");
        if(values.length != 2) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }
        String accountId = values[1];
        if(!account.getId().equals(accountId)){
            return new SaxResponse(SaxStatus.UNAUTHORIZED);
        }
        float balance = AccountManager.getInstance().retrieveUserAccount(accountId).getBalance();
         try {
            SaxResponse response = new SaxResponse(SaxStatus.DATA_SUCCES);
            JSONObject object = new JSONObject();
            object.put("id", account.getId());  
            object.put("name", account.getName());
            object.put("balance", balance);
            response.setContent(object.toString());
            return response;
        } catch (JSONException e) {
            return new SaxResponse(SaxStatus.DATA_FAIL);
        }
        
    }
    
    private static SaxResponse getSharesFromAss(String input, UserAccount account) {
        String[] values = input.split("\\s");
        
        if(values.length != 2){
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }
        
        String assId = values[1];
        try {
            SaxResponse response = new SaxResponse(SaxStatus.DATA_SUCCES);
            JSONObject root = new JSONObject();
            JSONArray array = new JSONArray();
            for (ShareItem item : ShareManager.getInstance().getSeperatedSharesFrom(assId)) {
                JSONObject object = new JSONObject();
                object.put("id", item.getOwnerId());
                object.put("name", item.getOwnerName());
                object.put("shareSaleCount", item.getAmount());
                object.put("price", item.getPrice());
                array.put(object);
            }

            root.put("shareItems", array);
            response.setContent(root.toString());
            return response;
        } catch (JSONException e) {
            return new SaxResponse(SaxStatus.DATA_FAIL);
        }
    }

    private static SaxResponse getSharesOfUser(UserAccount account) {
        try {
            SaxResponse response = new SaxResponse(SaxStatus.DATA_SUCCES);
            JSONObject root = new JSONObject();
            JSONArray array = new JSONArray();
            for (Association a : AssociationManager.getInstance().getAssociations()) {
                JSONObject ass = new JSONObject();
                ass.put("id", a.getId());
                ass.put("name", a.getName());
                ass.put("shareCount", ShareManager.getInstance().getSharesFromOwner(account.getId(), a.getId()).size());

                int ShareSaleCount = ShareManager.getInstance().getSharesFromOwnerForSale(account.getId(), a.getId()).size();
                float price = 0.00f;
                if (ShareSaleCount > 0) {
                    price = ShareManager.getInstance().getSharesFromOwnerForSale(account.getId(), a.getId()).get(0).getPrice();
                }
                ass.put("shareSaleCount", ShareSaleCount);
                ass.put("price", price);

                array.put(ass);
            }

            root.put("shares", array);
            response.setContent(root.toString());
            return response;
        } catch (JSONException e) {
            return new SaxResponse(SaxStatus.DATA_FAIL);
        }
    }

    private static SaxResponse getAssociations(UserAccount account) {
        String yourId = account.getId();
        String yourShareCount = String.valueOf(ShareManager.getInstance().getSharesFromOwner(yourId).size());
        JSONObject you = new JSONObject();
        you.put("id", account.getId());
        you.put("name", account.getName());
        you.put("shareCount", yourShareCount);
        try {
            SaxResponse response = new SaxResponse(SaxStatus.DATA_SUCCES);
            JSONObject root = new JSONObject();
            JSONArray array = new JSONArray();
            array.put(you);
            for (Association a : AssociationManager.getInstance().getAssociations()) {
                JSONObject ass = new JSONObject();
                ass.put("id", a.getId());
                ass.put("name", a.getName());
                ass.put("shareCount", ShareManager.getInstance().getSharesFromAssForSale(a.getId()).size());
                array.put(ass);
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
            } else {
                return new SaxResponse(SaxStatus.DEPOSIT_FAIL);
            }

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
        /**
         * Only continue if valid values *
         */
        if (values.length != 5 || !isValid(values)) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }

        String buyerId = values[1];
        String seller = values[2];
        String assId = values[3];

        int amount;
        try {
            amount = Integer.parseInt(values[4]);
        } catch (NumberFormatException nf) {
            return new SaxResponse(SaxStatus.NO_VALID_AMOUNT);
        }
        if (amount <= 0) {
            return new SaxResponse(SaxStatus.NO_VALID_AMOUNT);
        }

        /**
         * Only continue if buyer == user *
         */
        if (!buyerId.equals(user.getId())) {
            return new SaxResponse(SaxStatus.UNAUTHORIZED);
        }

        if (Exchange.getInstance().shareTransaction(buyerId, seller, assId, amount)) {
            return new SaxResponse(SaxStatus.SHARE_PURCHASE_SUCCES);
        } else if(ShareManager.getInstance().getSharesFromOwnerForSale(seller, assId).size() < amount){
            return new SaxResponse(SaxStatus.NOT_ENOUGH_SHARES);
        }
        return new SaxResponse(SaxStatus.SHARE_PURCHASE_FAIL);
    }

    private static SaxResponse sellShares(String input, UserAccount user) {
        String[] values = input.split("\\s");
        if (values.length != 4 || !isValid(values)) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }

        String assId = values[2];

        int amount;
        try {
            amount = Integer.parseInt(values[1]);
        } catch (NumberFormatException nf) {
            return new SaxResponse(SaxStatus.NO_VALID_AMOUNT);
        }
        if (amount <= 0) {
            return new SaxResponse(SaxStatus.NO_VALID_AMOUNT);
        }
        float price;
        try {
            price = Float.parseFloat(values[3]);
        } catch (NumberFormatException nf) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }
        if (price <= 0.00f) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }

        if (ShareManager.getInstance().setSharesForSale(user.getId(), assId, amount, price)) {
            return new SaxResponse(SaxStatus.SHARE_SELL_SUCCES);
        } 
        return new SaxResponse(SaxStatus.SHARE_SALE_FAIL);
    }

    private static boolean isValid(String[] values) {
        for (String s : values) {
            if (s.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
