package com.chess.engine.gui;

import com.chess.engine.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    public static int ModeIndicator;
    public HomePage(){
        JButton button1 = new JButton("Chess");
        button1.setFocusable(false);
        button1.setOpaque(true);
        button1.setBackground(Color.decode("#F5F5DC"));
        button1.setBounds(100, 200, 150, 50);
        button1.setFont(new Font("Times New Roman",Font.BOLD,15));
        button1.setForeground(Color.decode("#5B342E"));
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModeIndicator = 1;
                Board board = Board.createStandardBoard();
                System.out.println(board);
                Table.get().show();
            }
        }

        );

        JButton button2 = new JButton("Really Bad Chess");
        button2.setFocusable(false);
        button2.setOpaque(true);
        button2.setBackground(Color.decode("#F5F5DC"));
        button2.setBounds(350,200,150,50);
        button2.setFont(new Font("Times New Roman",Font.BOLD,15));
        button2.setForeground(Color.decode("#5B342E"));
        button2.setBorder(BorderFactory.createEmptyBorder());
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModeIndicator = 2;
                Board board = Board.createStandardBoard2();
                System.out.println(board);
                Table.get().show();
            }
        });

        this.setSize(600,450);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon("art/homepagePic.jpg")));
        setLayout(new FlowLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
         this.setVisible(true);
         this.add(button1);
         this.add(button2);

    }
}
