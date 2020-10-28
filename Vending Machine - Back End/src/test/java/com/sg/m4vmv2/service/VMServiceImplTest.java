package com.sg.m4vmv2.service;

import com.sg.m4vmv2.dao.VendingPersistenceException;
import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import java.math.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class VMServiceImplTest {

    private VMService testServ;

    public VMServiceImplTest() {
        /*
        VMDAO testDAO = new VMDAOImplStub();
        VMAuditDAO testAuditDAO = new VMAuditDAOImplStub();

        testServ = new VMServiceImpl(testDAO, testAuditDAO);
        */
        
        ApplicationContext actx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testServ = actx.getBean("testServ", VMService.class);
    }

    /**
     * Test of stockItem method, of class VMServiceImpl.
     */
    @Test
    public void testStockItem() throws Exception {
        System.out.println("stockItem");

        //arrange
        Item newItem = new Item("Energy Drink", new BigDecimal("2.99"), 1);

        //act and assert
        try {
            testServ.stockItem(newItem);
        } catch (VendingPersistenceException
                | ItemDataValidationException e) {
            fail("Valid Item. No exception should be thrown");
        }
    }

    /**
     * Test of getInventory and inventoryCount method, of class VMServiceImpl.
     */
    @Test
    public void testGetInventoryWithCount() throws Exception {
        System.out.println("getInventory");

        //arrange
        Item onlyItem = new Item("Arizona Iced Tea", new BigDecimal("1.00"), 1);

        //act and assert
        try {
            testServ.stockItem(onlyItem);
        } catch (VendingPersistenceException
                | ItemDataValidationException e) {
            fail("Valid Item. No exception should be thrown");
        }

        assertEquals(1, testServ.getInventory().size(), "Test Inventory should only have 1 Item");
        assertTrue(testServ.getInventory().contains(onlyItem), "Test Inventory should contain Energy Drink");

        int testInventoryCount = testServ.inventoryCount(); //==1

        assertEquals(testInventoryCount, testServ.getInventory().size(), "Test Inventory count should register 1 Item");
    }

    /**
     * Test of getItem method, of class VMServiceImpl.
     */
    @Test
    public void testGetItem() throws Exception {
        System.out.println("getItem");

        //arrange
        Item onlyItem = new Item("Arizona Iced Tea", new BigDecimal("1.00"), 1);

        //act and assert
        try {
            testServ.stockItem(onlyItem);
            Item testItem = testServ.getItem(onlyItem.getName());

            assertEquals(testItem, onlyItem, "New item added should Arizona");
        } catch (VendingPersistenceException
                | ItemDataValidationException e) {
            fail("Valid Item. No exception should be thrown");
        }
    }

    /**
     * Test of sellItem method, of class VMServiceImpl. This will pass with
     * exact change
     */
    @Test
    public void testSellItemSuccess() throws Exception {
        System.out.println("sellItem - Exact");

        //arrange
        final BigDecimal exactChange = new BigDecimal("2.99");
        exactChange.setScale(2, RoundingMode.HALF_UP);
        Item newItem = new Item("Energy Drink", exactChange, 1);

        Map<Coins, Integer> expectedChange = new HashMap<>(); //should be empty

        //act and assert
        try {
            Map<Coins, Integer> testChange = testServ.sellItem(newItem, exactChange);

            assertEquals(expectedChange, testChange, "Should be getting back no change at all");
            assertEquals(newItem.getItemCount(), 0, "Quanity should be 0 after sale");
        } catch (InsufficientFundsException e) {
            fail("Should not be failing, valid change.");
        }
    }

    /**
     * Test of sellItem method, of class VMServiceImpl. This will fail as we
     * underpaid, triggering the exception
     */
    @Test
    public void testSellItemFailUnderPay() throws Exception {
        System.out.println("sellItem - Under Paid");

        //arrange
        BigDecimal underpayChange = new BigDecimal(".99");
        underpayChange.setScale(2, RoundingMode.HALF_UP);
        Item newItem = new Item("Energy Drink", new BigDecimal("2.99"), 1);
        newItem.getCost().setScale(2, RoundingMode.HALF_UP);

        //act and assert
        try {
            testServ.sellItem(newItem, underpayChange);
        } catch (InsufficientFundsException e) {
            return; //should fail
        }
    }

    /**
     * Test of sellItem method, of class VMServiceImpl. This will pass but we
     * will need to get change back
     */
    @Test
    public void testSellItemGetChange() throws Exception {
        System.out.println("sellItem - Overpaid");

        //arrange
        BigDecimal overpay = new BigDecimal("5.16");
        overpay.setScale(2, RoundingMode.HALF_UP);
        Item newItem = new Item("Energy Drink", new BigDecimal("3.00"), 1);
        newItem.getCost().setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedDifference = new BigDecimal("2.16");
        expectedDifference.setScale(2, RoundingMode.HALF_UP);

        Map<Coins, Integer> expectedChange = new HashMap<>();
        expectedChange.put(Coins.QUARTERS, 8);
        expectedChange.put(Coins.DIMES, 1);
        expectedChange.put(Coins.NICKELS, 1);
        expectedChange.put(Coins.PENNIES, 1);

        //act and assert
        try {
            Map<Coins, Integer> testChange = testServ.sellItem(newItem, overpay);
            BigDecimal changeAmount = overpay.subtract(newItem.getCost());

            assertEquals(expectedDifference, changeAmount, "Should be getting back $2.16");
            assertEquals(expectedChange, testChange, "Should be getting back 8 quarters, 1 dime, 1 nickel, 1 penny");
            assertEquals(newItem.getItemCount(), 0, "Quanity should be 0 after sale");
        } catch (InsufficientFundsException e) {
            fail("Shouldn't fail, deposited enough money to receive change");
        }
    }
}
