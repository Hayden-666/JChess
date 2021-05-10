package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.concurrent.Future;

public class MoveTransition {
    private final Move move;
    private final Board transitionBoard;
    private final MoveStatus moveStatus;

    public MoveTransition(Move move, Board transitionBoard, MoveStatus moveStatus) {
        this.move = move;
        this.moveStatus = moveStatus;
        this.transitionBoard = transitionBoard;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
