package com.adonogtx.poppocard.model;

public enum LocationType {
    KONBINI(true,true),
    SUBWAY(true,true),
    VENDING_MACHINE(false, true),
    DRUGSTORE(true, false),
    BAR(true, false),
    RESTAURANT(true, false),
    CAFE(true, false);


    final boolean acceptsCharge;
    final boolean acceptsRecharge;

    LocationType(boolean acceptsCharge, boolean acceptsRecharge) {
        this.acceptsCharge = acceptsCharge;
        this.acceptsRecharge = acceptsRecharge;
    }

    public boolean isAcceptsCharge() {
        return acceptsCharge;
    }

    public boolean isAcceptsRecharge() {
        return acceptsRecharge;
    }
}
