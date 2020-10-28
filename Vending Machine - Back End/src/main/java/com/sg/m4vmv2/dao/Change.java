/*
Change class helper class
 */
package com.sg.m4vmv2.dao;

import com.sg.m4vmv2.dto.Item;
import com.sg.m4vmv2.dto.Coins;
import com.sg.m4vmv2.service.InsufficientFundsException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Change {

    /*fields*/
    private static final BigDecimal PENNY_VALUE = new BigDecimal("0.01");
    private static final BigDecimal NICKEL_VALUE = new BigDecimal("0.05");
    private static final BigDecimal DIME_VALUE = new BigDecimal("0.10");
    private static final BigDecimal QUARTER_VALUE = new BigDecimal("0.25");

    /*behaviors*/
    /**
     * Figure out how much change to dispense
     *
     * @param cashIn {BigDecimal} cash inputted by user
     * @param buying {Item} the consumable the user intends to buy
     * @return {Map} a HashMap of change in coins to be dispensed to the user
     * @throws InsufficientFundsException if user didn't input enough money
     */
    static public Map<Coins, Integer> calculateChange(Item buying, BigDecimal cashIn)
            throws InsufficientFundsException {
        Map<Coins, Integer> changeCoins = new HashMap<>();
        BigDecimal price = buying.getCost();
        BigDecimal change = cashIn.subtract(price);

        if (change.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Not enough money to buy item");
        } else {
            //RoundingMode.DOWN to simulate integer division via truncation
            int quarterCt = change.divide(QUARTER_VALUE, 0, RoundingMode.DOWN).intValue();
            changeCoins.put(Coins.QUARTERS, quarterCt);
            BigDecimal changeAfterQuarters = change.subtract(QUARTER_VALUE.multiply(new BigDecimal(String.valueOf(quarterCt))));

            int dimeCt = changeAfterQuarters.divide(DIME_VALUE, 0, RoundingMode.DOWN).intValue();
            changeCoins.put(Coins.DIMES, dimeCt);
            BigDecimal changeAfterDimes = changeAfterQuarters.subtract(DIME_VALUE.multiply(new BigDecimal(String.valueOf(dimeCt))));

            int nickelCt = changeAfterDimes.divide(NICKEL_VALUE, 0, RoundingMode.DOWN).intValue();
            changeCoins.put(Coins.NICKELS, nickelCt);
            BigDecimal changeAfterNickels = changeAfterDimes.subtract(NICKEL_VALUE.multiply(new BigDecimal(String.valueOf(nickelCt))));

            int pennyCt = changeAfterNickels.divide(PENNY_VALUE, 0, RoundingMode.DOWN).intValue();
            changeCoins.put(Coins.PENNIES, pennyCt);

            return changeCoins;
        }
    }
}
