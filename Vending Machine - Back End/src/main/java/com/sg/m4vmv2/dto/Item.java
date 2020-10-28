/*
Item DTO class, populates Vending Machine DTO
 */
package com.sg.m4vmv2.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {

    /*fields*/
    private String name;
    private BigDecimal cost;
    private int itemCount;

    /*ctor*/
    public Item(String name, BigDecimal cost, int itemCount) {
        this.name = name;
        this.cost = cost;
        this.itemCount = itemCount;
    }

    /*getters/setter*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    /*testing methods*/
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.cost);
        hash = 47 * hash + this.itemCount;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (this.itemCount != other.itemCount) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.cost, other.cost)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Item{" + "name=" + name + ", cost=" + cost + ", itemCount=" + itemCount + '}';
    }
}
