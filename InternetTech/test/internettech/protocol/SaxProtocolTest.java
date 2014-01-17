/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.protocol;

import internettech.json.JSONObject;
import internettech.manager.AccountManager;
import internettech.manager.AssociationManager;
import internettech.manager.ShareManager;
import internettech.model.Association;
import internettech.model.Share;
import internettech.model.UserAccount;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christian
 */
public class SaxProtocolTest {

    private Association testAssHasSales;
    private Association testAssHasNoSales;
    private UserAccount testAccount_1;
    private UserAccount testAccount_2;
    private final float testMoney = 100.0f;

    public SaxProtocolTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        testAccount_1 = new UserAccount("test1", "123456", testMoney);
        testAccount_2 = new UserAccount("test2", "123456", testMoney);
        testAssHasNoSales = new Association("hasNo");
        testAssHasSales = new Association("hasYes");

        // Add shares to testAccount_1
        for (int i = 0; i < 10; i++) {
            Share share = new Share(testAssHasSales.getId(), testAssHasSales.getName());
            share.setOwnerId(testAccount_1.getId()); // Set owner id to this account
            share.setOwnerName(testAccount_1.getName());
            ShareManager.getInstance().save(share);
        }
        
        // Add shares to testAccount_2
        for (int i = 0; i < 10; i++) {
            Share share = new Share(testAssHasSales.getId(), testAssHasSales.getName());
            share.setOwnerId(testAccount_2.getId()); // Set owner id to this account
            share.setOwnerName(testAccount_2.getName());
            ShareManager.getInstance().save(share);
        }

        // Add shares that are not for sale to this association
        for (int i = 0; i < 10; i++) {
            Share share = new Share(testAssHasNoSales.getId(), testAssHasNoSales.getName());
            share.setForSale(false);
            ShareManager.getInstance().save(share);
        }

        
        AccountManager.getInstance().createUser(testAccount_1);
        AccountManager.getInstance().createUser(testAccount_2);
        AssociationManager.getInstance().save(testAssHasSales);
        AssociationManager.getInstance().save(testAssHasNoSales);
        
        
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testNoCommand() {
        System.out.println("test no command");
        String input = "";

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testProcessRegister() {
        System.out.println("test register");
        String method = "CREATE_ACCOUNT";
        String input = method;
        SaxResponse expResult = new SaxResponse(SaxStatus.ACCOUNT_CREATED);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        assertEquals(expResult.getStatus(), result.getStatus());
        assertNotNull(result.getContent());
    }

    @Test
    public void testProcessLoginGood() {
        
        System.out.println("test login good");
        String method = "LOGIN_ACCOUNT";
        String username = testAccount_1.getName();
        String password = testAccount_1.getPassword();
        String input = method + " " + username + " " + password;

        SaxResponse expResult = new SaxResponse(SaxStatus.LOGIN_SUCCES);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        
        System.err.println(input);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testProcessLoginFail() {
        System.out.println("test login fail wrong password");
        String method = "LOGIN_ACCOUNT";
        String username = testAccount_1.getName();
        String password = testAccount_1.getPassword() + 1;
        String input = method + " " + username + " " + password;

        SaxResponse expResult = new SaxResponse(SaxStatus.LOGIN_FAIL);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testProcessLoginPassBad() {
        System.out.println("test login no password");
        String method = "LOGIN_ACCOUNT";
        String username = testAccount_1.getName();
        String password = "";
        String input = method + " " + username + " " + password;

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testProcessLoginNameBad() {
        System.out.println("test login no username");
        String method = "LOGIN_ACCOUNT";
        String username = "";
        String password = testAccount_1.getPassword();
        String input = method + " " + username + " " + password;

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyWithdrawAllMoney() {
        System.out.println("test money withdraw all money");
        String method = "MONEY_WITHDRAW";
        String input = method + " " + testMoney;

        SaxResponse expResult = new SaxResponse(SaxStatus.MONEY_WITHDRAWN);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
        assertTrue(testAccount_1.getBalance() == 0.0f);
    }

    @Test
    public void testMoneyWithdrawSufficientMoney() {
        System.out.println("test money withdraw sufficient");
        String method = "MONEY_WITHDRAW";
        float withdrawal = testMoney / 2;
        float moneyAfter = testAccount_1.getBalance() - withdrawal;
        String input = method + " " + withdrawal;

        SaxResponse expResult = new SaxResponse(SaxStatus.MONEY_WITHDRAWN);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
        assertTrue(testAccount_1.getBalance() == moneyAfter);
    }

    @Test
    public void testMoneyWithdrawNoValidMoney() {
        System.out.println("test missing money value");
        String method = "MONEY_WITHDRAW";
        String input = method + " ";

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyWithdrawInsufficientFunds() {
        System.out.println("test insufficient funds");
        String method = "MONEY_WITHDRAW";
        String input = method + " " + (testAccount_1.getBalance() + 0.01f);

        SaxResponse expResult = new SaxResponse(SaxStatus.WITHDRAWAL_FAIL);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyWithdrawNegativeAmount() {
        System.out.println("test withdraw negative amount");
        String method = "MONEY_WITHDRAW";
        String input = method + " " + (testAccount_1.getBalance() - testAccount_1.getBalance());

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyDeposit() {
        System.out.println("test deposit money");
        String method = "MONEY_DEPOSIT";
        float moneyToStore = 1000;
        float balanceAfther = testAccount_1.getBalance() + moneyToStore;
        String input = method + " " + (moneyToStore);

        SaxResponse expResult = new SaxResponse(SaxStatus.MONEY_STORED);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
        assertTrue(testAccount_1.getBalance() == balanceAfther);
    }

    @Test
    public void testMoneyDepositNoValidMoney() {
        System.out.println("test deposit no money value");
        String method = "MONEY_DEPOSIT";
        String input = method + " ";

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyDepositNegativeAmount() {
        System.out.println("test deposit negative amount");
        String method = "MONEY_DEPOSIT";
        float moneyToStore = -1000;
        String input = method + " " + (moneyToStore);

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testPurchaseShareNoSharesForSale() {
        System.out.println("test purchase share, no shares for sale");
        String method = "PURCHASE_SHARE";
        String buyer = testAccount_1.getId();
        String seller = testAssHasNoSales.getId();
        int amount = 2;
        String assId = testAssHasNoSales.getId();

        String input = method + " " + buyer + " " + seller + " " + assId + " " + amount;
        SaxResponse expResult = new SaxResponse(SaxStatus.NOT_ENOUGH_SHARES);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testPurchaseNegativeAmount() {
        System.out.println("test purchase negative amount of shares");
        String method = "PURCHASE_SHARE";
        String buyer = testAccount_1.getId();
        String seller = testAssHasSales.getId();
        int amount = -2;
        String assId = testAssHasSales.getId();

        String input = method + " " + buyer + " " + seller + " " + assId + " " + amount;
        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_AMOUNT);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testSellShareSucces() {
        System.out.println("test sell shares good");
        String method = "SELL_SHARE";

        int amount = ShareManager.getInstance().getSharesFromOwner(testAccount_1.getId(), testAssHasSales.getId()).size();
        assertTrue(amount > 0);

        String assId = testAssHasSales.getId();
        float price = 6.0f;

        String input = method + " " + amount + " " + assId + " " + price;
        SaxResponse expResult = new SaxResponse(SaxStatus.SHARE_SELL_SUCCES);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testSellZeroShares() {
        System.out.println("test sell zero shares");
        String method = "SELL_SHARE";
        int amount = 0;
        String assId = testAssHasSales.getId();
        float price = 6.0f;

        String input = method + " " + amount + " " + assId + " " + price;
        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_AMOUNT);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testSellShareMoreSharesThanOwned() {
        System.out.println("test sell more shares than owned");
        String method = "SELL_SHARE";

        int amount = ShareManager.getInstance().getSharesFromOwner(testAccount_1.getId(), testAssHasSales.getId()).size() + 1;
        assertTrue(amount > 0);

        String assId = testAssHasSales.getId();
        float price = 6.0f;

        String input = method + " " + amount + " " + assId + " " + price;
        SaxResponse expResult = new SaxResponse(SaxStatus.SHARE_SALE_FAIL);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testSellSharesForFree() {
        System.out.println("test sell shares for free");
        String method = "SELL_SHARE";

        int amount = ShareManager.getInstance().getSharesFromOwner(testAccount_1.getId(), testAssHasSales.getId()).size();
        assertTrue(amount > 0);

        String assId = testAssHasSales.getId();
        float price = 0.00f;

        String input = method + " " + amount + " " + assId + " " + price;
        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount_1);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testProfitWhenSellingShares() {

    }

}
