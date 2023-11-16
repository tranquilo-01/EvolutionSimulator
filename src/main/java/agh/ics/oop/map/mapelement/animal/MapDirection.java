package agh.ics.oop.map.mapelement.animal;

import agh.ics.oop.Vector2d;

public enum MapDirection {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    public static final MapDirection[] cachedValues = values();

    public Vector2d toVector2d() {
        return switch (this) {
            case N -> new Vector2d(0, -1);
            case NE -> new Vector2d(1, -1);
            case E -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, 1);
            case S -> new Vector2d(0, 1);
            case SW -> new Vector2d(-1, 1);
            case W -> new Vector2d(-1, 0);
            case NW -> new Vector2d(-1, -1);
        };
    }

    public MapDirection turn(int move) {
        return cachedValues[(this.ordinal() + move) % MapDirection.cachedValues.length];
    }
}
