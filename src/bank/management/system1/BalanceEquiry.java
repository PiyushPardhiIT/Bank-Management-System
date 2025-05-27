package bank.management.system1;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet; // Explicitly import ResultSet
import java.sql.SQLException; // <--- ADD THIS IMPORT
import javax.swing.*;
import java.util.*; // Keep if you actually use Date or other util classes, otherwise remove

class BalanceEnquiry extends JFrame implements ActionListener {

    // Removed unused JTextFields t1, t2 and JLabel l2, l3
    JButton b1; // Only b1 is used for BACK
    JLabel l1; // This JLabel will display the balance
    String pin;

    BalanceEnquiry(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bank/management/system1/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3); // Renamed for clarity, avoiding conflict with l3 variable
        imageLabel.setBounds(0, 0, 960, 1080);
        add(imageLabel);

        l1 = new JLabel(); // Initialize the JLabel that will display the balance
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        b1 = new JButton("BACK");

        setLayout(null); // Use absolute positioning

        l1.setBounds(190, 350, 400, 35); // Position the balance label
        imageLabel.add(l1); // Add label to the imageLabel so it appears on top of the ATM image

        b1.setBounds(390, 633, 150, 35); // Position the BACK button
        imageLabel.add(b1); // Add button to the imageLabel

        int balance = 0; // Initialize balance

        // Moved the database logic into a proper try-catch block
        // and ensure resources are closed.
        Conn c1 = null; // Declare Conn object outside try block
        ResultSet rs = null; // Declare ResultSet object outside try block
        try {
            c1 = new Conn(); // Establish database connection
            // Execute query to get all transactions for the current PIN
            rs = c1.s.executeQuery("SELECT type, amount FROM bank WHERE pin = '" + pin + "'");

            while (rs.next()) { // Iterate through the transaction records
                if (rs.getString("type").equals("Deposit")) {
                    balance += Integer.parseInt(rs.getString("amount"));
                } else if (rs.getString("type").equals("Withdrawl")) { // Assuming "Withdrawl" for debit
                    balance -= Integer.parseInt(rs.getString("amount"));
                }
                // Handle any other transaction types if they exist in your 'mode' column
            }
        } catch (SQLException e) {
            // Print stack trace for SQL errors
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            // Handle cases where 'amount' might not be a valid integer
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Data format error: Invalid amount in transaction history.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close ResultSet and Statement/Connection to free resources
            try {
                if (rs != null) rs.close();
                // If Conn.java uses a single Statement/Connection object, it might be closed
                // when the Conn object goes out of scope or the application exits.
                // For a more robust solution in a larger app, you'd close here.
                // For this simple structure, you might skip closing c1.s and c1.c here
                // if they are meant to be reused across different UI components,
                // but generally closing is better.
                // if (c1 != null && c1.s != null) c1.s.close();
                // if (c1 != null && c1.c != null) c1.c.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        // Set the text of the label AFTER the balance has been calculated
        l1.setText("Your Current Account Balance is Rs " + balance);

        b1.addActionListener(this); // Add action listener for the BACK button

        setSize(960, 1080);
        setUndecorated(true); // Keep the frame undecorated for ATM look
        setLocation(500, 0);
        setVisible(true);
    }

    // This method handles the action for the BACK button
    public void actionPerformed(ActionEvent ae) {
        setVisible(false); // Hide the current Balance Enquiry window
        new Transactions(pin).setVisible(true); // Go back to the Transactions window
    }

    public static void main(String[] args) {
        // For testing, provide a dummy PIN. Replace with a real PIN if testing directly.
        new BalanceEnquiry("1234").setVisible(true);
    }
}