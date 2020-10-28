package com.sg.m4vmv2.service;

import com.sg.m4vmv2.dao.VMDAO;
import com.sg.m4vmv2.dao.VendingPersistenceException;
import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VMDAOImplStub implements VMDAO {

    public Item onlyItem;

    /*ctors*/
    public VMDAOImplStub() {
        onlyItem = new Item("Arizona Iced Tea", new BigDecimal("1.00"), 1);
    }

    public VMDAOImplStub(Item onlyItem) {
        this.onlyItem = onlyItem;
    }

    /*stubs*/
    @Override
    public Item addItem(Item snackDrink) throws VendingPersistenceException {
        if (snackDrink == onlyItem) {
            return onlyItem;
        } else {
            return null;
        }
    }

    @Override
    public Item removeItem(Item snackDrink) throws VendingPersistenceException {
        if (snackDrink == onlyItem) {
            return onlyItem;
        } else {
            return null;
        }
    }

    @Override
    public Item getItem(String itemName) throws VendingPersistenceException, NoSuchItemExistsException {
        if (itemName.equals(onlyItem.getName())) {
            return onlyItem;
        } else {
            return null;
        }
    }

    @Override
    public List<Item> getInventory() throws VendingPersistenceException {
        List<Item> testInv = new ArrayList<>();
        testInv.add(onlyItem);
        
        return testInv;
    }

    @Override
    public Map<Coins, Integer> dispenseItemChange(Item snackDrink, BigDecimal userCashIn)
            throws InsufficientFundsException {
        Map<Coins, Integer> expectedChange = new HashMap<>();
        expectedChange.put(Coins.QUARTERS, 8);
        expectedChange.put(Coins.DIMES, 1);
        expectedChange.put(Coins.NICKELS, 1);
        expectedChange.put(Coins.PENNIES, 1);
        
        return expectedChange;
    }

    @Override
    public int inventoryCount() throws VendingPersistenceException {
        return 1;
    }

}
