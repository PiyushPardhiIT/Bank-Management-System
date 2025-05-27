package bank.management.system1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener{
    JLabel l1,l2,l3;
    JTextField tf1;
    JPasswordField pf2;
    JButton b1,b2,b3;
  
    Login(){
        setTitle("AUTOMATED TELLER MACHINE");
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bank/management/system1/icons/logo.jpg"));
        Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l11 = new JLabel(i3);
        l11.setBounds(70, 10, 100, 100);
        add(l11);
        
        l1 = new JLabel("WELCOME TO ATM");
        l1.setFont(new Font("Osward", Font.BOLD, 38));
        l1.setBounds(200,40,450,40);
        add(l1);
        
        l2 = new JLabel("Card No:");
        l2.setFont(new Font("Raleway", Font.BOLD, 28));
        l2.setBounds(125,150,375,30);
        add(l2);
        
        tf1 = new JTextField(15);
        tf1.setBounds(300,150,230,30);
        tf1.setFont(new Font("Arial", Font.BOLD, 14));
        add(tf1);
        
        l3 = new JLabel("PIN:");
        l3.setFont(new Font("Raleway", Font.BOLD, 28));
        l3.setBounds(125,220,375,30);
        add(l3);
        
        pf2 = new JPasswordField(15);
        pf2.setFont(new Font("Arial", Font.BOLD, 14));
        pf2.setBounds(300,220,230,30);
        add(pf2);
                
        b1 = new JButton("SIGN IN");
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
        
        b2 = new JButton("CLEAR");
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        
        b3 = new JButton("SIGN UP");
        b3.setBackground(Color.BLACK);
        b3.setForeground(Color.WHITE);
        
        setLayout(null);
        
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        b1.setBounds(300,300,100,30);
        add(b1);
        
        b2.setFont(new Font("Arial", Font.BOLD, 14));
        b2.setBounds(430,300,100,30);
        add(b2);
        
        b3.setFont(new Font("Arial", Font.BOLD, 14));
        b3.setBounds(300,350,230,30);
        add(b3);
        
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        
        getContentPane().setBackground(Color.WHITE);
        
        setSize(800,480);
        setLocation(550,200);
        setVisible(true);
        
    }
public void actionPerformed(ActionEvent ae){
    try{
        if(ae.getSource()==b1){ // This block handles the "SIGN IN" button
            Conn c1 = new Conn(); // Establish a database connection
            
            String cardnumberInput = tf1.getText(); // Get text from Card Number field
            String pinInput = pf2.getText();       // Get text from PIN field

            // **CRITICAL FIX HERE: Change 'cardno' to 'cardnumber'**
            String q = "select * from login where cardnumber = '"+cardnumberInput+"' and pin = '"+pinInput+"'";

            ResultSet rs = c1.s.executeQuery(q); // Execute the query

            if(rs.next()){ // If a matching record is found
                setVisible(false); // Hide the current Login window
                // Pass the entered PIN to the Transactions window
                new Transactions(pinInput).setVisible(true); // Open the Transactions window
            }else{ // If no matching record is found
                JOptionPane.showMessageDialog(null, "Incorrect Card Number or PIN"); // Show error message
            }
        }else if(ae.getSource()==b2){ // This block handles the "CLEAR" button
            tf1.setText(""); // Clear the card number text field
            pf2.setText(""); // Clear the PIN password field
        }else if(ae.getSource()==b3){ // This block handles the "SIGN UP" button
            setVisible(false); // Hide the current Login window
            new Signup().setVisible(true); // Open the Signup (Page 1) window
        }
    }catch(Exception e){
        e.printStackTrace(); // Print any exceptions to the console for debugging
    }
}    public static void main(String[] args){
        new Login().setVisible(true);
    }

    
}



