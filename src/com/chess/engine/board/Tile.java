package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.chess.engine.board.boardUtils.NUM_TILES;

public abstract class Tile {
    protected final int tileCoordinate;
    private static final Map<Integer,EmptyTile>EMPTY_TILE_CACHE=createAllPossibleTiles();
    private static Map<Integer,EmptyTile>createAllPossibleTiles(){
        final Map<Integer,EmptyTile>emptyTileMap = new HashMap<>();
        for(int i = 0; i < NUM_TILES; i++){
            emptyTileMap.put(i ,new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTileMap);
    }
    public static Tile createTile(final int tileCoordinate,final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) :EMPTY_TILE_CACHE.get(tileCoordinate);
    }
    private Tile(int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();
    public static final class EmptyTile extends Tile {
       private EmptyTile(final int coordinate) {
           super(coordinate);
       }
       @Override
       public String toString(){
           return "-";
       }
       @Override
        public boolean isTileOccupied(){
           return false;
       }
       @Override
       public Piece getPiece(){
           return null;
       }
    }

    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;
        private OccupiedTile(int tileCoordinate,final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase(): getPiece().toString();
        }
        @Override
        public boolean isTileOccupied(){
            return true;
        }
        @Override
        public Piece getPiece(){
            return this.pieceOnTile;
        }
    }
}
