package agh.ics.oop.constants;

public enum MapVariant {
    TUBULAR,
    HELL_PORTAL;

    static MapVariant parse(int value) {
        return switch (value) {
            case 0 -> TUBULAR;
            case 1 -> HELL_PORTAL;
            default -> null;
        };
    }
}
