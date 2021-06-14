package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.boardUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {

    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};

    public Bishop(final int piecePosition,final Alliance pieceAlliance) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance,true);
    }
    public Bishop(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance,isFirstMove);
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public Queen PawnPromo(Move move) {
        return null;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateoordinationOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (boardUtils.isvalidtilecoordinate(candidateDestinationCoordinate)) {
                if(isFirstColumnExclusion(candidateDestinationCoordinate,candidateoordinationOffset)||
                        isEightColumnExclusion(candidateDestinationCoordinate,candidateoordinationOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateoordinationOffset;
                if (boardUtils.isvalidtilecoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }
    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return boardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return boardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }
}