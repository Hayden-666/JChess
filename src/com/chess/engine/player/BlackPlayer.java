package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board,
                       final Collection<Move> blackStandardLegalMoves,
                       final Collection<Move> whiteStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && ! this.isInCheck() && this.isCastled() == false){
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.calculateAttackOnTile(5,opponentLegals).isEmpty()&&
                            Player.calculateAttackOnTile(6, opponentLegals).isEmpty()&&
                            rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new Move.KingSidecastleMove(this.board,
                                this.playerKing,
                                6,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                5));
                        this.setCastleStatus(true);
                    }
                }
            }
            if(!this.board.getTile(1).isTileOccupied()&&
                    !this.board.getTile(2).isTileOccupied()&&
                    !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()&&
                        Player.calculateAttackOnTile(2,opponentLegals).isEmpty()&&
                        Player.calculateAttackOnTile(3, opponentLegals).isEmpty()&&
                        rookTile.getPiece().getPieceType().isRook()){
                    kingCastles.add(new Move.QueenSidecastleMove(this.board,
                            this.playerKing,
                            2,
                            (Rook)rookTile.getPiece(),
                            rookTile.getTileCoordinate(),
                            3));
                    this.setCastleStatus(true);
                }
            }
        }
        return kingCastles;
    }
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
}
