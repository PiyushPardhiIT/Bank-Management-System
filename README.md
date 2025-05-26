# Bank-Management-System

[Bank-Management-System]
A simple, desktop-based Bank Management System built with Java Swing and MySQL, providing core banking functionalities like account creation, deposits, withdrawals, PIN changes, and mini-statements.

Table of Contents
Features
Technologies Used
Database Setup
Installation & Setup
How to Run
Screenshots
Contributing
Contact
Features

Account Creation: New users can sign up and create a bank account.
Login System: Secure login using Card Number and PIN.
Transactions:
Deposit: Add funds to the account.
Withdrawal: Withdraw funds from the account.
Fast Cash: Quick withdrawals of predefined amounts.
PIN Change: Users can update their ATM PIN.
Balance Enquiry: View the current account balance.
Mini Statement: Access a brief history of recent transactions.
Database Integration: All account and transaction data is stored persistently in a MySQL database.
Technologies Used
Frontend: Java Swing (GUI)
Backend/Database: MySQL
JDBC: For Java-Database connectivity
IDE: Apache NetBeans (recommended for this project structure)
Database Setup
This project requires a MySQL database to store bank account and transaction information.

Install MySQL: If you don't have MySQL installed, download and install it (e.g., MySQL Community Server, MySQL Workbench).

Create Database:
Open MySQL Workbench or your preferred MySQL client and execute the following SQL to create the database:

SQL

CREATE DATABASE bankmanagementsystem1;
USE bankmanagementsystem1;
Create Tables: Execute the following SQL commands to create the necessary tables:

SQL

-- Table for Signup (Form 1)
CREATE TABLE signup (
    formno VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    fname VARCHAR(100),
    dob VARCHAR(50),
    gender VARCHAR(10),
    email VARCHAR(100),
    marital_status VARCHAR(20),
    address VARCHAR(200),
    city VARCHAR(100),
    pincode VARCHAR(10),
    state VARCHAR(100)
);

-- Table for Signup (Form 2) - Additional Details
CREATE TABLE signup2 (
    formno VARCHAR(20) PRIMARY KEY,
    religion VARCHAR(50),
    category VARCHAR(50),
    income VARCHAR(50),
    education VARCHAR(100),
    occupation VARCHAR(100),
    pan VARCHAR(20),
    aadhar VARCHAR(20),
    senior_citizen VARCHAR(5),
    existing_account VARCHAR(5)
);

-- Table for Signup (Form 3) - Account Details
CREATE TABLE signup3 (
    formno VARCHAR(20) PRIMARY KEY,
    accountType VARCHAR(50),
    cardnumber VARCHAR(20),
    pin VARCHAR(10),
    facility VARCHAR(255)
);

-- Table for Login Credentials
CREATE TABLE login (
    formno VARCHAR(20) PRIMARY KEY,
    cardnumber VARCHAR(20),
    pin VARCHAR(10)
);

-- Table for Bank Transactions (Crucial for balance and mini statement)
CREATE TABLE bank (
    pin VARCHAR(10),
    date DATETIME, -- Changed from VARCHAR to DATETIME for proper date handling
    type VARCHAR(20), -- Changed from 'mode' to 'type' based on consistent code usage
    amount VARCHAR(20)
);
Important Note: The bank table's date column is set to DATETIME. Ensure your Java code (especially Deposit, Withdrawl, FastCash) uses SimpleDateFormat("yyyy-MM-dd HH:mm:ss") to format dates correctly for this column.

JDBC Driver: Ensure you have the MySQL JDBC Connector JAR file in your project's libraries. You can download it from the official MySQL website or add it via your IDE (e.g., in NetBeans, right-click "Libraries" -> "Add JAR/Folder...").

Installation & Setup
Clone the repository:
Bash


Open in NetBeans (Recommended):
Open NetBeans IDE.
Go to File > Open Project...
Navigate to the cloned repository folder and select it.
Add MySQL JDBC Driver:
In NetBeans, in the "Projects" window, expand your project.
Right-click on "Libraries" and select "Add JAR/Folder...".
Navigate to where you saved your mysql-connector-java-X.X.X.jar file and select it.
Configure Database Connection (Conn.java):
Locate the Conn.java file in your bank.management.system1 package.
Update the database connection URL, username, and password if they are different from the defaults. <!-- end list -->
Java

package bank.management.system1;

import java.sql.*;

public class Conn{
    Connection c;
    Statement s;
    public Conn(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); // For MySQL 8.0+
            c = DriverManager.getConnection("jdbc:mysql:///bankmanagementsystem1", "root", "your_mysql_password"); // <-- UPDATE PASSWORD
            s = c.createStatement();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}

How to Run
Build the project: In NetBeans, right-click on your project in the "Projects" window and select "Clean and Build".
Run the application:
To start the Bank Management System, right-click on Login.java (or your main entry point, usually the first window) and select "Run File".


Example Placeholder:

Login Screen:
Signup Form:
Transactions Menu:
Mini Statement:


Contributing
Feel free to fork this repository and contribute!

Fork the Project
Create your Feature Branch (git checkout -b feature/AmazingFeature)
Commit your Changes (git commit -m 'Add some AmazingFeature')
Push to the Branch (git push origin feature/AmazingFeature)
Open a Pull Request


Contact
[Piyush Pardhi] - [pardhipiyush07@gmail.com] - [www.linkedin.com/in/piyush-pardhi-1816a12ba]

