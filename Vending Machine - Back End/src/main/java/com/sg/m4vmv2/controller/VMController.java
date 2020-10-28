package com.sg.m4vmv2.controller;

import com.sg.m4vmv2.dao.VendingPersistenceException;
import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import com.sg.m4vmv2.service.InsufficientFundsException;
import com.sg.m4vmv2.service.ItemDataValidationException;
import com.sg.m4vmv2.service.NoSuchItemExistsException;
import com.sg.m4vmv2.service.VMService;
import com.sg.m4vmv2.view.VMView;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class VMController {

    private VMView view;
    private VMService service;

    public VMController(VMService service, VMView view) {
        this.service = service;
        this.view = view;
    }

    /**
     * App controller
     */
    public void run() {
        boolean inProgram = true;

        try {
            while (inProgram) {
                int menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1: {
                        buyItem();
                        break;
                    }
                    case 2: {
                        stockMachine();
                        break;
                    }
                    case 0: {
                        inProgram = false;
                        break;
                    }
                    default: {
                        unknownCommand();
                    }
                }
            }
            exitMessage();
        } catch (VendingPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Get input for menu choice
     *
     * @return {int} 0-4 for menu actions
     */
    private int getMenuSelection() {
        return view.mainMenuAndSelection();
    }

    /**
     * Display all consumables, get cash and item choice inputs, validate, and
     * finally dispense item and exact change
     *
     * @throws VendingPersistenceException if cannot load or write to inventory
     *                                     file
     * @throws NoSuchItemExistsException   if cannot find item the user intends
     *                                     to buy
     * @throws InsufficientFundsException  if user doesn't input enough money
     */
    private void buyItem() throws VendingPersistenceException {
        view.displayBuyBanner();

        //display inventory
        List<Item> inventory = service.getInventory();
        view.displayInventory(inventory);
        if (inventory.isEmpty()) {
            view.displayEmptyInventoryExitBanner();
            return; //stop buying if nothing in stock
        }

        //user cash
        boolean hasErrors;
        BigDecimal cashIn = new BigDecimal("0");
        do {
            try {
                cashIn = view.getCash();
                hasErrors = false;
            } catch (NumberFormatException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        //get item
        Item itemToBuy = null;
        do {
            String itemToBuyString = view.getUserBuySelection();

            try {
                itemToBuy = service.getItem(itemToBuyString);
                hasErrors = false;
            } catch (NoSuchItemExistsException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        //make sale
        do {
            try {
                Map<Coins, Integer> usersChange = service.sellItem(itemToBuy, cashIn);

                if (usersChange.isEmpty()) {
                    view.displayPurchaseNoChangeBanner(itemToBuy);
                } else {
                    view.displayPurchaseWithChange(itemToBuy, usersChange);
                }

                hasErrors = false;
            } catch (InsufficientFundsException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }

            //get more cash
            boolean badCash = true;
            if (hasErrors) {
                do {
                    try {
                        cashIn = cashIn.add(view.getRemainingCash());
                        badCash = false;
                    } catch (NumberFormatException e) {
                        badCash = true;
                        view.displayErrorMessage(e.getMessage());
                    }
                } while (badCash);
            }
        } while (hasErrors);
    }

    /**
     * Stock the vending machine with merchandise to sell
     *
     * @throws VendingPersistenceException if cannot load or write to inventory
     *                                     file
     */
    private void stockMachine() throws VendingPersistenceException {
        view.displayStockMachineBanner();
        boolean hasErrors;

        do {
            Item newItem = null;

            //new item info
            do {
                try {
                    newItem = view.getNewItemInfo();
                    hasErrors = false;
                } catch (NumberFormatException e) {
                    hasErrors = true;
                    view.displayErrorMessage(e.getMessage());
                }
            } while (hasErrors);

            try {
                service.stockItem(newItem);
                int itemCount = service.inventoryCount(); //display item count
                view.displayStockMachineSuccessBanner(itemCount);
                hasErrors = false;
            } catch (ItemDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
    }

    /**
     * Display banner for invalid menu choices
     */
    public void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    /**
     * Display exit banner for User exiting the program
     */
    public void exitMessage() {
        view.displayExitBanner();
    }
}
