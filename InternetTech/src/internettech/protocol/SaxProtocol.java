/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.protocol;

import internettech.model.Account;
import internettech.model.Exchange;
import internettech.model.SaxResponse;
import internettech.model.SaxStatus;

/**
 *
 * @author Christian
 */
public final class SaxProtocol {

    private SaxProtocol() {
    }

    public static SaxResponse processRequest(String input, Account user) {
        String function = input.split("\\s")[0];

        if (user != null) {
            switch (function) {
                case "MONEY_WITHDRAW":
                    return withdrawMoney(input, user);
                case "MONEY_DEPOSIT":
                    return depositMoney(input, user);
                case "PURCHASE_SHARE":
                    break;
                case "SELL_SHARE":
                    break;
                case "GET_SHARES":
                    break;
                case "GET_ASSOCIATIONS":
                    break;
                default:
                    return new SaxResponse(SaxStatus.NO_VALID_COMMAND
                    );
            }
        } else {
            switch (function) {
                case "CREATE_ACCOUNT":
                    return register();
                case "LOGIN_ACCOUNT":
                    return login(input);
                default:
                    return new SaxResponse(SaxStatus.UNAUTHORIZED);
            }
        }
        return null;
    }

    private static SaxResponse withdrawMoney(String function, Account user) {
        String[] f = function.split("\\s");
        try {
            long date = Long.valueOf(f[1]);
            float amount = Float.valueOf(f[2]);
            if (amount <= 0.0f) {
                return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
            }
            if (Exchange.getInstance().withdraw(user, amount)) {
                return new SaxResponse(SaxStatus.MONEY_WITHDRAWN);
            } else {
                return new SaxResponse(SaxStatus.WITHDRAWAL_FAIL);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }

    }

    private static SaxResponse depositMoney(String function, Account user) {
        String[] f = function.split("\\s");
        try {
            long date = Long.valueOf(f[1]);
            float amount = Float.valueOf(f[2]);

            if (amount <= 0.0f) {
                return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
            }
            Exchange.getInstance().deposit(user, amount);
            return new SaxResponse(SaxStatus.MONEY_STORED);

        } catch (ArrayIndexOutOfBoundsException e) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }
    }

    private static SaxResponse register() {
        Account account = Exchange.getInstance().generateAccount();
        SaxResponse response = new SaxResponse(SaxStatus.ACCOUNT_CREATED);
        response.setContent(account.toString());
        return response;
    }

    private static SaxResponse login(String function) {
        String[] values = function.split("\\s");

        if (values.length != 4) {
            return new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        }

        long date = Long.valueOf(values[1]);
        String username = values[2];
        String password = values[3];
        Account account = Exchange.getInstance().login(username, password);
        if (account != null) {
            SaxResponse response = new SaxResponse(SaxStatus.LOGIN_SUCCES);
            response.setContent(account.toString());
            return response;
        } else {
            return new SaxResponse(SaxStatus.LOGIN_FAIL);
        }

    }

}
