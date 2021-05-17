package com.chess.engine.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.boardUtils;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameFrame;
    private Board chessBoard;
    private final BoardPanel boardPanel;

    private  Tile sourceTile;
    private  Tile destinationTile;
    private  Piece humanMovedPiece;

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
    private static String defaultPieceImagesPath = "art/";
    private final static Dimension OUTER_FRAME_DEMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENTION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);


    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.chessBoard = Board.createStandardBoard();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DEMENSION);
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("open up that pgn file");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem= new JMenuItem("exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
        }
        private class BoardPanel extends JPanel{
            final List<TilePanel> boardTiles;
            BoardPanel(){
                super(new GridLayout(8,8));
                boardTiles = new ArrayList<>();
                for (int i=0;i < boardUtils.NUM_TILES;i++){
                    final TilePanel tilePanel = new TilePanel(this,i);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
                setPreferredSize(BOARD_PANEL_DIMENTION);
                validate();
            }

            public void drawBoard(final Board board) {
                removeAll();
                for(TilePanel tilePanel: boardTiles){
                    tilePanel.drawTile(board);
                    add(tilePanel);
                }
                validate();
                repaint();
            }
        }
        private class TilePanel extends JPanel{
            private final int tileId;
            TilePanel(BoardPanel boardPanel,
                      int tileId){
                super(new GridBagLayout());
                this.tileId = tileId;
                setPreferredSize(TILE_PANEL_DIMENSION);
                assignTileColor();
                assignTilePieceIcon(chessBoard);
                addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        if(isRightMouseButton(e)){
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }else if(isLeftMouseButton(e)){
                            if(sourceTile == null){
                                sourceTile = chessBoard.getTile(tileId);
                                humanMovedPiece = sourceTile.getPiece();
                                if(humanMovedPiece == null){
                                    sourceTile = null;
                                }
                            }else {
                                destinationTile = chessBoard.getTile(tileId);
                                final Move move = Move.moveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                                final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                                if(transition.getMoveStatus().isDone()){
                                    chessBoard = transition.getBoard();
                                }
                                sourceTile = null;
                                destinationTile = null;
                                humanMovedPiece = null;
                            }
                            SwingUtilities.invokeLater(new Runnable()  {
                                @Override
                                public void run() {
                                    boardPanel.drawBoard(chessBoard);
                                }
                            });
                        }
                    }

                    @Override
                    public void mousePressed(final MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(final MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(final MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(final MouseEvent e) {

                    }
                });
                validate();
            }
            public void drawTile(final Board board){
                assignTileColor();
                assignTilePieceIcon(board);
                validate();
                repaint();

            }
            private void assignTilePieceIcon(Board board){
                this.removeAll();
                if(board.getTile(this.tileId).isTileOccupied()){
                    try{
                        final BufferedImage image =
                                ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
                                + board.getTile(this.tileId).getPiece().toString()+".gif"));
                        add(new JLabel(new ImageIcon(image)));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
            private void assignTileColor() {
                if (boardUtils.EIGTH_RANK[this.tileId] ||
                        boardUtils.SIXTH_RANK[this.tileId] ||
                        boardUtils.FORTH_RANK[this.tileId] ||
                        boardUtils.SECOND_RANK[this.tileId] ) {
                    setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
                }
                    if (boardUtils.SEVENRTH_RANK[this.tileId] ||
                            boardUtils.FIFTH_RANK[this.tileId] ||
                            boardUtils.THIRD_RANK[this.tileId] ||
                            boardUtils.FIRST_RANK[this.tileId] ) {
                        setBackground(this.tileId%2!=0? lightTileColor: darkTileColor);
                }

            }

        }

    }
