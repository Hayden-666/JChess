package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;
    public static final Move NULL_MOVE = new NullMove();
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board,final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    @Override
    public boolean equals(final Object o){
        if(this == o) return true;
        if(!(o instanceof Move)) return false;
        final Move otherMove = (Move) o;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    @Override
    public int hashCode(){
        int result = 1;
        result = 31 * result + this.destinationCoordinate;
        result = 31 * result + this.movedPiece.hashCode();
        result = 31 * result + this.movedPiece.getPiecePosition();
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
            if((boardUtils.FIRST_RANK[this.destinationCoordinate] && this.movedPiece.isPawn() && this.movedPiece.getPieceAlliance().isBlack()) ||
                (boardUtils.EIGTH_RANK[this.destinationCoordinate] && this.movedPiece.isPawn() && this.movedPiece.getPieceAlliance().isWhite())){
                System.out.println("promotion condition met");
                builder.setPiece(this.movedPiece.PawnPromo(this));
            } else if((!(boardUtils.FIRST_RANK[destinationCoordinate] && boardUtils.EIGTH_RANK[this.destinationCoordinate]) && this.movedPiece.isPawn())
                        || (!this.movedPiece.isPawn())){
                System.out.println("* " + this.destinationCoordinate + " " + this.movedPiece.isPawn());
                builder.setPiece(this.movedPiece.movePiece(this));
            }
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            System.out.println("currentMoveMaker;" + this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
    }

    public static final class MajorMove extends Move{

        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
       @Override
        public boolean equals(final Object other){
           return this == other || other instanceof MajorMove && super.equals(other);
        }
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + boardUtils.getPositionAtCoordinate(this.destinationCoordinate);
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
            if(!(other instanceof AttackMove)){
                return false;
            }
            AttackMove attackMove = (AttackMove) other;
            return super.equals(attackMove) && getAttackedPiece().equals(attackMove.getAttackedPiece());
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
    public static class MajorAttackMove
            extends AttackMove {

        public MajorAttackMove(final Board board,
                               final Piece pieceMoved,
                               final int destinationCoordinate,
                               final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);

        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + boardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    public static final class PawnMove extends Move{
        public PawnMove(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object o){
            return this == o || o instanceof PawnMove && super.equals(o);
        }
        @Override
        public String toString(){
            return boardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    public static final class PawnAttackMove extends AttackMove{
        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedpiece) {
            super(board, movedPiece, destinationCoordinate,attackedpiece);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }
        @Override
        public String toString(){
            return boardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    boardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    public static final class PawnEnPassantAttackMove extends AttackMove{
        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movedPiece,
                                       final int destinationCoordinate,
                                       final Piece attackedpiece) {
            super(board, movedPiece, destinationCoordinate,attackedpiece);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }
        @Override
        public Board execute(){
            final Board.Builder builder = new Board.Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(this.movedPiece!=piece){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if (this.getAttackedPiece() != piece){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    public static final class PawnJump extends Move{
        public PawnJump(final Board board,
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
        @Override
        public String toString(){
            return boardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int CastleRookDestination;
        public CastleMove(final Board board,
                   final Piece movedPiece,
                   final int destinationCoordinate,
                   final Rook castleRook,
                   final int castleRookStart,
                   final int CastleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.CastleRookDestination = CastleRookDestination;
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
            builder.setPiece(new Rook(this.CastleRookDestination,this.castleRook.getPieceAlliance()));
            builder.removePiece(castleRookStart);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.CastleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
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
        public boolean equals(final Object o){
            return this == o || o instanceof KingSidecastleMove && super.equals(o);
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
        public boolean equals(final Object o){
            return this == o || o instanceof QueenSidecastleMove && super.equals(o);
        }
        @Override
        public String toString(){
            return "0-0";
        }
    }
    public static final class NullMove extends Move{
        NullMove() {
            super(null, 65);
        }
        @Override
        public Board execute(){
            throw new RuntimeException("cannot execute null move");
        }
        @Override
        public int getCurrentCoordinate(){
            return -1;
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
                    System.out.println("createMove method executed");
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
