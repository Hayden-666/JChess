package com.chess.engine.board;

public class boardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    private static boolean[] initColumn(int columnNum) {

        boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNum] = true;
            columnNum += NUM_TILES_PER_ROW;
        }while(columnNum <NUM_TILES);
        return column;
    }
    public static boolean[] initRow(int rowNumber){
        final boolean[] row = new boolean[NUM_TILES];
        do{
            row[rowNumber] = true;
            rowNumber++;
        }while(rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }
    public static boolean isvalidtilecoordinate(int candidateDestinationCoordinate) {
        return candidateDestinationCoordinate >=0 && candidateDestinationCoordinate <NUM_TILES;
    }
}
