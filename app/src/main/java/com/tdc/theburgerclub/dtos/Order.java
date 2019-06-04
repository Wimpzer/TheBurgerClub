package com.tdc.theburgerclub.dtos;

import com.tdc.theburgerclub.enums.Burger;
import com.tdc.theburgerclub.enums.BurgerOrMenu;
import com.tdc.theburgerclub.enums.Soda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order {
    private String ordererName;
    private Burger burgerName;
    private BurgerOrMenu burgerOrMenu;
    private Soda soda;
    private boolean withCheese;
    private boolean withBacon;
    private List<Dip> dips;
    private int price;

    public Order(String ordererName, Burger burgerName,BurgerOrMenu burgerOrMenu, Soda soda, boolean withCheese, boolean withBacon, List<Dip> dips, int price) {
        this.ordererName = ordererName;
        this.burgerName = burgerName;
        this.burgerOrMenu = burgerOrMenu;
        this.soda = soda;
        this.withCheese = withCheese;
        this.withBacon = withBacon;
        this.dips = dips;
        this.price = price;
    }

    public Order(HashMap<String, Object> map) {
        this.ordererName = (String) map.get("ordererName");
        this.burgerName = Burger.valueOf(((String) map.get("burgerName")).toUpperCase());
        this.burgerOrMenu = BurgerOrMenu.valueOf(((String) map.get("burgerOrMenu")).toUpperCase());
        if(map.get("soda") != null) {
            this.soda = Soda.valueOf(((String) map.get("soda")).toUpperCase());
        }
        this.withCheese = (boolean) map.get("withCheese");
        this.withBacon = (boolean) map.get("withBacon");
        List<Dip> dipsFromMap = null;
        if(map.get("dips") != null) {
            dipsFromMap = new ArrayList<>();
            for (HashMap<String, Object> dipsMap : (List<HashMap<String, Object>>) map.get("dips")) {
                Dip dip = new Dip(dipsMap);
                dipsFromMap.add(dip);
            }
        }
        this.dips = dipsFromMap;
        this.price = ((Long) map.get("price")).intValue();
    }

    public String getOrdererName() {
        return ordererName;
    }

    public Burger getBurgerName() {
        return burgerName;
    }

    public BurgerOrMenu getBurgerOrMenu() {
        return burgerOrMenu;
    }

    public Soda getSoda() {
        return soda;
    }

    public boolean isWithCheese() {
        return withCheese;
    }

    public boolean isWithBacon() {
        return withBacon;
    }

    public List<Dip> getDips() {
        return dips;
    }

    public int getPrice() {
        return price;
    }
}