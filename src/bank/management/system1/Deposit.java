package bank.management.system1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.sql.SQLException; // Import SQLException for better error handling
import java.text.SimpleDateFormat; // Import SimpleDateFormat for date formatting

public class Deposit extends JFrame implements ActionListener {

    JTextField t1; // Removed unused t2
    JButton b1,b2; // Removed unused b3
    JLabel l1; // Removed unused l2, l3 (the class member l3)
    String pin;

    Deposit(String pin){
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bank/management/system1/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3); // Renamed to avoid conflict with former l3 field
        imageLabel.setBounds(0, 0, 960, 1080);
        add(imageLabel);

        l1 = new JLabel("ENTER AMOUNT YOU WANT TO DEPOSIT");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        t1 = new JTextField();
        t1.setFont(new Font("Raleway", Font.BOLD, 22));

        b1 = new JButton("DEPOSIT");
        b2 = new JButton("BACK");

        setLayout(null);

        l1.setBounds(190,350,400,35);
        imageLabel.add(l1); // Add to imageLabel

        t1.setBounds(190,420,320,25);
        imageLabel.add(t1); // Add to imageLabel

        b1.setBounds(390,588,150,35);
        imageLabel.add(b1); // Add to imageLabel

        b2.setBounds(390,633,150,35);
        imageLabel.add(b2); // Add to imageLabel

        b1.addActionListener(this);
        b2.addActionListener(this);

        setSize(960,1080);
        setUndecorated(true);
        setLocation(500,0);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        try{
            String amountStr = t1.getText(); // Get amount as string from text field

            // NEW: Format the date to a SQL-compatible string
            Date date = new Date(); // Get current date and time
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define desired format
            String strDate = formatter.format(date); // Convert Date object to formatted String

            if(ae.getSource()==b1){ // If "DEPOSIT" button is clicked
                if(amountStr.isEmpty()){ // Check if amount field is empty
                    JOptionPane.showMessageDialog(null, "Please enter the Amount you want to Deposit.");
                    return; // Stop execution if empty
                }

                // Attempt to parse amount to an integer to catch non-numeric input early
                int amount;
                try {
                    amount = Integer.parseInt(amountStr);
                    if (amount <= 0) { // Basic validation: deposit amount must be positive
                        JOptionPane.showMessageDialog(null, "Deposit amount must be positive.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid Amount. Please enter a numerical value.");
                    return; // Stop execution if not a valid number
                }

                Conn c1 = new Conn(); // Establish database connection
                // Execute update to insert the deposit transaction
                // Use the formatted date string (strDate) and the parsed integer amount
                c1.s.executeUpdate("INSERT INTO bank VALUES('"+pin+"', '"+strDate+"', 'Deposit', '"+amount+"')");

                JOptionPane.showMessageDialog(null, "Rs. "+amount+" Deposited Successfully.");

                setVisible(false); // Hide the current Deposit window
                new Transactions(pin).setVisible(true); // Go back to Transactions
            }else if(ae.getSource()==b2){ // If "BACK" button is clicked
                setVisible(false); // Hide the current Deposit window
                new Transactions(pin).setVisible(true); // Go back to Transactions
            }
        }catch(SQLException e){ // Catch specific SQL exceptions
            e.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }catch(Exception e){ // Catch any other unexpected exceptions
            e.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        // For testing purposes, pass a dummy PIN.
        new Deposit("1234").setVisible(true);
    }
}