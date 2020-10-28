package com.sg.m4vmv2.dao;

import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import com.sg.m4vmv2.service.InsufficientFundsException;
import com.sg.m4vmv2.service.NoSuchItemExistsException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface VMDAO {

    /*snack/drink specific*/
    /**
     * Add an item to the inventory
     *
     * @param snackDrink {Item} new consumable that user can purchase
     * @return {boolean} true if successfully added, false otherwise
     * @throws VendingPersistenceException if cannot write to inventory file
     */
    Item addItem(Item snackDrink) throws VendingPersistenceException;

    /**
     * Remove an item from inventory when bought by user
     *
     * @param snackDrink {Item} an existing consumable that was bought and needs
     *                   to be removed from inventory
     * @return {boolean} if item was removed or not
     * @throws VendingPersistenceException if cannot read in or write to file
     */
    Item removeItem(Item snackDrink) throws VendingPersistenceException;

    /**
     * Validate that an item exists in inventory
     *
     * @param itemName {String} user's input
     * @return {Item} the item that corresponds with the param
     * @throws VendingPersistenceException if cannot read from inventory file
     * @throws NoSuchItemExistsException   if cannot find item in inventory
     */
    Item getItem(String itemName) throws VendingPersistenceException, NoSuchItemExistsException;

    /**
     * Get all existing items in inventory
     *
     * @return {List} all Items will be returned in an ArrayList
     * @throws VendingPersistenceException if cannot read from inventory file
     */
    List<Item> getInventory() throws VendingPersistenceException;

    /**
     * Dispense the proper change when an item is purchased/removed from
     * inventory
     *
     * @param snackDrink {Item} the item that was bought
     * @param userCashIn {BigDecimal} the total cash amount entered by the user
     * @return Map of the coins to be dispensed to the user after the full
     *         transaction
     * @throws InsufficientFundsException if not enough money was entered for
     *                                    transaction
     */
    Map<Coins, Integer> dispenseItemChange(Item snackDrink, BigDecimal userCashIn)
            throws InsufficientFundsException;

    /**
     * Tally items in inventory
     *
     * @return {int} number of items in stock
     * @throws VendingPersistenceException if cannot read from inventory file
     */
    int inventoryCount() throws VendingPersistenceException;
}
