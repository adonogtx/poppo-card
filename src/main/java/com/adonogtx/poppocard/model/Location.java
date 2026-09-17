package com.adonogtx.poppocard.model;

import java.util.Objects;

public class Location {
    String name;
    LocationType type;

    public Location(String name, LocationType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(name, location.name) && type == location.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    public String getName() {
        return name;
    }

    public LocationType getType() {
        return type;
    }
}
