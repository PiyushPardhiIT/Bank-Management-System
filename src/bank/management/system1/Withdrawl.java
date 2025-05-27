package bank.management.system1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date; // Still using java.util.Date
import java.sql.*;
import java.text.SimpleDateFormat; // NEW: Import for date formatting

public class Withdrawl extends JFrame implements ActionListener{

    JTextField t1; // Removed t2 as it wasn't used
    JButton b1,b2; // Removed b3 as it wasn't used
    JLabel l1,l2; // Removed l3, l4 as they were either unused or re-declared
    String pin;

    Withdrawl(String pin){
        this.pin = pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bank/management/system1/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3); // Renamed to avoid confusion with existing l3
        imageLabel.setBounds(0, 0, 960, 1080);
        add(imageLabel);

        l1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        l2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("System", Font.BOLD, 16));

        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 25));

        b1 = new JButton("WITHDRAW");
        b2 = new JButton("BACK");

        setLayout(null);

        l1.setBounds(190,350,400,20);
        imageLabel.add(l1); // Add to the imageLabel for proper layering

        l2.setBounds(190,400,400,20);
        imageLabel.add(l2); // Add to the imageLabel

        t1.setBounds(190,450,330,30);
        imageLabel.add(t1); // Add to the imageLabel

        b1.setBounds(390,588,150,35);
        imageLabel.add(b1); // Add to the imageLabel

        b2.setBounds(390,633,150,35);
        imageLabel.add(b2); // Add to the imageLabel

        b1.addActionListener(this);
        b2.addActionListener(this);

        setSize(960,1080);
        setLocation(500,0);
        setUndecorated(true); // Removes window decorations (title bar, close buttons)
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        try{
            String amount = t1.getText(); // Get amount from text field

            // NEW: Format the date to a SQL-compatible string
            Date date = new Date(); // Get current date and time
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define desired format
            String strDate = formatter.format(date); // Convert Date object to formatted String

            if(ae.getSource()==b1){ // If "WITHDRAW" button is clicked
                if(amount.isEmpty()){ // Check if amount field is empty
                    JOptionPane.showMessageDialog(null, "Please enter the Amount you want to Withdraw.");
                    return; // Stop execution if empty
                }

                Conn c1 = new Conn();
                ResultSet rs = c1.s.executeQuery("SELECT * FROM bank WHERE pin = '"+pin+"'");
                int balance = 0;

                while(rs.next()){
                    if(rs.getString("type").equals("Deposit")){
                        balance += Integer.parseInt(rs.getString("amount"));
                    }else{ // "Withdrawl" or other debit type
                        balance -= Integer.parseInt(rs.getString("amount"));
                    }
                }
                rs.close(); // Good practice: close the ResultSet

                int withdrawAmount = Integer.parseInt(amount); // Convert withdrawal amount to int

                if(balance < withdrawAmount){
                    JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                    return; // Stop execution if balance is insufficient
                }

                // Insert the withdrawal transaction into the bank table
                String query = "INSERT INTO bank VALUES('"+pin+"', '"+strDate+"', 'Withdrawl', '"+withdrawAmount+"')";
                c1.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Rs. "+withdrawAmount+" Debited Successfully.");

                setVisible(false); // Hide the current Withdrawl window
                new Transactions(pin).setVisible(true); // Go back to Transactions
            }else if(ae.getSource()==b2){ // If "BACK" button is clicked
                setVisible(false); // Hide the current Withdrawl window
                new Transactions(pin).setVisible(true); // Go back to Transactions
            }
        }catch(NumberFormatException e){ // Catch specific error if amount is not a valid number
            JOptionPane.showMessageDialog(null, "Please enter a valid numerical amount.");
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace(); // Print any other exceptions for debugging
            System.out.println("Error in Withdrawl: "+e.getMessage()); // More specific error message
            JOptionPane.showMessageDialog(null, "An error occurred during withdrawal. Please try again.");
        }
    }

    public static void main(String[] args){
        new Withdrawl("").setVisible(true); // For testing purposes, pass an empty string or a dummy PIN
    }
}