package com.example.thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

///immutable class
public class TroopTile implements Tile, JSONSerializable{

    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side() {
        return side;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face() {
        return face;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop() {
        return troop;
    }

    /// Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    public TroopTile flipped(){
        return new TroopTile(this.troop, this.side, face.flip());
    }


    @Override
    public boolean canStepOn() {
        ///this tile is troop, can not be stepped on
        ///step != capture
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }


    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<TroopAction> actions = troop.actions(face());
        List<Move> moves = new ArrayList<>();

        for (TroopAction action : actions) {
            moves.addAll(action.movesFrom(pos, state.sideOnTurn(), state));
        }
        return moves;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"troop\":");
        troop.toJSON(writer);
        writer.print(",\"side\":");
        side.toJSON(writer);
        writer.print(",\"face\":");
        face.toJSON(writer);
        writer.print("}");
    }
}
