package bank.management.system1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.ResultSet; // Explicitly import ResultSet
import java.sql.SQLException; // Explicitly import SQLException

public class MiniStatement extends JFrame implements ActionListener {

    JButton b1; // Removed b2 as it was unused
    JLabel l2, l3, l4; // l1 will be a JTextArea for transactions
    JTextArea transactionArea; // NEW: Use JTextArea for displaying transactions

    MiniStatement(String pin) {
        super("Mini Statement");
        getContentPane().setBackground(Color.WHITE);
        setSize(400, 600);
        setLocation(20, 20);
        setLayout(null); // Use absolute positioning

        // ATM Image (l3 in your original code, renamed for clarity)
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bank/management/system1/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0, 0, 960, 1080); // Ensure it covers the background if needed
        // For MiniStatement, since it's a smaller window, we might not need the full ATM background.
        // If you want the ATM background here, you need to adjust bounds and add components to it.
        // For simplicity and clarity in a mini-statement, I'll keep the background white
        // and arrange components directly on the JFrame. If you want the ATM image,
        // you'd add this 'imageLabel' to the JFrame and then add all other components TO 'imageLabel'.
        // For now, I'll assume a clean white background.

        // "Indian Bank" label
        l2 = new JLabel("Indian Bank");
        l2.setBounds(150, 20, 100, 20);
        add(l2);

        // Card Number Label
        l3 = new JLabel();
        l3.setBounds(20, 80, 350, 20); // Increased width
        add(l3);

        // Transaction History Area (JTextArea inside JScrollPane)
        transactionArea = new JTextArea();
        transactionArea.setEditable(false); // Make it read-only
        transactionArea.setFont(new Font("System", Font.PLAIN, 12));
        transactionArea.setText("Date        Type          Amount\n"); // Header for transactions

        JScrollPane scrollPane = new JScrollPane(transactionArea);
        scrollPane.setBounds(20, 110, 360, 250); // Adjust bounds for scrollable area
        add(scrollPane);

        // Total Balance Label
        l4 = new JLabel();
        l4.setBounds(20, 400, 300, 20);
        add(l4);

        // Get Card Number from login table
        Conn cLogin = null;
        ResultSet rsLogin = null;
        try {
            cLogin = new Conn();
            // CRITICAL FIX: Use "cardnumber" instead of "cardno"
            rsLogin = cLogin.s.executeQuery("SELECT cardnumber FROM login WHERE pin = '" + pin + "'");
            if (rsLogin.next()) {
                String fullCardNumber = rsLogin.getString("cardnumber");
                // Mask the card number for security
                if (fullCardNumber != null && fullCardNumber.length() >= 16) {
                    l3.setText("Card Number:    " + fullCardNumber.substring(0, 4) + "XXXXXXXX" + fullCardNumber.substring(12, 16));
                } else {
                    l3.setText("Card Number:    " + fullCardNumber + " (Masking Error)"); // Handle shorter numbers
                }
            } else {
                 l3.setText("Card Number:    Not Found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error (Login Info): " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred (Login Info): " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rsLogin != null) rsLogin.close();
                // Close cLogin.s and cLogin.c if Conn objects are not intended for reuse.
                // For simplicity here, assuming Conn manages its single statement/connection throughout the app.
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        // Get Transaction History and calculate Balance from bank table
        Conn cBank = null;
        ResultSet rsBank = null;
        int balance = 0;
        try {
            cBank = new Conn();
            // CRITICAL FIX: Use "type" instead of "mode" for the transaction type column
            rsBank = cBank.s.executeQuery("SELECT date, type, amount FROM bank WHERE pin = '" + pin + "' ORDER BY date ASC");

            while (rsBank.next()) {
                // Append transaction details to JTextArea
                transactionArea.append(rsBank.getString("date") + "    " +
                                       String.format("%-10s", rsBank.getString("type")) + "    " + // Format type for alignment
                                       rsBank.getString("amount") + "\n");

                // Calculate balance based on type
                if (rsBank.getString("type").equals("Deposit")) {
                    balance += Integer.parseInt(rsBank.getString("amount"));
                } else if (rsBank.getString("type").equals("Withdrawl")) { // Assuming "Withdrawl" for debit
                    balance -= Integer.parseInt(rsBank.getString("amount"));
                }
            }
            l4.setText("Your total Balance is Rs " + balance);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error (Transactions): " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Data Error: Invalid amount in transaction history.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred (Transactions): " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rsBank != null) rsBank.close();
                // Close cBank.s and cBank.c if Conn objects are not intended for reuse.
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        b1 = new JButton("Exit");
        b1.setBounds(20, 500, 100, 25); // Position the Exit button
        add(b1); // Add the button to the JFrame

        b1.addActionListener(this); // Add action listener for the Exit button
    }

    public void actionPerformed(ActionEvent ae) {
        // This method handles the action for the "Exit" button
        // When Exit is clicked, simply close the MiniStatement window
        this.dispose(); // Use dispose() to release resources
    }

    public static void main(String[] args) {
        // For testing, provide a dummy PIN. Replace with a real PIN from your DB for actual testing.
        new MiniStatement("1234").setVisible(true);
    }
}