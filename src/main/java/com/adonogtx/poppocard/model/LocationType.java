package com.adonogtx.poppocard.model;

public enum LocationType {
    KONBINI(true,true),
    SUBWAY(true,true),
    VENDING_MACHINE(true, false),
    DRUGSTORE(false, true),
    BAR(false, true),
    RESTAURANT(false, true),
    CAFE(false, true);

    final boolean acceptsRecharge;
    final boolean acceptsCharge;

    LocationType(boolean acceptsRecharge, boolean acceptsCharge) {
        this.acceptsRecharge = acceptsRecharge;
        this.acceptsCharge = acceptsCharge;
    }

    public boolean isAcceptsRecharge() {
        return acceptsRecharge;
    }

    public boolean isAcceptsCharge() {
        return acceptsCharge;
    }

}
