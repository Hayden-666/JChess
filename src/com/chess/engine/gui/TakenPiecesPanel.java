package com.chess.engine.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class TakenPiecesPanel extends JPanel {
    private final JPanel southPanel;
    private final JPanel northPanel;

    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40,80);
    private static final Color PANEL_COLOR = Color.decode("#F5F5DC");
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecesPanel(){
        super(new BorderLayout());
        this.southPanel = new JPanel(new GridLayout(10,2));
        this.northPanel = new JPanel(new GridLayout(10,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel,BorderLayout.NORTH);
        this.add(this.southPanel,BorderLayout.SOUTH);
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }
    public void redo(final Table.MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece> BlackTakenPieces = new ArrayList<>();
        final List<Piece> WhiteTakenPieces = new ArrayList<>();
        for (final Move move: moveLog.getMoves()){
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite()) {
                    WhiteTakenPieces.add(takenPiece);
                } else if (takenPiece.getPieceAlliance().isBlack()) {
                    BlackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("should not reach here");
                }
            }
        }
        Collections.sort(WhiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        Collections.sort(BlackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        for(final Piece takenPiece: WhiteTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("art/"+
                        takenPiece.getPieceAlliance().toString().substring(0,1)+""+takenPiece.toString()+".gif"));
                ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                Image transformedImage = icon.getImage(); // transform it
                Image newimg = transformedImage.getScaledInstance(20,20 ,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                icon = new ImageIcon(newimg);  // transform it back
                imageLabel.setIcon(icon);
                this.northPanel.add(imageLabel);
            }catch(final IOException e){
                e.printStackTrace();
            }
        }
        for(final Piece takenPiece: BlackTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("art/"+
                        takenPiece.getPieceAlliance().toString().substring(0,1)+""+takenPiece.toString()+".gif"));
                ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                Image transformedImage = icon.getImage(); // transform it
                Image newimg = transformedImage.getScaledInstance(20,20 , java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                icon = new ImageIcon(newimg);  // transform it back
                imageLabel.setIcon(icon);
                this.southPanel.add(imageLabel);
            }catch(final IOException e){
                e.printStackTrace();
            }
        }
        validate();
    }

}
