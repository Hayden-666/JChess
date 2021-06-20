package com.chess.engine.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.boardUtils;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.chess.engine.board.boardUtils.NUM_TILES;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private final JFrame gameFrame;
    private Board chessBoard;
    private final BoardPanel boardPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;
    private Move computerMove;

    private  Tile sourceTile;
    private  Tile destinationTile;
    private  Piece humanMovedPiece;

    private boolean highlightLegalMoves;
    private BoardDirection boardDirection;

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
    private static String defaultPieceImagesPath = "art/";
    private final static Dimension OUTER_FRAME_DEMENSION = new Dimension(600,500);
    private final static Dimension BOARD_PANEL_DIMENTION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);

    private static final Table INSTANCE = new Table();
    public Table() {
        this.gameFrame = new JFrame("CHESS 2K21");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        if( HomePage.ModeIndicator == 1 ) {
            this.chessBoard = Board.createStandardBoard();
        }
        if( HomePage.ModeIndicator == 2 ) {
            this.chessBoard = Board.createStandardBoard2();
        }
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DEMENSION);
        this.gameFrame.setLocationRelativeTo(null);
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);

        this.gameFrame.setVisible(true);
        this.highlightLegalMoves = false;
        this.boardDirection = BoardDirection.NORMAL;
    }
    public static Table get(){
        return INSTANCE;
    }
    public void Clear(){
        Table.INSTANCE.gameFrame.setVisible(false);
    }
    public void show() {
        Table.get().getMoveLog().Clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }
    private GameSetup getGameSetup() {
        return this.gameSetup;
    }
    private Board getGameBoard(){
        return this.chessBoard;
    }
    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPrefereneMenu());
        tableMenuBar.add(createOptionMenu());
        return tableMenuBar;
    }
    private JMenu createPrefereneMenu(){
        final JMenu preferenceMenu = new JMenu("preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("flit board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferenceMenu.add(flipBoardMenuItem);
        preferenceMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHilighterCheckBox = new JCheckBoxMenuItem("highlight legal moves",false);
        legalMoveHilighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves =legalMoveHilighterCheckBox.isSelected();
            }
        });
        preferenceMenu.add(legalMoveHilighterCheckBox);
        return preferenceMenu;
    }
    private JMenu createOptionMenu(){
        final JMenu optionMenu = new JMenu("options");
        final JMenuItem setupMenuItem = new JMenuItem("setup game");
        setupMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        optionMenu.add(setupMenuItem);
        return optionMenu;
    }
    public void setupUpdate (final GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);
    }
    private static class TableGameAIWatcher implements Observer{
        @Override
        public void update(Observable o, Object arg) {
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer())
                && !Table.get().getGameBoard().currentPlayer().isInCheckmate() &&
                !Table.get().getGameBoard().currentPlayer().isInStalemate()){
                //create AI thread
                //execute AI work
                final AIThinkTank aiThinkTank= new AIThinkTank();
                aiThinkTank.execute();
            }
            if(Table.get().getGameBoard().currentPlayer().isInStalemate()){
                System.out.println("Game over " + Table.get().getGameBoard().currentPlayer() + " is in Stalemate");
            }
            if(Table.get().getGameBoard().currentPlayer().isInCheckmate()){
                System.out.println("Game over " + Table.get().getGameBoard().currentPlayer() + " is in Checkmate");
            }
        }
    }
    private static class AIThinkTank extends SwingWorker <Move, String>{

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MiniMax(4);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }
        @Override
        public void done() {
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void moveMadeUpdate(PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private void updateComputerMove(final Move bestMove) {
        this.computerMove = bestMove;
    }

    public void updateGameBoard(Board transitionBoard) {
        this.chessBoard = transitionBoard;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem exitMenuItem= new JMenuItem("exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        final JMenuItem backToHomePageMenuItem= new JMenuItem("back to home page");
        backToHomePageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.setVisible(false);
            }
        });
        fileMenu.add(backToHomePageMenuItem);
        fileMenu.add(exitMenuItem);
        return fileMenu;
        }
        public enum BoardDirection{
            NORMAL{
                @Override
                List<TilePanel>traverse(final List<TilePanel> boardTiles){
                    return boardTiles;
                }

                @Override
                BoardDirection opposite() {
                    return FLIPPED;
                }
            },
            FLIPPED {
                @Override
                List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                    return Lists.reverse(boardTiles);
                }

                @Override
                BoardDirection opposite() {
                    return NORMAL;
                }
            };
            abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
            abstract BoardDirection opposite();
        }

        private class BoardPanel extends JPanel{
            final List<TilePanel> boardTiles;
            BoardPanel(){
                super(new GridLayout(8,8));
                boardTiles = new ArrayList<>();
                for (int i=0;i < NUM_TILES;i++){
                    final TilePanel tilePanel = new TilePanel(this,i);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
                setPreferredSize(BOARD_PANEL_DIMENTION);
                validate();
            }

            public void drawBoard(final Board board) {
                removeAll();
                for(TilePanel tilePanel: boardDirection.traverse(boardTiles)){
                    tilePanel.drawTile(board);
                    add(tilePanel);
                }
                validate();
                repaint();
                //System.out.println("drawBoard performed");
            }
        }
        public static class MoveLog{
        private final List<Move> moves;
        public MoveLog(){
            this.moves  = new ArrayList<>();
        }
        public List<Move> getMoves(){
            return this.moves;
        }
        public void addMove(Move move){
            this.moves.add(move);
        }
        public int Size(){
            return moves.size();
        }
        public void Clear(){
            this.moves.clear();
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(Move move){
            return this.moves.remove(move);
        }

        }
        public enum PlayerType {
        HUMAN,
        COMPUTER
    }
        private class TilePanel extends JPanel{
            private final int tileId;
            TilePanel(BoardPanel boardPanel,
                      int tileId){
                super(new GridBagLayout());
                this.tileId = tileId;
                this.setPreferredSize(TILE_PANEL_DIMENSION);
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
                            System.out.println("mouseclick detected");
                            if(sourceTile == null){
                                //（first click)
                                sourceTile = chessBoard.getTile(tileId);
                                humanMovedPiece = sourceTile.getPiece();
                                if(humanMovedPiece == null){
                                    sourceTile = null;
                                }

                            }else {
                                //move to destination coordinate (second click)
                                destinationTile = chessBoard.getTile(tileId);
                                final Move move = Move.moveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                                final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                                System.out.println(transition.getMoveStatus());
                                if(transition.getMoveStatus().isDone()){
                                    chessBoard = transition.getTransitionBoard();
                                    moveLog.addMove(move);
                                    Border selectBorder = BorderFactory.createLineBorder(Color.green);
                                    setBorder(selectBorder);
                                    try {
                                        SoundEffect soundEffect = new SoundEffect();
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    } catch (UnsupportedAudioFileException unsupportedAudioFileException) {
                                        unsupportedAudioFileException.printStackTrace();
                                    } catch (LineUnavailableException lineUnavailableException) {
                                        lineUnavailableException.printStackTrace();
                                    }
                                }
                                sourceTile = null;
                                destinationTile = null;
                                humanMovedPiece = null;

                            }
                            SwingUtilities.invokeLater(new Runnable()  {
                                @Override
                                public void run() {
                                    gameHistoryPanel.redo(chessBoard, moveLog);
                                    takenPiecesPanel.redo(moveLog);
                                    if (gameSetup.isAIPlayer(chessBoard.currentPlayer())) {
                                        Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                    }
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
            private void highlightTileBorder(final Board board) {
                if(humanMovedPiece != null &&
                        humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance() &&
                        humanMovedPiece.getPiecePosition() == this.tileId) {
                    setBorder(BorderFactory.createLineBorder(Color.green));
                } else {
                    setBorder(BorderFactory.createEmptyBorder());
                }
            }
            private void highlightLegalMoves(final Board board){
                if(highlightLegalMoves){
                    for(Move move:pieceLegalMoves(board)){
                        if (move.getDestinationCoordinate()==this.tileId){
                            try{
                                add(new JLabel(new ImageIcon(ImageIO.read(new File("art/green_dot.png")))));
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
            private Collection<Move> pieceLegalMoves(final Board board){
                if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                    return humanMovedPiece.calculateLegalMoves(board);
                }
                return Collections.emptyList();
            }

            public void drawTile(final Board board){
                assignTileColor();
                assignTilePieceIcon(board);
                highlightLegalMoves(board);
                highlightTileBorder(board);
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

