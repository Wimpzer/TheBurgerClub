package com.tdc.theburgerclub.enums;

public enum Burger {
    CLASSIC("Classic"),
    LETSRUMBLE("Let\'s Rumble"),
    RUMBLEINTHEJUNGLE("Rumble in the jungle"),
    GREENBEAST("Green beast Veggie"),
    POLLO("Pollo burger"),
    GORILLA("Gorilla");

    private String name;

    Burger(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Burger fromString(String text) {
        for (Burger b : Burger.values()) {
            if (b.name.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
