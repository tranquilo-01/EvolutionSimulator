package agh.ics.oop.constants;

public enum AnimalMutationVariant {
    FULL_RANDOMNESS,
    SLIGHT_CORRECTION;

    static AnimalMutationVariant parse(int value) {
        return switch (value) {
            case 0 -> FULL_RANDOMNESS;
            case 1 -> SLIGHT_CORRECTION;
            default -> null;
        };
    }
}
