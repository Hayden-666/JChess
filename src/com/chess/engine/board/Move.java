package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import java.util.Collection;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate(){;
    return this.destinationCoordinate;
    }

    public abstract Board execute();


    public static final class MajorMove extends Move{

        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }
    public static final class AttackMove extends Move{
        final Piece attackedpiece;
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, final Piece attackedpiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedpiece = attackedpiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }
}
