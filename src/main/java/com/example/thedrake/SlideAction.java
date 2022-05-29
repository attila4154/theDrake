package com.example.thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {
    public SlideAction(int x, int y) {
        super(x, y);
    }

    /**
     * using slide action figure can go
     * in the direction of given offset
     * (like queen in chess)
     */
    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> moves = new ArrayList<>();
        TilePos curTarget = origin.stepByPlayingSide(offset(), side);
        /**
         * checking what moves can be made in the direction of the given offset
         * loop ends if tile went off board in the direction of the offset
         * or if there is an opposite troop at that tile
         */
        while (curTarget != BoardPos.OFF_BOARD ) {
//            if (state.canStepFromTo(origin, curTarget, offset())) {
            if (state.canStep(origin, curTarget)) {
                moves.add(new StepOnly(origin, (BoardPos) curTarget));
            } else if (state.canCapture(origin, curTarget)) {
                moves.add(new StepAndCapture(origin, (BoardPos) curTarget));
                break;
            } else if (!state.canStepTo(curTarget)) break;
            curTarget = curTarget.stepByPlayingSide(offset(), side);
        }

        return moves;
    }
}
