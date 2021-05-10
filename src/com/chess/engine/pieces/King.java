package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.boardUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece{
    public King(final int piecePosition,final Alliance pieceAlliance) {
        super(PieceType.KING, piecePosition, pieceAlliance);
    }
    private static final int[] CANDIDATE_MOVE_COORDINATE = {1,7,8,9,-1,-7,-8,-9};
    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE){
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }
            if (boardUtils.isvalidtilecoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board,this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,pieceAtDestination));

                    }
                }
            }

        }
        return legalMoves;
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return boardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
            candidateOffset == 7);
}
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return boardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == -1 ||
                candidateOffset == 9);
    }
}
