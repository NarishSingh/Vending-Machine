package com.sg.m4vmv2.dao;

import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.dto.Item;
import com.sg.m4vmv2.service.InsufficientFundsException;
import com.sg.m4vmv2.service.NoSuchItemExistsException;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class VMDAOImpl implements VMDAO {

    private Map<String, Item> inventory = new HashMap<>();
    public final String INVENTORY_FILE;
    public static final String DELIMITER = "::";

    /*CTORS*/
    //production
    public VMDAOImpl() {
        INVENTORY_FILE = "inventory.txt";
    }

    //testing
    public VMDAOImpl(String inventoryTextFile) {
        this.INVENTORY_FILE = inventoryTextFile;
    }

    /*INTERFACE IMPL*/
    @Override
    public Item addItem(Item snackDrink) throws VendingPersistenceException {
        loadInventory();
        Item added = inventory.put(snackDrink.getName(), snackDrink);
        writeInventory();

        return added;
    }

    @Override
    public Item removeItem(Item snackDrink) throws VendingPersistenceException {
        Item removedItem = inventory.remove(snackDrink.getName());
        writeInventory();
        return removedItem;
    }

    @Override
    public Item getItem(String itemName) throws VendingPersistenceException,
            NoSuchItemExistsException {
        loadInventory();

        Item matchingItem = inventory.get(itemName);

        if (matchingItem == null) {
            throw new NoSuchItemExistsException("No such item in inventory");
        } else {
            return matchingItem;
        }
    }

    @Override
    public List<Item> getInventory() throws VendingPersistenceException {
        loadInventory();
        return new ArrayList<>(inventory.values());
    }

    @Override
    public Map<Coins, Integer> dispenseItemChange(Item snackDrink, BigDecimal userCashIn)
            throws InsufficientFundsException {
        try {
            return Change.calculateChange(snackDrink, userCashIn);
        } catch (InsufficientFundsException e) {
            throw new InsufficientFundsException("Not enough cash to purchase item", e);
        }
    }

    @Override
    public int inventoryCount() throws VendingPersistenceException {
        loadInventory();

        Long itemCtLong = inventory.values().stream()
                .count();

        return itemCtLong.intValue();
    }

    /*DATA (UN)MARSHALLING*/
    /**
     * Marshall data from an item obj in inventory to text
     *
     * @param anItem {Item} Item obj in inventory with all fields filled in
     * @return {String} the obj as a delimited String
     */
    private String marshallItem(Item anItem) {
        String itemAsText = anItem.getName() + DELIMITER;
        itemAsText += anItem.getCost().toString() + DELIMITER;
        itemAsText += anItem.getItemCount();

        return itemAsText;
    }

    /**
     * Unmarshall data from a delimited line of text into constructing a item
     * obj
     *
     * @param itemAsText {String} delimited by ::
     * @return {Item} obj constructed with all fields filled
     */
    private Item unmarshallItem(String itemAsText) {
        String[] itemTokens = itemAsText.split(DELIMITER);

        String itemName = itemTokens[0];
        BigDecimal itemCost = new BigDecimal(itemTokens[1]);
        int itemCount = Integer.parseInt(itemTokens[2]);

        Item itemFromFile = new Item(itemName, itemCost, itemCount);

        return itemFromFile;
    }

    /**
     * Read in file and construct items in inventory from lines of delimited
     * text
     *
     * @throws VendingPersistenceException if cannot read in file
     */
    private void loadInventory() throws VendingPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(INVENTORY_FILE)));
        } catch (FileNotFoundException e) {
            throw new VendingPersistenceException("Couldn't load inventory from file", e);
        }

        String currentLine;
        Item currentItem;
        inventory.clear();

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentItem = unmarshallItem(currentLine);

            inventory.put(currentItem.getName(), currentItem);
        }

        scanner.close();
    }

    /**
     * Write all items in inventory to INVENTORY_FILE
     *
     * @throws VendingPersistenceException if cannot write to file
     */
    private void writeInventory() throws VendingPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(INVENTORY_FILE));

            inventory.values().stream()
                    .forEach((item) -> {
                        String itemAsText = marshallItem(item);
                        out.println(itemAsText);
                        out.flush();
                    });

            out.close();
        } catch (IOException e) {
            throw new VendingPersistenceException("Could not write to inventory file", e);
        }
    }
}
