package com.sg.m4vmv2.service;

import com.sg.m4vmv2.dao.VendingPersistenceException;
import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface VMService {

    /**
     * Validate a newly constructed item and stock it in inventory
     *
     * @param snackDrink {Item} a item obj created by admin
     * @throws ItemDataValidationException if item is invalid
     * @throws VendingPersistenceException if cannot stock/persist item to
     *                                     inventory file
     */
    void stockItem(Item snackDrink) throws ItemDataValidationException, VendingPersistenceException;

    /**
     * Return the current inventory
     *
     * @return {List} all items available for purchase
     * @throws VendingPersistenceException if cannot read from inventory file
     */
    List<Item> getInventory() throws VendingPersistenceException;

    /**
     * Get an item from inventory
     *
     * @param itemName {String} name of item that user wants to buy
     * @return {Item} the item that corresponds with the name inputted
     * @throws VendingPersistenceException if cannot read from inventory file
     * @throws NoSuchItemExistsException   if no item exists matching the name
     *                                     inputted by user
     */
    Item getItem(String itemName) throws VendingPersistenceException, NoSuchItemExistsException;

    /**
     * Sell an existing item from inventory, removing it, and returning the
     * proper change if necessary
     *
     * @param snackDrink {Item} the item to be sold
     * @param userCashIn {BigDecimal} cash inputted by user
     * @return {Map} change for the user as a HashMap of coins, will return
     *         empty if no change to be dispensed
     * @throws VendingPersistenceException if cannot read from or write to
     *                                     inventory file
     * @throws InsufficientFundsException  if user has not inputted enough money
     *                                     into the vending machine
     */
    Map<Coins, Integer> sellItem(Item snackDrink, BigDecimal userCashIn)
            throws VendingPersistenceException, InsufficientFundsException;

    /**
     * Tally items in inventory
     *
     * @return {int} number of items currently in stock
     * @throws VendingPersistenceException if cannot read from inventory file
     */
    int inventoryCount() throws VendingPersistenceException;
}
