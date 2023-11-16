package agh.ics.oop;

import java.util.Objects;

public class Vector2d implements Comparable {
    public final int x;
    public final int y;


    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        String result;
        result = "(" + this.x + "," + this.y + ")";
        return result;
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    Vector2d upperRight(Vector2d other) {
        int x = Math.max(this.x, other.x);
        int y = Math.max(this.y, other.y);
        return new Vector2d(x, y);
    }

    Vector2d lowerLeft(Vector2d other) {
        int x = Math.min(this.x, other.x);
        int y = Math.min(this.y, other.y);
        return new Vector2d(x, y);
    }

    public Vector2d add(Vector2d other) {
        int x = this.x + other.x;
        int y = this.y + other.y;
        return new Vector2d(x, y);
    }

    Vector2d subtract(Vector2d other) {
        int x = this.x - other.x;
        int y = this.y - other.y;
        return new Vector2d(x, y);
    }

    Vector2d opposite() {
        int x = -this.x;
        int y = -this.y;
        return new Vector2d(x, y);
    }


    @Override
    public int compareTo(Object o) {
        if (this.precedes((Vector2d) o)) {
            return -1;
        } else if (this.follows((Vector2d) o)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int compareX(Vector2d other) {
        return this.x - other.x;
    }

    public int compareY(Vector2d other) {
        return this.y - other.y;
    }

}
