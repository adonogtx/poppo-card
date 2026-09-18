package com.adonogtx.poppocard.model;

public enum LocationType {
    KONBINI(Chargeability.CHARGEABLE_AND_RECHARGEABLE),
    SUBWAY(Chargeability.CHARGEABLE_AND_RECHARGEABLE),
    VENDING_MACHINE(Chargeability.RECHARGEABLE),
    DRUGSTORE(Chargeability.CHARGEABLE),
    BAR(Chargeability.CHARGEABLE),
    RESTAURANT(Chargeability.CHARGEABLE),
    CAFE(Chargeability.CHARGEABLE);

    private final Chargeability chargeability;

    LocationType(Chargeability chargeability) {
        this.chargeability = chargeability;
    }

    public Chargeability getChargeability() {
        return chargeability;
    }
}
