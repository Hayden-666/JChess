package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.boardUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece{
    public Rook(final int piecePosition,final Alliance pieceAlliance) {
        super(PieceType.ROOK, piecePosition, pieceAlliance);
    }
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {1,-1,8,-8};
    @Override
    public String toString(){
        return PieceType.ROOK.toString();
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
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return boardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 );
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return boardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1 );
    }

}
