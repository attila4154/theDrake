package com.example.thedrake;

import java.util.ArrayList;
import java.util.List;

public class StrikeAction extends TroopAction{
    public StrikeAction (int x, int y) {
        super(x,y);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> moves = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        if (state.canCapture(origin, target)) {
            moves.add(new CaptureOnly(origin, (BoardPos)target));
        }

        return moves;
    }
}
