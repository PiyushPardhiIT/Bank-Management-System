package bank.management.system1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.SQLException; // Import SQLException for better error handling

public class Pin extends JFrame implements ActionListener {

    JPasswordField t1, t2;
    JButton b1, b2;
    JLabel l1, l2, l3; // l4 is for the image, so keep it local or rename if a field
    String pin;

    Pin(String pin) {
        this.pin = pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bank/management/system1/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3); // Renamed l4 to imageLabel for clarity
        imageLabel.setBounds(0, 0, 960, 1080);
        add(imageLabel);

        l1 = new JLabel("CHANGE YOUR PIN");
        l1.setFont(new Font("System", Font.BOLD, 16));
        l1.setForeground(Color.WHITE);

        l2 = new JLabel("New PIN:");
        l2.setFont(new Font("System", Font.BOLD, 16));
        l2.setForeground(Color.WHITE);

        l3 = new JLabel("Re-Enter New PIN:");
        l3.setFont(new Font("System", Font.BOLD, 16));
        l3.setForeground(Color.WHITE);

        t1 = new JPasswordField();
        t1.setFont(new Font("Raleway", Font.BOLD, 25));

        t2 = new JPasswordField();
        t2.setFont(new Font("Raleway", Font.BOLD, 25));

        b1 = new JButton("CHANGE");
        b2 = new JButton("BACK");

        b1.addActionListener(this);
        b2.addActionListener(this);

        setLayout(null); // Use absolute positioning

        l1.setBounds(280, 330, 800, 35);
        imageLabel.add(l1); // Add to imageLabel

        l2.setBounds(180, 390, 150, 35);
        imageLabel.add(l2);

        l3.setBounds(180, 440, 200, 35);
        imageLabel.add(l3);

        t1.setBounds(350, 390, 180, 25);
        imageLabel.add(t1);

        t2.setBounds(350, 440, 180, 25);
        imageLabel.add(t2);

        b1.setBounds(390, 588, 150, 35);
        imageLabel.add(b1);

        b2.setBounds(390, 633, 150, 35);
        imageLabel.add(b2);

        setSize(960, 1080);
        setLocation(500, 0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            // Get passwords as char arrays (recommended)
            char[] newPinChars = t1.getPassword();
            char[] reEnterPinChars = t2.getPassword();

            String newPin = new String(newPinChars); // Convert to String for DB queries
            String reEnterPin = new String(reEnterPinChars);

            // Input Validation - Check for empty fields first
            if (newPin.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter New PIN.");
                return;
            }
            if (reEnterPin.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please re-enter New PIN.");
                return;
            }

            // Check if PINs match
            if (!newPin.equals(reEnterPin)) {
                JOptionPane.showMessageDialog(null, "Entered PINs do not match. Please try again.");
                // Clear fields for retry
                t1.setText("");
                t2.setText("");
                return;
            }
            
            // Optional: Add PIN length validation (e.g., 4 digits)
            if (newPin.length() != 4 || !newPin.matches("\\d{4}")) { // Checks if exactly 4 digits
                 JOptionPane.showMessageDialog(null, "PIN must be a 4-digit number.");
                 t1.setText("");
                 t2.setText("");
                 return;
            }


            if (ae.getSource() == b1) { // "CHANGE" button
                Conn c1 = new Conn();

                // Update PIN in all relevant tables
                // The 'pin' variable in this class holds the OLD PIN.
                // 'newPin'/'rpin' holds the new PIN.
                String updateBank = "UPDATE bank SET pin = '" + newPin + "' WHERE pin = '" + this.pin + "'";
                String updateLogin = "UPDATE login SET pin = '" + newPin + "' WHERE pin = '" + this.pin + "'";
                String updateSignup3 = "UPDATE signupthree SET pin = '" + newPin + "' WHERE pin = '" + this.pin + "'";

                c1.s.executeUpdate(updateBank);
                c1.s.executeUpdate(updateLogin);
                c1.s.executeUpdate(updateSignup3);

                JOptionPane.showMessageDialog(null, "PIN changed successfully.");
                setVisible(false);
                // Pass the new PIN to Transactions
                new Transactions(newPin).setVisible(true);

            } else if (ae.getSource() == b2) { // "BACK" button
                setVisible(false);
                new Transactions(this.pin).setVisible(true); // Go back with the OLD pin
            }
        } catch (SQLException e) { // Catch specific SQL exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (Exception e) { // Catch any other unexpected exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
        } finally {
             // It's good practice to clear char arrays containing sensitive data
             t1.setText(""); // Clear the password fields
             t2.setText("");
        }
    }

    public static void main(String[] args) {
        // For testing, pass a dummy OLD PIN.
        new Pin("1234").setVisible(true);
    }
}
