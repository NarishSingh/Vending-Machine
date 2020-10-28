package com.sg.m4vmv2.service;

import com.sg.m4vmv2.dao.VMAuditDAO;
import com.sg.m4vmv2.dao.VMDAO;
import com.sg.m4vmv2.dao.VendingPersistenceException;
import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VMServiceImpl implements VMService {

    private VMDAO dao;
    private VMAuditDAO auditDAO;

    /*CTOR*/
    public VMServiceImpl(VMDAO dao, VMAuditDAO auditDAO) {
        this.dao = dao;
        this.auditDAO = auditDAO;
    }

    /*INTERFACE IMPL*/
    @Override
    public void stockItem(Item snackDrink) throws ItemDataValidationException, VendingPersistenceException {
        validateNewItemData(snackDrink);
        dao.addItem(snackDrink);
    }

    @Override
    public List<Item> getInventory() throws VendingPersistenceException {
        return dao.getInventory();
    }

    @Override
    public Item getItem(String itemName) throws VendingPersistenceException,
            NoSuchItemExistsException {
        try {
            return dao.getItem(itemName);
        } catch (NoSuchItemExistsException e) {
            throw new NoSuchItemExistsException("No such item in inventory", e);
        }
    }

    @Override
    public Map<Coins, Integer> sellItem(Item snackDrink, BigDecimal userCashIn)
            throws VendingPersistenceException, InsufficientFundsException {
        Map<Coins, Integer> change = new HashMap<>();

        if (snackDrink.getCost().compareTo(userCashIn) == 0) {
            //exact/no change
            try {
                //edit item quantities
                dao.removeItem(snackDrink);
                int itemCount = snackDrink.getItemCount();
                snackDrink.setItemCount(--itemCount);
                dao.addItem(snackDrink);

                return change; //empty
            } catch (VendingPersistenceException e) {
                throw new VendingPersistenceException("Could not load or write to inventory file");
            }
        } else if (snackDrink.getCost().compareTo(userCashIn) < 0) {
            //need change
            try {
                change = dao.dispenseItemChange(snackDrink, userCashIn);

                //edit item quantities
                dao.removeItem(snackDrink);
                int itemCount = snackDrink.getItemCount();
                snackDrink.setItemCount(--itemCount);
                dao.addItem(snackDrink);

                return change;
            } catch (VendingPersistenceException e) {
                throw new VendingPersistenceException("Could not load or write to inventory file");
            }
        } else {
            //underpaid
            throw new InsufficientFundsException("Not enough money to buy item");
        }
    }

    @Override
    public int inventoryCount() throws VendingPersistenceException {
        return dao.inventoryCount();
    }

    /**
     * Validate that a new item has a valid name and price above 0
     *
     * @param item {Item} an item constructed from view
     * @throws ItemDataValidationException
     */
    private void validateNewItemData(Item item) throws ItemDataValidationException {
        if (item.getName() == null
                || item.getName().trim().length() == 0
                || item.getCost().compareTo(new BigDecimal("0.01")) <= 0) {
            throw new ItemDataValidationException("ERROR: invalid item, must have valid name and cost");
        }
    }
}
