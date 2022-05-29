package com.example.thedrake;

import java.io.PrintWriter;

public enum TroopFace implements JSONSerializable {
    AVERS,
    REVERS;

    public TroopFace flip() {
        if (this == AVERS) return REVERS;
        return AVERS;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"" + this + "\"");
    }
}