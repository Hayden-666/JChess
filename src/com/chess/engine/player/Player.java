package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final Collection<Move> legalMoves;
    protected final King playerKing;
    private final boolean isInCheck;

    protected Player(final Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishking();
        this.legalMoves = legalMoves;
        this.legalMoves.addAll(calculateKingCastles(legalMoves,opponentMoves));
        //System.out.println(this.legalMoves);
        this.isInCheck = !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();

    }
    public King getPlayerKing(){
        return this.playerKing;
    }
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }
    protected static Collection<Move> calculateAttackOnTile(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (Move move:opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return attackMoves;
    }

    public boolean isLegalMove(Move move){
        System.out.println(this.getAlliance() + " contains legal move?" + this.legalMoves.contains(move));
        System.out.println(move.getCurrentCoordinate());
        System.out.println(move.getDestinationCoordinate());

        return this.legalMoves.contains(move);
    }
    public boolean isInCheck(){
        return this.isInCheck;
    }
    public boolean isInCheckmate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves(){
        for (final Move move: this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    };

    public boolean isInStalemate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    public boolean isCastled(){
        return false;
    }
    public MoveTransition makeMove(Move move){
        if(!isLegalMove(move)){
            return new MoveTransition(move, this.board ,MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttack = Player.calculateAttackOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttack.isEmpty()){
            return new MoveTransition(move, this.board, MoveStatus.LEAVE_PLAYER_IN_CHECK);
        }
        return new MoveTransition(move,transitionBoard,MoveStatus.DONE);
    }

    private King establishking() {
        for(Piece piece: getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("should not reach here! Not a valid Board!");
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);
}
