/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.model;

/**
 *
 * @author Christian, Mark
 */
public enum SaxStatus {

    /**
     * SUCCESS *
     */
    CONNECTION_CREATED(1.1f, "connection created"),
    ACCOUNT_CREATED(1.2f, "account created"),
    LOGIN_SUCCES(1.3f, "login succes"),
    SHARE_PURCHASED(1.4f, "share purchased"),
    SHARE_SOLD(1.5f, "share sold"),
    MONEY_STORED(1.6f, "money stored"),
    MONEY_WITHDRAWN(1.7f, "money withdrawn"),
    DATA_SUCCES(1.8f, "data succes"),
    /**
     * FAILURE *
     */
    NO_MORE_ACCOUNTS(2.1f, "no more accounts available"),
    LOGIN_FAIL(2.2f, "login fail: incorrect credentials"),
    SHARE_PURCHASE_FAIL(2.3f, "insufficient funds"),
    SHARE_SALE_FAIL(2.4f, "share not found"),
    WITHDRAWAL_FAIL(2.5f, "insufficient funds"),
    /**
     * COMMAND FAILED *
     */
    NO_VALID_COMMAND(3.1f, "command does not exist"),
    UNAUTHORIZED(3.2f, "not logged in"),
    /**
     * CONNECTION FAILED *
     */
    CONNECTION_FAIL(4.1f, "could not connect");

    private final float statusCode;
    private final String statusMessage;

    SaxStatus(float status, String name) {
        this.statusMessage = name;
        this.statusCode = status;
    }

    public float getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
