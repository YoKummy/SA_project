package SA_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class frame extends JFrame implements ActionListener{

    String[] room = {
        "1 person room",
        "2 persons room",
        "4 persons room"
    };
    
    private JPanel contentPane;
    private JLabel inputJLabel;
    private JLabel outputLabel;
    JComboBox<String> roomType = new JComboBox<>(room);
    private JTextField input;
    private JTextField outputText;
    private JButton book;
    private JButton clear;

    
    frame(){
        contentPane = new JPanel();

        setBounds(200, 200, 800, 500);
        setTitle("Book System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        inputJLabel = new JLabel();
        inputJLabel.setText("Room size: ");
        inputJLabel.setBounds(20, 20, 150, 20);


        roomType.setSelectedIndex(0);
        roomType.setBounds(130, 20, 400, 20);
        /* input = new JTextField();
        input.setText("");
        input.setBounds(130, 20, 400, 20);
        input.setColumns(20);
        input.setEditable(true); */

        outputLabel = new JLabel();
        outputLabel.setBounds(560, 20, 200, 20);
        outputLabel.setText("Available room:");

        outputText = new JTextField();
        outputText.setBounds(560, 40, 200, 300);
        outputText.setText("");
        outputText.setColumns(20);
        outputText.setEditable(false);

        book = new JButton();
        book.setText("book");
        book.setBounds(20, 60, 80, 20);
        book.addActionListener(this);

        clear = new JButton();
        clear.setText("clear");
        clear.setBounds(110, 60, 80, 20);
        clear.addActionListener(this);

        contentPane.add(inputJLabel);
        contentPane.add(roomType);
        contentPane.add(outputLabel);
        contentPane.add(outputText);
        contentPane.add(book);
        contentPane.add(clear);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == book) {
        JOptionPane.showMessageDialog(this, "Successful!");
        if (e.getSource() == clear) {
            input.setText("");
            outputText.setText("");
        }
       }
    }
    
    /* private String getRandomAnswer(){
    String[] answers = {
        
    };
    Random random = new Random();
    int index = random.nextInt(answers.length);
    return answers[index];
    } */
}

public class bookSystem {
    public static void main(String[] args) {
        new frame();
    }
}
