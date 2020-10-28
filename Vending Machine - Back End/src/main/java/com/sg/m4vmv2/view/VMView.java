package com.sg.m4vmv2.view;

import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class VMView {

    UserIO io;

    public VMView(UserIO io) {
        this.io = io;
    }

    /**
     * Print main menu to UI and get user's action
     *
     * @return {int} 0-4 corresponding to menu selection
     */
    public int mainMenuAndSelection() {
        io.print("***THE VENDING MACHINE***");
        io.print("1 | Buy");
        io.print("2 | Stock Machine -Admin-");
        io.print("0 | Exit");

        boolean hasErrors;
        int selection = 0;

        do {
            try {
                hasErrors = false;
                selection = io.readInt("Enter Action", 0, 2);
            } catch (NumberFormatException e) {
                displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        } while (hasErrors);

        return selection;
    }

    /*BUY*/
    /**
     * Display Buy banner for UI
     */
    public void displayBuyBanner() {
        io.print("===BUY===");
    }

    /**
     * Print inventory to UI
     *
     * @param allItems {List} all items in inventory
     */
    public void displayInventory(List<Item> allItems) {
        if (allItems.isEmpty()) {
            io.print("No items in inventory.");
        } else {
            io.print("---*---");

            allItems.stream()
                    .forEach((item) -> {
                        if (!(item.getItemCount() <= 0)) {
                            io.print(item.getName() + " | $" + item.getCost());
                        }
                    });

            io.print("-------");
        }
    }

    /**
     * If inventory is empty, program will quit out to main menu after
     * displaying this notice to UI
     */
    public void displayEmptyInventoryExitBanner() {
        io.readString("Come back soon after we've restocked! Press ENTER to continue");
    }

    /**
     * Prompt user to enter cash into the vending machine
     *
     * @return {BigDecimal} cash amount for purchasing
     */
    public BigDecimal getCash() {
        BigDecimal cash = new BigDecimal(io.readString("Enter cash amount: $").trim());
        cash.setScale(2, RoundingMode.HALF_UP);

        return cash;
    }

    /**
     * Prompt user to enter the item they want to buy
     *
     * @return {String} a string of the item name
     */
    public String getUserBuySelection() {
        return io.readString("Please enter name of product to buy: ").trim();
    }

    /**
     * Prompt user to enter cash again to pay off the remaining balance
     *
     * @return {BigDecimal} cash amount to be added to the initial input of cash
     */
    public BigDecimal getRemainingCash() {
        BigDecimal remainingCash = new BigDecimal(io.readString("Please pay remaining balance: ").trim());
        remainingCash.setScale(2, RoundingMode.HALF_UP);

        return remainingCash;
    }

    /**
     * Confirm purchase with no change back
     *
     * @param bought {Item} the purchased item
     */
    public void displayPurchaseNoChangeBanner(Item bought) {
        io.print("Here is your " + bought.getName());
        io.readString("Press ENTER to continue");
    }

    /**
     * Confirm a purchase, and print change back for user
     *
     * @param bought {Item} the purchased item
     * @param change {Map} change HashMap using COINS enum keys, and Integer
     *               values representing the number of coins to be dispensed
     */
    public void displayPurchaseWithChange(Item bought, Map<Coins, Integer> change) {
        io.print("Here is your " + bought.getName());
        io.print("Your change:");

        change.forEach((coin, number) -> System.out.println(number + " " + coin.toString()));

        io.readString("Press ENTER to continue");
    }

    /*STOCKING MACHINE*/
    /**
     * Display opening Stock Machine banner for UI
     */
    public void displayStockMachineBanner() {
        io.print("===STOCK MACHINE===");
    }

    /**
     * Get the name and cost for constructing a new consumable to be stocked
     * into the vending machine
     *
     * @return {Item} new consumable that can be purchased
     */
    public Item getNewItemInfo() {
        String itemName = io.readString("Please enter item name: ").trim();
        BigDecimal itemCost = new BigDecimal(io.readString("Please enter item cost: $").trim());
        int itemCount = io.readInt("How many are you adding to the machine?: ");
        itemCost.setScale(2, RoundingMode.HALF_UP);

        return new Item(itemName, itemCost, itemCount);
    }

    /**
     * Display closing Stock Machine banner for UI if item successfully stock
     *
     * @param totalItems {int} total items in inventory
     */
    public void displayStockMachineSuccessBanner(int totalItems) {
        io.print("Item stocked in vending machine.");
        io.print(totalItems + " product types in stock");
        io.readString("Press Enter to continue");
    }

    /*EXIT*/
    /**
     * Display Exit banner in UI
     */
    public void displayExitBanner() {
        io.print("***Thank you***");
    }

    /*EXCEPTION HANDLING*/
    /**
     * Display Unknown Command banner in UI
     */
    public void displayUnknownCommandBanner() {
        io.print("Unknown command");
    }

    /**
     * Display error message for issues with IO
     *
     * @param errorMsg {String} error message to user
     */
    public void displayErrorMessage(String errorMsg) {
        io.print("===Error===");
        io.print(errorMsg);
    }
}
