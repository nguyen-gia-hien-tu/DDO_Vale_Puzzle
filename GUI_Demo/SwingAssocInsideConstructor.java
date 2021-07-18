package GUI_Demo;

import javax.swing.*;

import src.Board;

import java.awt.event.*;

public class SwingAssocInsideConstructor {
    private JFrame frame;
    private JButton[][] buttons;

    SwingAssocInsideConstructor() {
        frame = new JFrame();
        buttons = new JButton[Board.getSize()][Board.getSize()];


        final JTextField textField = new JTextField();
        textField.setBounds(50, 50, 150, 20);

        JButton button = new JButton("Click Me");
        button.setBounds(150, 100, 100, 40);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText("Hello World!");
            }
        });

        frame.add(button);
        frame.add(textField);
        
        frame.setSize(400, 500);    // 400 width and 500 height
        frame.setLayout(null);      // using no layout manager
        frame.setVisible(true);     // making the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        new SwingAssocInsideConstructor();
    }
}
