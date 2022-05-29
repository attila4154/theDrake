package com.example.thedrake;


public class Offset2D {
    public final int x;
    public final int y;

    public Offset2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Offset2D copy() {
        return new Offset2D(this.x, this.y);
    }

    public boolean equalsTo(int x, int y) {
        return this.x == x && this.y == y;
    }

    public Offset2D yFlipped() {
        return new Offset2D(x, -y);
    }

    public Offset2D lengthen() {
        int newX = getX() == 0 ? getX() : getX() + 1;
        int newY = getY() == 0 ? getY() : getY() + 1;
        return new Offset2D(newX, newY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}