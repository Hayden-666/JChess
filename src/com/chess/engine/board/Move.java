package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();
    Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    @Override
    public boolean equals(final Object o){
        if(this == o) return true;
        if(!(o instanceof Move)) return false;
        final Move otherMove = (Move) o;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    @Override
    public int hashCode(){
        int result = 1;
        result = 31 * result + this.destinationCoordinate;
        result = 31 * result + this.movedPiece.hashCode();
        return result;
    }
    public int getDestinationCoordinate(){;
        return this.destinationCoordinate;
    }
    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }
    public Piece getMovedPiece(){
        return this.movedPiece;
    }
    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }
    public Board execute() {
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
    }

    public static final class MajorMove extends Move{

        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }
    public static class AttackMove extends Move{
        final Piece attackedPiece;
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, final Piece attackedpiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedpiece;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(other instanceof AttackMove){
                return false;
            }
            AttackMove attackMove = (AttackMove) other;
            return super.equals(attackMove) && getAttackedPiece().equals(attackMove.getAttackedPiece());
        }
        @Override
        public Board execute() {
            return null;
        }
        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Piece getAttackedPiece (){
            return this.attackedPiece;
        }

    }
    public static final class PawnMove extends Move{
        PawnMove(final Board board,
                 final Piece movedPiece,
                 final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    public static final class PawnAttackMove extends AttackMove{
        PawnAttackMove(final Board board,
                       final Piece movedPiece,
                       final int destinationCoordinate,
                       final Piece attackedpiece) {
            super(board, movedPiece, destinationCoordinate,attackedpiece);
        }
    }
    public static final class PawnEnPassantAttackMove extends AttackMove{
        PawnEnPassantAttackMove(final Board board,
                       final Piece movedPiece,
                       final int destinationCoordinate,
                       final Piece attackedpiece) {
            super(board, movedPiece, destinationCoordinate,attackedpiece);
        }
    }
    public static final class PawnJump extends Move{
        PawnJump(final Board board,
                 final Piece movedPiece,
                 final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public Board execute(){
            Board.Builder builder = new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int getCastleRookDestination;
        public CastleMove(final Board board,
                   final Piece movedPiece,
                   final int destinationCoordinate,
                   final Rook castleRook,
                   final int castleRookStart,
                   final int getCastleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.getCastleRookDestination = getCastleRookDestination;
        }
        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }
        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.getCastleRookDestination,this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    public static final class KingSidecastleMove extends CastleMove{
        public KingSidecastleMove(final Board board,
                           final Piece movedPiece,
                           final int destinationCoordinate,
                           final Rook castleRook,
                           final int castleRookStart,
                           final int getCastleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,getCastleRookDestination);
        }
        @Override
        public String toString(){
            return "0-0";
        }
    }
    public static final class QueenSidecastleMove extends CastleMove{
        public QueenSidecastleMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate,
                            final Rook castleRook,
                            final int castleRookStart,
                            final int getCastleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,getCastleRookDestination);
        }
        @Override
        public String toString(){
            return "0-0";
        }
    }
    public static final class NullMove extends Move{
        NullMove() {
            super(null, null, -1);
        }
        @Override
        public Board execute(){
            throw new RuntimeException("cannot execute null move");
        }
    }
    public static class moveFactory{
        private moveFactory(){
            throw new RuntimeException("moveFactory class is not instantiatable");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            for(final Move move : board.getAllLegalMoves()){
                if (move.getCurrentCoordinate()==currentCoordinate &&
                    move.getDestinationCoordinate()==destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
