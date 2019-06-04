package com.tdc.theburgerclub.dtos;

import com.tdc.theburgerclub.enums.DipEnum;

import java.util.HashMap;

public class Dip {
    DipEnum dipEnum;
    int amount;

    public Dip() {
    }

    public Dip(DipEnum dipEnum) {
        this.dipEnum = dipEnum;
        this.amount = 0;
    }

    public Dip(HashMap<String, Object> map) {
        this.dipEnum = DipEnum.valueOf(((String) map.get("dipEnum")).toUpperCase());
        this.amount = ((Long) map.get("amount")).intValue();
    }

    public DipEnum getDipEnum() {
        return dipEnum;
    }

    public void setDipEnum(DipEnum dipEnum) {
        this.dipEnum = dipEnum;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
