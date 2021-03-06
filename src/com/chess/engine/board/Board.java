package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

import java.util.*;

public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> WhitePieces;
    private final Collection<Piece> BlackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;
private Board(final Builder builder){
    this.gameBoard = createGameBoard(builder);
    this.WhitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
    this.BlackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
    this.enPassantPawn = builder.enPassantPawn;
    final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.WhitePieces);
    final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.BlackPieces);

    this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
    this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
    this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);


}
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i=0; i<boardUtils.NUM_TILES;i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i+1)%boardUtils.NUM_TILES_PER_ROW==0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    public Player blackPlayer(){
        return this.blackPlayer;
    }
    public Player whitePlayer(){
        return this.whitePlayer;
    }
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    public Collection<Piece> getWhitePieces(){
        return this.WhitePieces;
    }
    public Collection<Piece> getBlackPieces(){
        return this.BlackPieces;
    }


    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece:pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile: gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
    return activePieces;
    }

    public Tile getTile(final int tileCoordinate){
    return gameBoard.get(tileCoordinate);
    }
private static List<Tile> createGameBoard(final Builder builder){
    final Tile[] tiles = new Tile[boardUtils.NUM_TILES];
    for(int i=0; i < boardUtils.NUM_TILES; i++){
        tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
    }
    return Arrays.asList(tiles);
}
public static Board createStandardBoard(){
    final Builder builder = new Builder();
    // BLACK SIDE
    builder.setPiece(new Rook(0,Alliance.BLACK));
    builder.setPiece(new Knight(1,Alliance.BLACK));
    builder.setPiece(new Bishop(2,Alliance.BLACK));
    builder.setPiece(new Queen(3,Alliance.BLACK));
    builder.setPiece(new King(4,Alliance.BLACK));
    builder.setPiece(new Bishop(5,Alliance.BLACK));
    builder.setPiece(new Knight(6,Alliance.BLACK));
    builder.setPiece(new Rook(7,Alliance.BLACK));
    builder.setPiece(new Pawn(8,Alliance.BLACK));
    builder.setPiece(new Pawn(9,Alliance.BLACK));
    builder.setPiece(new Pawn(10,Alliance.BLACK));
    builder.setPiece(new Pawn(11,Alliance.BLACK));
    builder.setPiece(new Pawn(12,Alliance.BLACK));
    builder.setPiece(new Pawn(13,Alliance.BLACK));
    builder.setPiece(new Pawn(14,Alliance.BLACK));
    builder.setPiece(new Pawn(15,Alliance.BLACK));
    //WHITE SIDE
    builder.setPiece(new Rook(63,Alliance.WHITE));
    builder.setPiece(new Knight(62,Alliance.WHITE));
    builder.setPiece(new Bishop(61,Alliance.WHITE));
    builder.setPiece(new King(60,Alliance.WHITE));
    builder.setPiece(new Queen(59,Alliance.WHITE));
    builder.setPiece(new Bishop(58,Alliance.WHITE));
    builder.setPiece(new Knight(57,Alliance.WHITE));
    builder.setPiece(new Rook(56,Alliance.WHITE));
    builder.setPiece(new Pawn(55,Alliance.WHITE));
    builder.setPiece(new Pawn(54,Alliance.WHITE));
    builder.setPiece(new Pawn(53,Alliance.WHITE));
    builder.setPiece(new Pawn(52,Alliance.WHITE));
    builder.setPiece(new Pawn(51,Alliance.WHITE));
    builder.setPiece(new Pawn(50,Alliance.WHITE));
    builder.setPiece(new Pawn(49,Alliance.WHITE));
    builder.setPiece(new Pawn(48,Alliance.WHITE));

    builder.setMoveMaker(Alliance.WHITE);
    return builder.build();
}
    public static Board createStandardBoard2(){
        final Builder builder = new Builder();
        // BLACK SIDE
        for (int i=0; i<16; i ++) {
            String PieceString = boardUtils.PickRandPieces(boardUtils.PieceSelection);
            if (PieceString == "BISHOP") {
                builder.setPiece(new Bishop(i, Alliance.BLACK));
            }
            if (PieceString == "KNIGHT") {
                builder.setPiece(new Knight(i, Alliance.BLACK));
            }
            if (PieceString == "QUEEN") {
                builder.setPiece(new Queen(i, Alliance.BLACK));
            }
            if (PieceString == "ROOK") {
                builder.setPiece(new Rook(i, Alliance.BLACK));
            }
            if (PieceString == "PAWN") {
                builder.setPiece(new Pawn(i, Alliance.BLACK));
            }
            if (i == 4) {
                builder.setPiece(new King(i, Alliance.BLACK));
            }
        }
        //WHITE SIDE
        for (int i=48; i<64; i ++) {
            String PieceString = boardUtils.PickRandPieces(boardUtils.PieceSelection);
            if (PieceString == "BISHOP") {
                builder.setPiece(new Bishop(i, Alliance.WHITE));
            }
            if (PieceString == "KNIGHT") {
                builder.setPiece(new Knight(i, Alliance.WHITE));
            }
            if (PieceString == "QUEEN") {
                builder.setPiece(new Queen(i, Alliance.WHITE));
            }
            if (PieceString == "ROOK") {
                builder.setPiece(new Rook(i, Alliance.WHITE));
            }
            if (PieceString == "PAWN") {
                builder.setPiece(new Pawn(i, Alliance.WHITE));
            }
            if (i == 60) {
                builder.setPiece(new King(i, Alliance.WHITE));
            }
        }

        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }
    public Iterable<Move> getAllLegalMoves(){
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
        return Collections.unmodifiableList(allLegalMoves);
    }
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    public static class Builder{
    Map<Integer, Piece> boardConfig;
    Alliance nextMoveMaker;
    Pawn enPassantPawn;
    public Builder(){
        this.boardConfig = new HashMap<>();
    }

    public Board build(){
        return new Board(this);
    }
    public Builder setPiece(final Piece piece){
        this.boardConfig.put(piece.getPiecePosition(),piece);
        return this;
    }
    public Builder removePiece(final int piecePosition){
        this.boardConfig.put(piecePosition, null);
        return this;
    }
    public Builder setMoveMaker(final Alliance nextMoveMaker) {
        this.nextMoveMaker = nextMoveMaker;
        return this;
    }
    public void setEnPassantPawn(Pawn movedPawn) {
        this.enPassantPawn = movedPawn;
    }
    }
}

