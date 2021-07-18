package GUI_Demo;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class FirstSwingExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame();        // creating instance of JFrame

        // JLabel label = new JLabel();
        // label.setBounds(200, 200, 100, 100);
        // frame.add(label);

        // JButton button = new JButton("Click Me");   // creating instance of JButton
        // button.setBounds(130, 100, 100, 40);

        // button.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         label.setText("You Clicked Me");
        //     }
        // });
        // frame.add(button);      // adding button in JFrame

        // JButton[] buttons = new JButton[5];
        // for (int i = 0; i < buttons.length; i++) {
        //     buttons[i] = new JButton("Click Me " + String.valueOf(i));
        //     buttons[i].setBounds(10 + 100 * i, 300, 100, 100);
        //     buttons[i].addActionListener(new ActionListener() {
        //         public void actionPerformed(ActionEvent e) {
        //             label.setText("You Clicked Me");
        //         }
        //     });
        //     frame.add(buttons[i]);
        // }


        JPanel panel = new JPanel();

        String[] str = { "3 x 3", "4 x 4", "5 x 5" };
        JComboBox<String> choices = new JComboBox<String>(str);
        choices.setBounds(50, 50, 100, 100);
        choices.setSelectedItem(0);
        choices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb = (JComboBox<String>) e.getSource();
                String choiceString = (String) cb.getSelectedItem();               
                System.out.println(choiceString);
            }
        });
        // panel.add(choices);

        frame.add(choices);
        

        frame.setSize(700, 500);    // 400 width and 500 height
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
