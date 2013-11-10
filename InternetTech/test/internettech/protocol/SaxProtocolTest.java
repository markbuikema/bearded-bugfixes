/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internettech.protocol;

import internettech.json.JSONObject;
import internettech.manager.ShareManager;
import internettech.model.Association;
import internettech.model.SaxResponse;
import internettech.model.SaxStatus;
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
    private UserAccount testAccount;
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
        String method = "CREATE_ACCOUNT";
        String input = method;
        SaxResponse accountContent = SaxProtocol.processRequest(input, null);
        JSONObject obj = new JSONObject(accountContent.getContent());
        assertNotNull(obj.get("password"));
        assertNotNull(obj.getDouble("money"));
        testAccount = new UserAccount(obj.getString("username"), obj.getString("password"), testMoney);

        testAssHasNoSales = new Association("hasNo");
        testAssHasSales = new Association("hasYes");
        for (int i = 0; i < 10; i++) {
            Share share1 = new Share(testAssHasNoSales.getId());
            Share share2 = new Share(testAssHasSales.getId());
            Share share3 = new Share(testAssHasSales.getId());
            share1.setForSale(false);
            share3.setOwnerId(testAccount.getId());
            ShareManager.getInstance().storeShare(share1);
            ShareManager.getInstance().storeShare(share2);
            ShareManager.getInstance().storeShare(share3);
        }
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
        String username = testAccount.getName();
        String password = testAccount.getPassword();
        String input = method + " " + username + " " + password;

        SaxResponse expResult = new SaxResponse(SaxStatus.LOGIN_SUCCES);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testProcessLoginFail() {
        System.out.println("test login fail wrong password");
        String method = "LOGIN_ACCOUNT";
        String username = testAccount.getName();
        String password = testAccount.getPassword() + 1;
        String input = method + " " + username + " " + password;

        SaxResponse expResult = new SaxResponse(SaxStatus.LOGIN_FAIL);
        SaxResponse result = SaxProtocol.processRequest(input, null);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testProcessLoginPassBad() {
        System.out.println("test login no password");
        String method = "LOGIN_ACCOUNT";
        String username = testAccount.getName();
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
        String password = testAccount.getPassword();
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
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
        assertTrue(testAccount.getBalance() == 0.0f);
    }

    @Test
    public void testMoneyWithdrawSufficientMoney() {
        System.out.println("test money withdraw sufficient");
        String method = "MONEY_WITHDRAW";
        float withdrawal = testMoney / 2;
        float moneyAfter = testAccount.getBalance() - withdrawal;
        String input = method + " " + withdrawal;

        SaxResponse expResult = new SaxResponse(SaxStatus.MONEY_WITHDRAWN);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
        assertTrue(testAccount.getBalance() == moneyAfter);
    }

    @Test
    public void testMoneyWithdrawNoValidMoney() {
        System.out.println("test missing money value");
        String method = "MONEY_WITHDRAW";
        String input = method + " ";

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyWithdrawInsufficientFunds() {
        System.out.println("test insufficient funds");
        String method = "MONEY_WITHDRAW";
        String input = method + " " + (testAccount.getBalance() + 0.01f);

        SaxResponse expResult = new SaxResponse(SaxStatus.WITHDRAWAL_FAIL);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyWithdrawNegativeAmount() {
        System.out.println("test withdraw negative amount");
        String method = "MONEY_WITHDRAW";
        String input = method + " " + (testAccount.getBalance() - testAccount.getBalance());

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyDeposit() {
        System.out.println("test deposit money");
        String method = "MONEY_DEPOSIT";
        float moneyToStore = 1000;
        float balanceAfther = testAccount.getBalance() + moneyToStore;
        String input = method + " " + (moneyToStore);

        SaxResponse expResult = new SaxResponse(SaxStatus.MONEY_STORED);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
        assertTrue(testAccount.getBalance() == balanceAfther);
    }

    @Test
    public void testMoneyDepositNoValidMoney() {
        System.out.println("test deposit no money value");
        String method = "MONEY_DEPOSIT";
        String input = method + " ";

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testMoneyDepositNegativeAmount() {
        System.out.println("test deposit negative amount");
        String method = "MONEY_DEPOSIT";
        float moneyToStore = -1000;
        String input = method + " " + (moneyToStore);

        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testPurchaseShareSucces() {
        System.out.println("test purchase share");
        String method = "PURCHASE_SHARE";
        String buyer = testAccount.getId();
        String seller = testAssHasSales.getId();
        int amount = ShareManager.getInstance().getSharesFromOwnerForSale(testAssHasSales.getId(), testAssHasSales.getId()).size();
        String assId = testAssHasSales.getId();

        String input = method + " " + buyer + " " + seller + " " + assId + " " + amount;
        SaxResponse expResult = new SaxResponse(SaxStatus.SHARE_PURCHASE_SUCCES);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testPurchaseShareNoSharesForSale() {
        System.out.println("test purchase share, no shares for sale");
        String method = "PURCHASE_SHARE";
        String buyer = testAccount.getId();
        String seller = testAssHasNoSales.getId();
        int amount = 2;
        String assId = testAssHasNoSales.getId();

        String input = method + " " + buyer + " " + seller + " " + assId + " " + amount;
        SaxResponse expResult = new SaxResponse(SaxStatus.SHARE_PURCHASE_FAIL);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }

    @Test
    public void testPurchaseNegativeAmount() {
        System.out.println("test purchase negative amount of shares");
        String method = "PURCHASE_SHARE";
        String buyer = testAccount.getId();
        String seller = testAssHasSales.getId();
        int amount = -2;
        String assId = testAssHasSales.getId();

        String input = method + " " + buyer + " " + seller + " " + assId + " " + amount;
        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_AMOUNT);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }
    
    @Test
    public void testSellShareSucces() {
        System.out.println("test sell shares good");
        String method = "SELL_SHARE";
        
        int amount = ShareManager.getInstance().getSharesFromOwner(testAccount.getId(), testAssHasSales.getId()).size();
        assertTrue(amount > 0);
        
        String assId = testAssHasSales.getId();
        float price = 6.0f;
        
        String input = method + " " + amount + " " + assId + " " + price;
        SaxResponse expResult = new SaxResponse(SaxStatus.SHARE_SELL_SUCCES);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
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
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }
    
    @Test
    public void testSellShareMoreSharesThanOwned() {
        System.out.println("test sell more shares than owned");
        String method = "SELL_SHARE";
        
        int amount = ShareManager.getInstance().getSharesFromOwner(testAccount.getId(), testAssHasSales.getId()).size() + 1;
        assertTrue(amount > 0);
        
        String assId = testAssHasSales.getId();
        float price = 6.0f;
        
        String input = method + " " + amount + " " + assId + " " + price;
        SaxResponse expResult = new SaxResponse(SaxStatus.SHARE_SALE_FAIL);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }
    
    @Test
    public void testSellSharesForFree() {
        System.out.println("test sell shares for free");
        String method = "SELL_SHARE";
        
        int amount = ShareManager.getInstance().getSharesFromOwner(testAccount.getId(), testAssHasSales.getId()).size();
        assertTrue(amount > 0);
        
        String assId = testAssHasSales.getId();
        float price = 0.00f;
        
        String input = method + " " + amount + " " + assId + " " + price;
        SaxResponse expResult = new SaxResponse(SaxStatus.NO_VALID_COMMAND);
        SaxResponse result = SaxProtocol.processRequest(input, testAccount);
        assertEquals(expResult.getStatus(), result.getStatus());
    }
    
    

}
