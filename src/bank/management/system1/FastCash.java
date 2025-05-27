package bank.management.system1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*; // Import all SQL classes, including ResultSet and SQLException
import java.util.Date;
import java.text.SimpleDateFormat; // NEW: Import for date formatting

public class FastCash extends JFrame implements ActionListener {

    JLabel l1; // Removed unused l2
    JButton b1, b2, b3, b4, b5, b6, b7; // Removed unused b8
    // Removed unused JTextField t1
    String pin;

    FastCash(String pin) {
        this.pin = pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bank/management/system1/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3); // Renamed to avoid confusion with existing l3
        imageLabel.setBounds(0, 0, 960, 1080);
        add(imageLabel);

        l1 = new JLabel("SELECT WITHDRAWL AMOUNT");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        b1 = new JButton("Rs 100");
        b2 = new JButton("Rs 500");
        b3 = new JButton("Rs 1000");
        b4 = new JButton("Rs 2000");
        b5 = new JButton("Rs 5000");
        b6 = new JButton("Rs 10000");
        b7 = new JButton("BACK");

        setLayout(null);

        l1.setBounds(235, 400, 700, 35);
        imageLabel.add(l1); // Add to imageLabel

        b1.setBounds(170, 499, 150, 35);
        imageLabel.add(b1);

        b2.setBounds(390, 499, 150, 35);
        imageLabel.add(b2);

        b3.setBounds(170, 543, 150, 35);
        imageLabel.add(b3);

        b4.setBounds(390, 543, 150, 35);
        imageLabel.add(b4);

        b5.setBounds(170, 588, 150, 35);
        imageLabel.add(b5);

        b6.setBounds(390, 588, 150, 35);
        imageLabel.add(b6);

        b7.setBounds(390, 633, 150, 35);
        imageLabel.add(b7);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        b7.addActionListener(this);

        setSize(960, 1080);
        setLocation(500, 0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            // Get the text from the button pressed and extract the amount (e.g., "Rs 100" -> "100")
            String buttonText = ((JButton)ae.getSource()).getText();
            String amount = "";

            if (ae.getSource() == b7) { // If BACK button is clicked
                this.setVisible(false);
                new Transactions(pin).setVisible(true);
                return; // Exit the method here
            } else { // For withdrawal amount buttons
                amount = buttonText.substring(3); // "Rs 100" -> "100"
            }

            Conn c = new Conn();
            ResultSet rs = null; // Declare ResultSet outside try for finally block

            int balance = 0;
            try {
                rs = c.s.executeQuery("SELECT type, amount FROM bank WHERE pin = '"+pin+"'");
                while (rs.next()) {
                    // CRITICAL FIX: Use "type" column name instead of "mode"
                    if (rs.getString("type").equals("Deposit")) {
                        balance += Integer.parseInt(rs.getString("amount"));
                    } else if (rs.getString("type").equals("Withdrawl")) { // Explicitly check "Withdrawl"
                        balance -= Integer.parseInt(rs.getString("amount"));
                    }
                }
            } finally {
                if (rs != null) {
                    rs.close(); // Close ResultSet
                }
            }

            int withdrawAmount = Integer.parseInt(amount); // Convert string amount to integer

            // Check for insufficient balance AFTER amount is parsed and balance calculated
            if (balance < withdrawAmount) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                return; // Stop execution if balance is insufficient
            }

            // NEW: Format the date to a SQL-compatible string
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = formatter.format(date);

            // Execute update to insert the withdrawal transaction
            c.s.executeUpdate("INSERT INTO bank VALUES('"+pin+"', '"+strDate+"', 'Withdrawl', '"+withdrawAmount+"')");

            JOptionPane.showMessageDialog(null, "Rs. "+withdrawAmount+" Debited Successfully.");

            setVisible(false); // Hide current frame
            new Transactions(pin).setVisible(true); // Go back to Transactions

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Amount. Please select a valid amount.");
            e.printStackTrace();
        } catch (SQLException e) { // Catch specific SQL exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (Exception e) { // Catch any other unexpected exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new FastCash("").setVisible(true); // For testing, pass a dummy PIN
    }
}