package agh.ics.oop.constants;

public enum GrassGrowthVariant {
    FORESTED_EQUATOR,
    TOXIC_CORPSES;

    static GrassGrowthVariant parse(int value) {
        return switch (value) {
            case 0 -> FORESTED_EQUATOR;
            case 1 -> TOXIC_CORPSES;
            default -> null;
        };
    }
}
