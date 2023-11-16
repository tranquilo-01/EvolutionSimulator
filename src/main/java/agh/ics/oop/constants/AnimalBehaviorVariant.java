package agh.ics.oop.constants;

public enum AnimalBehaviorVariant {
    FULL_PREDESTINATION,
    A_LITTLE_MADNESS;

    static AnimalBehaviorVariant parse(int value) {
        return switch (value) {
            case 0 -> FULL_PREDESTINATION;
            case 1 -> A_LITTLE_MADNESS;
            default -> throw new IllegalArgumentException("Invalid value for AnimalBehaviorVariant: " + value);
        };
    }
}
