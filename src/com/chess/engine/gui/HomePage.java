package com.chess.engine.gui;

import com.chess.engine.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    public static int ModeIndicator = 0;
    public HomePage(){
        JLabel title = new JLabel("  CHESS 2K21 ");
        title.setFont(new Font("Apple Casual",Font.BOLD,30));
        title.setLayout(null);
        title.setForeground(Color.white);
        title.setBackground(Color.black);
        title.setBorder(BorderFactory.createLineBorder(Color.white, 8,true));
        title.setOpaque(false);
        title.setBounds(180, 10, 250, 100);


        JLabel mode = new JLabel("MODE 1:");
        mode.setFont(new Font("Apple Casual",Font.BOLD,25));
        mode.setLayout(null);
        mode.setForeground(Color.white);
        mode.setBackground(Color.black);
        mode.setOpaque(false);
        mode.setBounds(120, 120, 150, 100);

        JLabel mode2 = new JLabel("MODE 2:");
        mode2.setFont(new Font("Apple Casual",Font.BOLD,25));
        mode2.setLayout(null);
        mode2.setForeground(Color.white);
        mode2.setBackground(Color.black);
        mode2.setOpaque(false);
        mode2.setBounds(120, 280, 150, 100);

        JButton button1 = new JButton("Chess");
        button1.setFocusable(false);
        button1.setOpaque(true);
        button1.setBackground(Color.decode("#F5F5DC"));
        button1.setBounds(300, 150, 150, 50);
        button1.setFont(new Font("Times New Roman",Font.BOLD,15));
        button1.setForeground(Color.decode("#5B342E"));
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModeIndicator = 1;
                Board board = Board.createStandardBoard();
                System.out.println(board);
                Table.get().Clear();
                Table table = new Table();
                //Table.get().show();
            }
        }

        );

        JButton button2 = new JButton("Really Bad Chess");
        button2.setFocusable(false);
        button2.setOpaque(true);
        button2.setBackground(Color.decode("#F5F5DC"));
        button2.setBounds(300,300,150,50);
        button2.setFont(new Font("Times New Roman",Font.BOLD,15));
        button2.setForeground(Color.decode("#5B342E"));
        button2.setBorder(BorderFactory.createEmptyBorder());
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModeIndicator = 2;
                Board board = Board.createStandardBoard2();
                System.out.println(board);
                Table.get().Clear();
                Table table = new Table();
                //Table.get().show();
            }
        });

        this.setSize(600,450);
        this.setTitle("a boring strategic board game that only bigger brain wins");
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon("art/homepagePic.jpg")));
        setLayout(new FlowLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
         this.setVisible(true);
         this.add(title);
         this.add(mode);
         this.add(mode2);
         this.add(button1);
         this.add(button2);

    }
}
